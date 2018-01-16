package com.b.annotation;

import java.lang.reflect.Method;
public class TestAnnotation {  
     
        public static void main(String args[]) throws ClassNotFoundException, NoSuchMethodException, SecurityException{  
                Class<?> cls = Class.forName("com.b.annotation.AnnotationDemo");  
                boolean flag = cls.isAnnotationPresent(MyAnnotation1.class);  
                if(flag){  
                        System.out.println("????annotation");  
                        MyAnnotation1 annotation1 = cls.getAnnotation(MyAnnotation1.class);  
                        System.out.println(annotation1.value());  
                }  
                  
                Method method = cls.getMethod("sayHello");  
                flag = method.isAnnotationPresent(MyAnnotation2.class) ;  
                if(flag){  
                        System.out.println("??????annotation");  
                        MyAnnotation2 annotation2 = method.getAnnotation(MyAnnotation2.class);  
                        System.out.println(annotation2.description()+"/t"+annotation2.isAnnotation());  
                }  
        }  
          
}  
