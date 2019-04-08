package com.bill.demo.j8;

import com.google.common.base.*;
import com.google.common.base.Objects;
import com.google.common.base.Optional;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.*;
import com.google.common.io.Files;
import com.google.common.io.Resources;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.collect.Maps.newHashMap;
import static com.google.common.collect.Sets.newHashSet;

public class GuavaTest {

    @Test
    public void tetsCollect(){
        // 普通Collection的创建
        List<String> list = Lists.newArrayList();
        Set<String> set = newHashSet();
        Map<String, String> map = newHashMap();
        // 不变Collection的创建
        ImmutableList<String> iList = ImmutableList.of("a", "b", "c");
        ImmutableSet<String> iSet = ImmutableSet.of("e1", "e2");
        ImmutableMap<String, String> iMap = ImmutableMap.of("k1", "v1", "k2", "v2");

        Multimap<String,Integer> mMap = ArrayListMultimap.create();
        mMap.put("aa", 1);
        mMap.put("aa", 2);
        System.out.println(mMap.get("aa"));  //[1, 2]

        Multiset<String> mSet = HashMultiset.create();
        mSet.add("bill");
        mSet.add("bill");
        System.out.println(mSet.count("bill"));  //2

        //双向Map(Bidirectional Map) 键与值都不能重复
        BiMap<String, String> biMap = HashBiMap.create();
        //双键的Map Map--> Table-->rowKey+columnKey+value  //和sql中的联合主键有点像
        Table<String, String, Integer> tables = HashBasedTable.create();


        //list转换为特定规则的字符串
        List<String> listOrigin = new ArrayList<String>();
        listOrigin.add("aa");
        listOrigin.add("bb");
        listOrigin.add("cc");
        String result = Joiner.on("-").join(listOrigin);
        System.out.println(result);
        //把map集合转换为特定规则的字符串
        Map<String, Integer> mapOrigin = newHashMap();
        mapOrigin.put("xiaoming", 12);
        mapOrigin.put("xiaohong",13);
        result = Joiner.on(",").withKeyValueSeparator("=").join(mapOrigin);
        System.out.println(result);
    }


    @Test
    public void testCollectFilter(){
        //按照条件过滤
        ImmutableList<String> names = ImmutableList.of("begin", "code", "Guava", "Java");
        Iterable<String> fitered = Iterables.filter(names, Predicates.or(Predicates.equalTo("Guava"), Predicates.equalTo("Java")));
        System.out.println(fitered); // [Guava, Java]


        //自定义过滤条件   使用自定义回调方法对Map的每个Value进行操作
        ImmutableMap<String, Integer> m = ImmutableMap.of("begin", 12, "code", 15);
        // Function<F, T> F表示apply()方法input的类型，T表示apply()方法返回类型
        Map<String, Integer> m2 = Maps.transformValues(m, new Function<Integer, Integer>() {
            public Integer apply(Integer input) {
                if(input>12){
                    return input;
                }else{
                    return input+1;
                }
            }
        });
        System.out.println(m2);   //{begin=13, code=15}
    }

    @Test
    public void testCollectOperation(){
        //set的交集, 并集, 差集
        HashSet<Integer> setA = newHashSet(1, 2, 3, 4, 5);
        HashSet<Integer> setB = newHashSet(4, 5, 6, 7, 8);

        Sets.SetView<Integer> union = Sets.union(setA, setB);
        System.out.println("union:");
        for (Integer integer : union)
            System.out.println(integer);        //union:12345867

        Sets.SetView<Integer> difference = Sets.difference(setA, setB);
        System.out.println("difference:");
        for (Integer integer : difference)
            System.out.println(integer);        //difference:123

        Sets.SetView<Integer> intersection = Sets.intersection(setA, setB);
        System.out.println("intersection:");
        for (Integer integer : intersection)
            System.out.println(integer);        //intersection:45

        System.out.println("-----------------------------------------------------");

        //map的交集，并集，差集
        HashMap<String, Integer> mapA = newHashMap();
        mapA.put("bill",1001);//change for entriesDiffering and entriesInCommon
        mapA.put("tom",100);
        HashMap<String, Integer> mapB = newHashMap();
        mapB.put("bill",100);
        mapB.put("jam",100);

        MapDifference differenceMap = Maps.difference(mapA, mapB);
        System.out.println(differenceMap.areEqual());
        Map entriesDiffering = differenceMap.entriesDiffering();
        System.out.println(entriesDiffering);
        Map entriesOnlyOnLeft = differenceMap.entriesOnlyOnLeft();
        System.out.println(entriesOnlyOnLeft);
        Map entriesOnlyOnRight = differenceMap.entriesOnlyOnRight();
        System.out.println(entriesOnlyOnRight);
        Map entriesInCommon = differenceMap.entriesInCommon();
        System.out.println(entriesInCommon);
    }

