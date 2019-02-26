package test;

import java.io.*;
import java.time.Duration;
import java.time.Instant;

/**
 * Author: NNERO
 * Time : 上午10:52 19-2-1
 */
public class TestUtils {

    public interface Callback{
        void run(InputStream in);
    }

    public static void writeDataToFile(String filename,String data) throws IOException {
        FileOutputStream os = new FileOutputStream(filename);
        os.write(data.getBytes());
        os.close();
    }

    public static void doWhenInputRead(String filename,Callback callback) throws IOException {
        if(callback == null){
            throw new NullPointerException("callback is null");
        }
        FileInputStream in = new FileInputStream(filename);
        callback.run(in);
        in.close();
    }

    public static Instant getNow(){
        return Instant.now();
    }

    public static String getCost(Instant start){
        return Duration.between(start,Instant.now()).toString();
    }
}
