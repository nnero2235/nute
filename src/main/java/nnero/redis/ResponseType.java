package nnero.redis;

/**
 * Author: NNERO
 * Time : 下午3:18 19-2-13
 */
public enum ResponseType {
    STATUS('+'),
    ERROR('-'),
    INT(':'),
    LEN('$'),
    ARGC('*');

    private char tag;

    ResponseType(char c){
        this.tag = c;
    }

    public char getTag(){
        return tag;
    }
}
