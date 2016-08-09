package com.bill.demo.j8;

import com.bill.demo.j8.lambdas.People;
import com.bill.demo.j8.lambdas.Tool;
import org.junit.Test;

/**
 * Created by WANGBIL on 7/25/2016.
 */
public class LambdasTest {
    @Test
    public void test(){
        Tool toolImpl = () -> System.out.println("todo");

        People<Tool> peopleImpl = toolParameter -> {
            toolParameter.todo();
            System.out.println("use");
        };

        peopleImpl.use(toolImpl);


//        //Error:(20, 45) java: incompatible types: com.bill.demo.j8.lambdas.AbstractTool is not a functional interface
//        AbstractTool<Integer> absToolImpl = (Integer millisecParameter) -> {
//            todo();
//        };
//
//        peopleImpl.use(absToolImpl);
        System.out.println("====================================================");
        People<Tool> peopleImpl2 = toolParameter -> {
            toolParameter.todo();
            System.out.println("use" + (value++));
            System.out.println("use" + getValue());
            System.out.println("use" + VALUE);
            todo();
        };
        peopleImpl2.use(toolImpl);
    }

    private int value;
    public int getValue(){
        return value;
    }
    public final static int VALUE = 999;
    public static void todo(){
        System.out.println("static use");
    }
}
