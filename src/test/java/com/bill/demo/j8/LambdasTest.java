package com.bill.demo.j8;


import org.junit.Test;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Created by WANGBIL on 7/25/2016.
 */
public class LambdasTest {

    interface People<T> {
        void use(T tool);//default method is not function method
        default void print(){
            System.out.println("I am a vehicle!");
        }
    }
    interface Tool {
        void todo();
    }
    abstract class AbstractTool<T> implements Tool{
        @Override
        public void todo() {
            System.out.println("todo");
        }
        public abstract void hook(T millsec);
        //default abstract void testDefault(){} //==>error
        //default only used by interface
    }

    @Test
    public void testLambda(){
        Tool toolImpl = () -> System.out.println("todo");
        People<Tool> peopleImpl = toolParameter -> {
            toolParameter.todo();
            System.out.println("use");
        };
        peopleImpl.use(toolImpl);
        System.out.println("====================================================");
//        //lambda type must be interface
//        AbstractTool<Integer> absToolImpl = (Integer millisecParameter) -> {
//                System.out.println(millisecParameter);
//        };
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

    @Test
    public void testOptional(){
        Integer value1 = null;
        Integer value2 = new Integer(10);
        Optional<Integer> a = Optional.ofNullable(value1);
        Optional<Integer> b = Optional.of(value2);
        System.out.println(Integer.sum(a.orElse(1), b.get()));

    }

    @Test
    public void testObjects(){
        Integer value1 = null;
        Objects.requireNonNull(value1);
        System.out.println(Objects.nonNull(value1));
        System.out.println(Objects.isNull(value1));
    }

    interface SupplierTestInterface{
        void setName(String name);
        String getName();
    }

    class SupplierTestImpl implements SupplierTestInterface{
        private String name;

        public SupplierTestImpl(){

        }

        public SupplierTestImpl(String name){
            this.name = name;
        }
        public void setName(String name){
            this.name = name;
        }
        public String getName(){
            return this.name;
        }
    }

    @Test
    public void testSupplier(){
        //Supplier abstract a result of a function
        Supplier<SupplierTestInterface> supp = SupplierTestImpl::new;
        supp.get().setName("name121212");
        System.out.println(supp.get().getName());//==> null

        Supplier<SupplierTestInterface> supp2 = () -> new SupplierTestImpl("name1212");
        System.out.println(supp2.get().getName()); //==> name1212
    }

    @Test
    public void testFunction(){
        //简单的,只有一行 接受一个入参,最后要有一个返回值,
        Function<Integer, Boolean> function1 = (x) -> x>5;

        //标准的,有花括号, return, 分号.
        Function<Boolean, String> function2 = (x) -> {
            return "after function1: " + x;
        };
        System.out.println(function1.apply(6));
        System.out.println(function1.andThen(function2).apply(5));
    }

    @Test
    public void testConsumer(){
        //接口方法是入参类型为T, 无返回值的方法
        Consumer<String> consumer1 = (x) -> System.out.print(x);
        Consumer<String> consumer2 = (x) -> {
            System.out.println(" after consumer 1");
        };
        consumer1.andThen(consumer2).accept("test consumer1");
    }

    @Test
    public void testPredicate(){
        //接口方法的入参类型是T, 返回值是一个布尔值
        Predicate<String> predicate = (x) -> x.length() > 0;
        System.out.println(predicate.test("String"));
    }
}
