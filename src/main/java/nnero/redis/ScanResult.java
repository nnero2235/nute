package nnero.redis;

import java.util.List;

/**
 * Author: NNERO
 * Time : 下午5:42 19-2-14
 */
public class ScanResult {


    private String cursor;

    private List<String> result;

    public ScanResult(String cursor, List<String> result) {
        this.cursor = cursor;
        this.result = result;
    }

    public String getCursor() {
        return cursor;
    }

    public List<String> getResult() {
        return result;
    }
}
