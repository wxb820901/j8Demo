package com.bill.demo.j8.mutiThread.semaphore;



import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DoSemaphoreTestTasks {

    public static Map<String, Integer> tasks = new HashMap();
    static{
        tasks.put("bill1",new Integer(0));
        tasks.put("bill2",new Integer(0));
        tasks.put("bill3",new Integer(0));
        tasks.put("bill4",new Integer(0));
        tasks.put("bill0",new Integer(0));
    }
    public static final Semaphore semaphore = new Semaphore(5);//limit 5 threads access above map at same time


    public void doTestTask(int keyInt) {
        System.out.println("doTestTask");


            try {
                semaphore.acquire();
                String key = "bill"+keyInt;
                tasks.put(key, tasks.get(key).intValue()+1);
                semaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


    }


}
