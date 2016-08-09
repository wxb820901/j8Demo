package com.bill.demo.j8.lambdas;

/**
 * Created by WANGBIL on 7/25/2016.
 */
public interface People<T> {
    void use(T tool);//default method is not function method
    default void print(){
        System.out.println("I am a vehicle!");
    }



}
