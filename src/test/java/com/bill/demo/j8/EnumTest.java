package com.bill.demo.j8;

import org.junit.Test;

import static com.bill.demo.j8.EnumTest.testEnum.key1;

public class EnumTest {

    @Test
    public void test(){
        System.out.println(key1);
        System.out.println(key1.label);
//        System.out.println(testEnum.valueOf(null));
        System.out.println(testEnum.valueOf("xxx"));
    }

    enum testEnum{
        key1("value1"),
        key2("value2");

        private String label;

        testEnum (String label){
            this.label = label;
        }

    }
}
