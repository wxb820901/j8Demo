package com.bill.demo.j8.mutiThread;


import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class DoTestTasks {
    public static final ReentrantLock lock = new ReentrantLock(true);
    public static final Condition condiA = lock.newCondition();
    public static int index = 0;
    public final int MAX_TASK_NUMBER = 5;

    public void doTestTask() {
        System.out.println("doTestTask");
        try {
            if (lock.tryLock(2, TimeUnit.SECONDS)) {
                if (index >= MAX_TASK_NUMBER) {
                    condiA.await();
                }
                System.out.println("now is " + index++);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();

        }


    }

    public void cleanTaskList() {
        System.out.println("cleanTaskList");
        for (; ; ) {
            try {
                if (lock.tryLock(1, TimeUnit.SECONDS)) {
                    if (index == MAX_TASK_NUMBER) {
                        index = 0;
                        condiA.signalAll();
                        System.out.println("tasks clear  ");
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }
}
