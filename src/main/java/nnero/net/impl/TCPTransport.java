package nnero.net.impl;

import com.google.common.base.Strings;
import nnero.net.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.Charset;

/**
 * Author: NNERO
 * Time : 下午3:43 19-2-12
 * tcp协议的transport
 */
public class TCPTransport implements Transport {

    private final static Logger LOG = LoggerFactory.getLogger(TCPTransport.class);

    private static final int INIT_MAX_TRANSPORT_SIZE = 1024;

    private String host;

    private int port;

    private Socket clientSocket;

    private int maxTransportSize;

    public TCPTransport(String host,int port) throws IOException {
        this(host,port, INIT_MAX_TRANSPORT_SIZE,true);
    }

    public TCPTransport(String host,int port,int bufferSize,boolean autoConnect) throws IOException {
        if(Strings.isNullOrEmpty(host) || port <= 0 || port > 65535){
            throw new RuntimeException("host or port is wrong.host:"+host+"->port:"+port);
        }
        if(bufferSize <= 0){
            throw new RuntimeException("buffer size must > 0");
        }
        this.host = host;
        this.port = port;
        this.maxTransportSize = bufferSize;
        if(autoConnect){
            checkConnect();
        }
    }

    private void checkConnect() throws IOException {
        if(clientSocket != null && clientSocket.isConnected()){
            return;
        }
        clientSocket = new Socket(this.host,this.port); //创建链接
        LOG.debug("tcp conn was created and connected!");
    }

    private void sendInner(byte[] bytes,int offset,int len) throws IOException {
        OutputStream os = clientSocket.getOutputStream();
        os.write(bytes,offset,len);
        os.flush();
        LOG.debug("send bytes: "+len+"b");
    }

    @Override
    public void send(byte[] bytes) throws IOException {
        if(bytes == null){
            throw new NullPointerException("bytes is null");
        }
        checkConnect();
        sendInner(bytes,0,bytes.length);
    }

    @Override
    public void send(byte[] bytes, int offset, int len) throws IOException {
        if(bytes == null){
            throw new NullPointerException("bytes is null");
        }
        checkConnect();
        sendInner(bytes,offset,len);
    }

    @Override
    public void send(String data) throws IOException {
        if(Strings.isNullOrEmpty(data)){
            LOG.warn("data is empty. so don't send.");
            return;
        }
        checkConnect();
        byte[] bytes = data.getBytes(Charset.defaultCharset());
        int len = 0;
        for (int offset=0;offset<bytes.length;offset+=len){
            len = Math.min(maxTransportSize,bytes.length-offset);
            sendInner(bytes,offset,len);
        }
    }

    @Override
    public int receive(byte[] buf, int offset, int len) throws IOException {
        if(buf == null){
            throw new NullPointerException("buf is null");
        }
        checkConnect();
        InputStream in = clientSocket.getInputStream();
        int c = in.read(buf,offset,len);
        LOG.debug("receive bytes: "+c+"b");
        return c;
    }

    @Override
    public void close() throws IOException {
        if(clientSocket != null && clientSocket.isConnected()){
            LOG.debug("socket closed!");
            clientSocket.close();
        }
    }

    @Override
    public boolean isClosed() {
        if(clientSocket != null){
            return clientSocket.isClosed();
        }
        return true;
    }
}
