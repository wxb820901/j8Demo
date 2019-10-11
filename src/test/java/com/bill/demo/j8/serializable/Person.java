package com.bill.demo.j8.serializable;

import java.io.Serializable;

public class Person implements Serializable{
    private static final long serialVersionUID = 1L;
    private static final String country = "ITALY";
    private int age;
    private String name;
    transient int height;

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
