package nnero.redis.impl;

import nnero.redis.ResponseType;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: NNERO
 * Time : 下午3:12 19-2-13
 */
public class RedisResponse {

    private ResponseType type;

    private String strData;

    private int intData;

    private List<String> strListData;

    public RedisResponse(ResponseType type,int i){
        this.type = type;
        this.intData = i;
    }

    public RedisResponse(ResponseType type,String str){
        this.type = type;
        this.strData = str;
    }

    public RedisResponse(ResponseType type){
        this.type = type;
    }

    public void addString(String s){
        if(type != ResponseType.ARGC){
            throw new RuntimeException("wrong type:"+type+" can't addString()");
        }
        if(strListData == null){
            strListData = new ArrayList<>();
        }
        strListData.add(s);
    }

    public void addAllString(List<String> list){
        if(list == null){
            return;
        }
        if(type != ResponseType.ARGC){
            throw new RuntimeException("wrong type:"+type+" can't addAllString()");
        }
        if(strListData == null){
            strListData = new ArrayList<>();
        }
        strListData.addAll(list);
    }

    public ResponseType getType() {
        return type;
    }

    public String getStrData() {
        return strData;
    }

    public int getIntData() {
        if(type != ResponseType.INT){
            throw new RuntimeException("wrong type:"+type+" can't getIntData");
        }
        return intData;
    }

    public List<String> getStrListData() {
        if(type != ResponseType.ARGC){
            throw new RuntimeException("wrong type:"+type+" can't getStrListData()");
        }
        if(strListData == null){
            strListData = new ArrayList<>();
        }
        return strListData;
    }
}
