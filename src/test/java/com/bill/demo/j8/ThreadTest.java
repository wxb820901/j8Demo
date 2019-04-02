package com.bill.demo.j8;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ThreadTest {


    Thread th1 = new Thread() {
        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ":" + Thread.currentThread().getState());

            }
        }
    };

    Thread th2 = new Thread() {
        @Override
        public void run() {
            while(true){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ":" + Thread.currentThread().getState());

            }
        }
    };
    @Before
    public void before(){

    }
    @Test
    public void testThread(){
        th1.start();
        th2.start();
    }
}
