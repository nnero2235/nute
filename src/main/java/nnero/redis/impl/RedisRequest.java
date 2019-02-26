package nnero.redis.impl;

import com.google.common.base.Strings;

import java.nio.charset.Charset;

/**
 * Author: NNERO
 * Time : 下午3:02 19-2-13
 */
public class RedisRequest {

    private String encodeCommand;

    private String originCommand;

    public RedisRequest(String origin,String encodeCommand){
        if(Strings.isNullOrEmpty(origin)){
            throw new NullPointerException("origin command is empty!");
        }
        if(Strings.isNullOrEmpty(encodeCommand)){
            throw new NullPointerException("encode command is empty!");
        }
        this.originCommand = origin;
        this.encodeCommand = encodeCommand;
    }

    public byte[] getEncodeBytes(){
        return encodeCommand.getBytes(Charset.defaultCharset());
    }

    public String getVisualEncodeString(){
        return encodeCommand.replace("\r\n","\\r\\n");
    }

    public String getEncodeCommand() {
        return encodeCommand;
    }

    public String getOriginCommand() {
        return originCommand;
    }
}
