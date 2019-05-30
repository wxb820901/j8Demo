package com.bill.demo.j8.mutiThread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class DoTestTaskThreadPool {

    public static void main(String[] args){

//
//
//
//        final ExecutorService threadPool = Executors.newCachedThreadPool();//signleThreadPool will pending on first thread clean
//        threadPool.execute(() -> {
//            new DoTestTasks().cleanTaskList();
//        });
//        for(int i = 0; i<100; i++){
//            threadPool.execute(() -> {
//                new DoTestTasks().doTestTask(new TestTask());
//            });
//            if(i%5==0){
//                try {
//                    Thread.sleep(1000);//if not gap for add thread, the clear thread will not take the lock
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        while(true){
//            try {
//                if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) break;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }





//        Thread b = new Thread() {
//            public void run() {
//                new DoTestTasks().cleanTaskList();
//            }
//        };
//        b.start();
//        for(int i = 0; i<100; i++) {
//            if(i%5==0){
//                try {
//                    Thread.sleep(1000);//if not gap for add thread, the clear thread will not take the lock
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            Thread a = new Thread() {
//                public void run() {
//                    new DoTestTasks().doTestTask(new TestTask());
//                }
//            };
//            a.start();
//        }
    }
}
