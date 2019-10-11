package com.bill.demo.j8;

import org.junit.Test;

public class HashTest {

    /**
     * copy java string's hashcode, and change int to long for a bigger cycle
     * Integer.MAX_VALUE: 2147483647
     * Integer.MIN_VALUE: -2147483648
     * Long.MAX_VALUE: 9223372036854775807
     * Long.MIN_VALUE: -9223372036854775808
     *
     * @param str
     * @return
     */
    public static long hashToLong(String str) {
        long h = 0;
        int off = 0;
        long len = str.length();
        for (int i = 0; i < len; i++) {
            h = 31 * h + str.charAt(off++);
        }
        return h;
    }

    /**
     *
     * @param str
     * @param rangeMax
     * @param rangeMin
     * @return
     */
    public static long hashProjectionByRange(String str, long rangeMax, long rangeMin) {
        return hashToLong(str) % (rangeMax-rangeMin) + rangeMin;
    }

    private String buildString(long times) {
        long count = 0l;
        StringBuffer sb = new StringBuffer();
        while (count++ < times) {
            sb.append("789xyz=\"{[");
        }
        return sb.toString();
    }

    @Test
    public void testHash() {
        String str = buildString(1000);
        System.out.println("==>" + hashToLong(str));
        System.out.println("==>" + hashProjectionByRange(str, 1000000000000001l, 0L));
    }

}
