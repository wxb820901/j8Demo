package com.bill.demo.thread;

public class ThreadTest {

    public static void main(String args[]){
        Calculator c1 = new Calculator();
        Calculator c2 = new Calculator();
        c1.start();
        c2.start();
        c2.waitFor(100000);
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        c2.notify();
    }
}
