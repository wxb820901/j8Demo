package com.bill.demo.j8;

import org.junit.Test;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Created by WANGBIL on 7/25/2016.
 */
public class StreamTest {
    @Test
    public void testStreams(){
        //sequential streams
        List<String> strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        System.out.println(filtered);
        //forEach
        System.out.println("====================================================");
        Random random = new Random();
        random.ints().limit(10).forEach(System.out::println);
        //map
        System.out.println("====================================================");
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        List<Integer> squaresList = numbers.stream().map( i -> i*i).distinct().collect(Collectors.toList());
        System.out.println(squaresList);
        //filter
        System.out.println("====================================================");
        Long count = strings.stream().filter(string -> string.isEmpty()).count();
        System.out.println(strings+" has " + count + " empty elements. ");
        //limit
        System.out.println("====================================================");
        System.out.println(strings+"'s first 2 element is " + strings.stream().limit(2).collect(Collectors.toList()));
        //sort
        System.out.println("====================================================");
        random.ints().limit(10).sorted().forEach(System.out::println);
        //parallelStream
        System.out.println("====================================================");
        count = strings.parallelStream().filter(string -> string.isEmpty()).count();
        System.out.println(strings+" has " + count + " empty elements. ");
    }

    @Test
    public void testCollectors(){
        List<String>strings = Arrays.asList("abc", "", "bc", "efg", "abcd","", "jkl");
        List<String> filtered = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.toList());
        //Collectors.joining(", ")
        System.out.println("Filtered List: " + filtered);
        String mergedString = strings.stream().filter(string -> !string.isEmpty()).collect(Collectors.joining(", "));
        System.out.println("Merged String: " + mergedString);
    }
    @Test
    public void testStatics(){
        List<Integer> numbers = Arrays.asList(3, 2, 2, 3, 7, 3, 5);
        IntSummaryStatistics stats = numbers.stream().mapToInt((x) -> x).summaryStatistics();
        System.out.println("stats : " + stats);
        System.out.println("Highest number in List : " + stats.getMax());
        System.out.println("Lowest number in List : " + stats.getMin());
        System.out.println("Sum of all numbers : " + stats.getSum());
        System.out.println("Average of all numbers : " + stats.getAverage());
    }

}
