package nnero.redis.impl;

import com.google.common.base.Strings;
import nnero.net.Transport;
import nnero.redis.Buffer;
import nnero.redis.Protocol;
import nnero.redis.ResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import static nnero.redis.ResponseType.*;

/**
 * Author: NNERO
 * Time : 下午3:10 19-2-13
 */
public class RedisProtocol implements Protocol {

    private static final String END_TAG = "\r\n";

    private final static Logger LOG = LoggerFactory.getLogger(RedisProtocol.class);

    private Transport transport;

    public RedisProtocol(Transport transport){
        if(transport == null){
            throw new NullPointerException("transport should not be null!");
        }
        this.transport = transport;
    }

    private RedisRequest encodeCommand(String command) {
        if(Strings.isNullOrEmpty(command)){
            LOG.warn("command is null!");
            return null;
        }
        String[] args = command.split(" ");
        int argc = args.length;
        StringBuilder sb = new StringBuilder();
        sb.append(ResponseType.ARGC.getTag()).append(argc).append(END_TAG);
        for(String arg : args){
            sb.append(ResponseType.LEN.getTag()).append(arg.length()).append(END_TAG)
                    .append(arg).append(END_TAG);
        }
        RedisRequest request = new RedisRequest(command,sb.toString());
        LOG.debug("encode: "+request.getVisualEncodeString());
        return request;
    }

    private ResponseType getResponseType(byte start){
        if(start == STATUS.getTag()){
            return STATUS;
        } else if(start == ResponseType.ERROR.getTag()){
            return ResponseType.ERROR;
        } else if(start == ResponseType.INT.getTag()){
            return ResponseType.INT;
        } else if(start == ResponseType.LEN.getTag()){
            return ResponseType.LEN;
        } else if(start == ResponseType.ARGC.getTag()){
            return ResponseType.ARGC;
        } else {
            throw new RuntimeException("unknown client type:"+start);
        }
    }

    private boolean isEndTag(byte c1,byte c2){
        return c1 == '\r' && c2 == '\n';
    }

    private Buffer decodeDataWithEnd(Cursor cursor) throws IOException {
        Buffer buffer = new Buffer(128);
        byte b1 = cursor.next();
        byte b2 = cursor.next();
        while (true) {
            if(isEndTag(b1,b2)){
                return buffer;
            }
            buffer.add(b1);
            b1 = b2;
            b2 = cursor.next();
        }
    }

    private String decodeStringWithLen(Cursor cursor) throws IOException {
        Buffer buffer = new Buffer(128);
        byte b1 = cursor.next();
        byte b2 = cursor.next();
        while (!isEndTag(b1,b2)) {
            buffer.add(b1);
            b1 = b2;
            b2 = cursor.next();
        }
        int len = parseInt(buffer.get(),buffer.getLength());
        if(len == -1){ //null
            return "nil";
        }
        buffer.clear();
        for(int i=0;i<len;i++){
            buffer.add(cursor.next());
        }
        if(isEndTag(cursor.next(),cursor.next())){
            return new String(buffer.get(),0,buffer.getLength(),Charset.defaultCharset());
        } else {
            throw new RuntimeException("broken data. not end with \\r\\n");
        }
    }

    private int parseInt(byte[] data,int len){
        int v = 0;
        for(int i=len-1;i>=0;i--){
            byte b = data[i];
            if(i == 0 && b == '-'){
                return -v;
            }
            if(b < 48 || b > 57){
                throw new RuntimeException("broken data. can't convert to int");
            }
            v += (b-48) * Math.pow(10,len-i-1);
        }
        return v;
    }

    private RedisResponse decodeStatus(Cursor cursor) throws IOException {
        LOG.debug("status msg decode");
        Buffer data = decodeDataWithEnd(cursor);
        return new RedisResponse(ResponseType.STATUS,
                new String(data.get(),0,data.getLength(), Charset.defaultCharset()));
    }

