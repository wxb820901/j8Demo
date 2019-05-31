package com.bill.demo.j8;


import com.bill.demo.j8.mutiThread.semaphore.DoSemaphoreTestTasks;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MultiThreadSemaphoreTest {
    @Test
    public void testThreadPool(){
        final ExecutorService threadPool = Executors.newCachedThreadPool();//signleThreadPool will pending on first thread clean

        for(int i = 0; i<100; i++){
            final int keyInt = i;
            threadPool.execute(() -> {
                new DoSemaphoreTestTasks().doTestTask(keyInt%5);
            });

        }

        while(true){
            try {
                if (!threadPool.awaitTermination(2, TimeUnit.SECONDS)) break;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        System.out.println(DoSemaphoreTestTasks.tasks);
        //check the "now is x" 100 times and "tasks clear" 20 times
    }
}
