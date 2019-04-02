package com.bill.demo.thread;

public class Calculator extends Thread {
    @Override
    public void run() {
        while(true){
            waitFor(1);
            System.out.println(Thread.currentThread().getName()+":"+Thread.currentThread().getState());
        }
    }
    public void waitFor(long sec){
        try {
            Thread.sleep(sec*1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