    private RedisResponse decodeError(Cursor cursor) throws IOException {
        LOG.debug("error msg decode");
        Buffer data = decodeDataWithEnd(cursor);
        return new RedisResponse(ResponseType.ERROR,
                new String(data.get(),0,data.getLength(),Charset.defaultCharset()));
    }

    private RedisResponse decodeInt(Cursor cursor) throws IOException {
        LOG.debug("int msg decode");
        Buffer data = decodeDataWithEnd(cursor);
        int i = parseInt(data.get(),data.getLength());
        return new RedisResponse(ResponseType.INT,i);
    }

    private RedisResponse decodeLen(Cursor cursor) throws IOException {
        LOG.debug("len msg decode");
        String data = decodeStringWithLen(cursor);
        return new RedisResponse(ResponseType.LEN,data);
    }

    private RedisResponse decodeArgc(Cursor cursor) throws IOException {
        LOG.debug("argc msg decode");
        Buffer buffer = decodeDataWithEnd(cursor);
        int argc = parseInt(buffer.get(),buffer.getLength());
        RedisResponse response = new RedisResponse(ResponseType.ARGC);
        for(int i=0;i<argc;i++){
            byte start = cursor.next();
            ResponseType type = getResponseType(start);
            switch (type){
                case STATUS:
                    response.addString(decodeStatus(cursor).getStrData());
                    break;
                case INT:
                    response.addString(String.valueOf(decodeInt(cursor).getIntData()));
                    break;
                case ERROR:
                    response.addString(decodeError(cursor).getStrData());
                    break;
                case LEN:
                    response.addString(decodeStringWithLen(cursor));
                    break;
                case ARGC: //recursion
                    response.addAllString(decodeArgc(cursor).getStrListData());
                    break;
                default:
                    throw new RuntimeException("argc::unknow start tag:\""+start+"\"");
            }
        }
        return response;
    }

    @Override
    public RedisResponse sendCommand(String command) throws IOException {
        RedisRequest request = encodeCommand(command);
        transport.send(request.getEncodeCommand());
        Cursor cursor = new Cursor(transport);
        byte start = cursor.next();
        ResponseType type = getResponseType(start);
        switch (type) {
            case STATUS:
                return decodeStatus(cursor);
            case INT:
                return decodeInt(cursor);
            case ERROR:
                return decodeError(cursor);
            case LEN:
                return decodeLen(cursor);
            case ARGC:
                return decodeArgc(cursor);
            default:
                throw new RuntimeException("unknown start tag:\"" + start + "\"");
        }
    }

    @Override
    public String debugCommand(String command) throws IOException {
        RedisRequest request = encodeCommand(command);
        transport.send(request.getEncodeCommand());
        Cursor cursor = new Cursor(transport);
        Buffer buffer = new Buffer();
        while (!cursor.isMaybeEnd() || !cursor.needReceive()){
            buffer.add(cursor.next0());
        }
        String msg = new String(buffer.get(),0,buffer.getLength(),Charset.defaultCharset());
        return msg.replace("\r\n","\\r\\n");
    }

    static class Cursor{

        private static final int BUFFER_SIZE = 1024;

        private int current;

        private byte[] buffer;

        private Transport transport;

        private int len;

        private boolean maybeEnd;

        public Cursor(Transport transport){
            buffer = new byte[BUFFER_SIZE];
            current = 0;
            this.transport = transport;
        }

        public byte next0() throws IOException {
            if(needReceive()) {
                if(maybeEnd){
                    return -1;
                }
                len = transport.receive(buffer, 0, buffer.length);
                current = 0;
                maybeEnd = len < buffer.length;
            }
            byte b = buffer[current];
            current++;
            return b;
        }

        public byte next() throws IOException {
            if(needReceive()) {
                len = transport.receive(buffer, 0, buffer.length);
                current = 0;
            }
            byte b = buffer[current];
            current++;
            return b;
        }

        public boolean isMaybeEnd(){
            return maybeEnd;
        }

        boolean needReceive(){
            return current >= len;
        }

    }
}
