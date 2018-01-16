package com.b.annotation;

@MyAnnotation1("this is annotation1")
public class AnnotationDemo {
    @MyAnnotation2(description = "this is annotation2", isAnnotation = true)
    public void sayHello() {
        System.out.println("hello world!");
    }
}
