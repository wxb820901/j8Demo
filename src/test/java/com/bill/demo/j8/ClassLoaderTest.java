package com.bill.demo.j8;

import org.junit.Test;

public class ClassLoaderTest {

    /*
    bootStrapClassLoader -> jre
    ExtClassLoader       -> ext
    AppClassLoader       -> classes
    DefineClassLoader    -> customized classloader
     */
    @Test
    public void testClassLoader() throws ClassNotFoundException {
        //C:\Program Files\Java\jre1.8.0_201\lib
        Class clazz = Class.forName("java.lang.Object");
        System.out.println(clazz.getClassLoader());//-> null -> bootStrapClassLoader
        clazz = Class.forName("sun.awt.HKSCS");
        System.out.println(clazz.getClassLoader());//-> null -> bootStrapClassLoader

        //C:\Program Files\Java\jre1.8.0_201\lib\ext
        clazz = Class.forName("sun.net.spi.nameservice.dns.DNSNameService");
        System.out.println(clazz.getClassLoader());//-> sun.misc.Launcher$ExtClassLoader

        //classpath
        clazz = Class.forName("org.junit.Test");
        System.out.println(clazz.getClassLoader());//-> sun.misc.Launcher$AppClassLoader
    }
}
