package com.b.redis;

/**
 * Created by wangbil on 9/12/2016.
 */

import org.apache.commons.io.FileUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.File;
import java.util.List;

public class JedisDemoMain {
    public static void main(String args[]) throws Exception {
        //Connecting to Redis server on localhost


        Jedis jedis = new Jedis("<IP>", 27841, 0);//connect master or
        jedis.connect();
        int preDefineTotal = 25000000;
        int perIterate = 2500;
        ScanParams params = new ScanParams();
        params.match("*");//"[EXTERNAL-INTERNAL]*"
        params.count(perIterate);

        String cursorLast = "-1";
        String cursorCurrent = "0";


        StringBuilder sb = new StringBuilder();
        while (!cursorLast.equals(cursorCurrent) ) {
            ScanResult<String> scanResult = jedis.scan(cursorCurrent, params);
            List<String> result = scanResult.getResult();
            cursorLast = cursorCurrent;
            cursorCurrent = new String(scanResult.getCursorAsBytes());

            if (result.size() > 0) {
                for (String key : result) {
                    System.out.println(key);
                    sb.append(key+"\n");

                }
            }
        }
        FileUtils.write(new File("C:\\UBS\\Dev\\xldn4160vdap.ldn.swissbank.com_27822.txt"), sb.toString());

    }

    public static final String TYPE_STRING = "string";
    public static final String TYPE_HASH = "hash";
    public static final String TYPE_ZSET = "zset";


    static String rs(String str, int i) {
        if (str == null || str.length() > i) {
            return str;
        } else {
            StringBuffer sb = new StringBuffer(str);
            while (sb.length() < i) {
                sb.append(" ");
            }
            return sb.toString();
        }
    }

    static String pbytes(byte[] ba) {
        if (ba == null || ba.length == 0) {
            return "";
        } else {
            StringBuffer sb = new StringBuffer();
            for (Byte b1 : ba) {
                sb.append(b1);
            }
            return sb.toString();
        }
    }
}
