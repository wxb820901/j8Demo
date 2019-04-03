package com.bill.demo.j8;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

//start current case and then run jconsole, check different heap area changing
public class JConsoleTest {

    static class OOMObject {
        public byte[] placeholder = new byte[64 * 1024];
    }

    public static void fillHeap(int num) throws InterruptedException {
        Thread.sleep(20000);
        List<OOMObject> list = new ArrayList<OOMObject>();
        for (int i = 0; i < num; i++) {
            Thread.sleep(50);
            list.add(new OOMObject());
        }
        System.gc();
    }

    @Test
    public void testJConsole() throws InterruptedException {
        fillHeap(1000);
        while(true){}
    }
}
