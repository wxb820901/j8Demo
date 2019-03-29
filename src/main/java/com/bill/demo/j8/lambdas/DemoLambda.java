package com.bill.demo.j8.lambdas;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class DemoLambda {
    public static void main(String args[]){
        List features = Arrays.asList("Lambdas", "Default Method", "Stream API", "Date and Time API", "Java");
        features.forEach(n -> System.out.println(n));
        features.forEach(System.out::println);

        Predicate<String> startsWithJ = (n) -> n.startsWith("J");
        Predicate<String> fourLetterLong = (n) -> n.length() == 4;
        features.stream()
                .filter(startsWithJ.and(fourLetterLong))
                .forEach((n) -> System.out.println("nName, which starts with 'J' and four letter long is : " + n));

        List costBeforeTax = Arrays.asList(100, 200, 300, 400, 500);
        costBeforeTax.stream().map((cost) ->  (1.12 *  Integer.parseInt(cost.toString()))).forEach(System.out::println);
    }
}
