package com.bill.demo.j8.lambdas;

/**
 * Created by WANGBIL on 7/25/2016.
 */
public abstract class AbstractTool<T> implements Tool{

    @Override
    public void todo() {
        System.out.println("todo");
    }

    public abstract void hook(T millsec);

//    default abstract void testDefault(){} //==>error
    //default only used by interface


}
