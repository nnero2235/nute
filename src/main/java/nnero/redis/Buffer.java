package nnero.redis;

import java.util.Arrays;

/**
 * Author: NNERO
 * Time : 下午7:20 19-2-13
 * 自动扩容
 */
public class Buffer {

    private static final int SIZE = 1024;

    private byte[] buf;

    private int index;

    public Buffer(){
        this(SIZE);
    }

    public Buffer(int size){
        buf = new byte[size];
    }

    public void add(byte b){
        checkExpand();
        buf[index++] = b;
    }

    public void clear(){
        index = 0;
    }

    public int getLength(){
        return index;
    }

    public byte[] get(){
        return buf;
    }

    private void checkExpand(){
        if(index >= buf.length){
            byte[] old = buf;
            buf = new byte[buf.length*2];
            System.arraycopy(old,0,buf,0,old.length);
        }
    }
}