    class Person{
        private Integer age;
        private String name;
        public Person(){

        }
        public Person(String name, int age){
            this.name = name;
            this.age = age;
        }
    }
    /*
    * natural()	对可排序类型做自然排序，如数字按大小，日期按先后排序
    * usingToString()	按对象的字符串形式做字典排序[lexicographical ordering]
    * from(Comparator)	把给定的Comparator转化为排序器
    * reverse()	获取语义相反的排序器
    * nullsFirst()	使用当前排序器，但额外把null值排到最前面。
    * nullsLast()	使用当前排序器，但额外把null值排到最后面。
    * compound(Comparator)	合成另一个比较器，以处理当前排序器中的相等情况。
    * lexicographical()	基于处理类型T的排序器，返回该类型的可迭代对象Iterable<T>的排序器。
    * onResultOf(Function)	对集合中元素调用Function，再按返回值用当前排序器排序。
    * */
    @Test
    public void testOrdering(){
        Person person = new Person("aa",14);  //String name  ,Integer age
        Person ps = new Person("bb",13);
        Ordering<Person> byOrdering = Ordering.natural().nullsFirst().onResultOf(new Function<Person,String>(){
            public String apply(Person person){
                return person.age.toString();
            }
        });
        byOrdering.compare(person, ps);
        System.out.println(byOrdering.compare(person, ps)); //1      person的年龄比ps大 所以输出1
    }

    @Test
    public void testTimeUnit(){
        Stopwatch stopwatch = Stopwatch.createStarted();
        for(int i=0; i<100000; i++){

        }
        long nanos = stopwatch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println(nanos);
    }

    @Test
    public void testFile() throws IOException {
        String filePath = "D:/test";
        File file = new File(filePath);
        if(!file.exists()){
            Files.touch(file);
            Files.write(new String("122\nsdasd\n12356").getBytes(), file);
        }
        List<String> list =  Files.readLines(file, Charsets.UTF_8);
        System.out.println(list);

        File from = new File(filePath);
        File to = new File(filePath+"2");
        Files.copy(from,to);  //复制文件
//        Files.deleteDirectoryContents(File directory); //删除文件夹下的内容(包括文件与子文件夹)
//        Files.deleteRecursively(File file); //删除文件或者文件夹
//        Files.move(File from, File to); //移动文件
//        URL url = Resources.getResource("abc.xml"); //获取classpath根下的abc.xml文件url

        from.delete();
        to.delete();
    }
    @Test
    public void testString(){
        //将String转换为特定的集合
        String str = "1-2-3-4-5-6";
        List<String> list = Splitter.on("-").splitToList(str);
        System.out.println(list); //list为  [1, 2, 3, 4, 5, 6]
        //除空串与空格
        str = "1-2-3-4-  5-  6   ";
        list = Splitter.on("-").omitEmptyStrings().trimResults().splitToList(str);
        System.out.println(list);

        //String转换为map
        str = "xiaoming=11,xiaohong=23";
        Map<String,String> map = Splitter.on(",").withKeyValueSeparator("=").split(str);
        System.out.println(map);

        //字符切割，或者特定的正则分隔
        String input = "aa.dd,,ff,,.";
        List<String> result = Splitter.onPattern("[.|,]").omitEmptyStrings().splitToList(input);
        System.out.println(result);

    }
    @Test
    public void testCache() throws ExecutionException {
        LoadingCache<String,String> cahceBuilder = CacheBuilder
                .newBuilder()
                .build(new CacheLoader<String, String>(){
                    @Override
                    public String load(String key) throws Exception {
                        String strProValue="hello "+key+"!";
                        return strProValue;
                    }
                });
        System.out.println(cahceBuilder.apply("begincode"));  //hello begincode!
        System.out.println(cahceBuilder.get("begincode")); //hello begincode!
        System.out.println(cahceBuilder.get("wen")); //hello wen!
        System.out.println(cahceBuilder.apply("wen")); //hello wen!
        System.out.println(cahceBuilder.apply("da"));//hello da!
        cahceBuilder.put("begin", "code");
        System.out.println(cahceBuilder.get("begin")); //code



        Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(1000).build();
        String resultVal = cache.get("code", new Callable<String>() {
            public String call() {
                String strProValue="begin "+"code"+"!";
                return strProValue;
            }
        });
        System.out.println("value : " + resultVal); //value : begin code!
    }



    @Test
    public void testOptional(){
        Optional<Integer> possible = Optional.of(5);
        System.out.println(possible.isPresent()); // returns true
        System.out.println(possible.get()); // returns 5
        Optional<Integer> possible2 = Optional.fromNullable(null);
        System.out.println(possible2.isPresent()); // returns false
        System.out.println(possible2.orNull()); // returns null
        System.out.println(Optional.of(possible2).or(possible));// returns 5
    }

    @Test
    public void testPreconditions(){
        int i = 0, j = 1;
        checkArgument(i >= 0, "Argument was %s but expected nonnegative", i);//IllegalArgumentException
        checkArgument(i < j, "Expected i < j, but %s > %s", i, j);
    }

    @Test
    public void testObject(){
        System.out.println(Objects.equal("a", "a")); // returns true
        System.out.println(Objects.equal(null, "a")); // returns false
        System.out.println(Objects.equal("a", null)); // returns false
        System.out.println(Objects.equal(null, null)); // returns true
    }
}
