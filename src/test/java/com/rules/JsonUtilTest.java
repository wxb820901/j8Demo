package com.rules;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import static com.bill.rules.odata.customization.util.JsonUtil.*;


public class JsonUtilTest {
    private static final Logger logger = LogManager.getLogger(JsonUtilTest.class);

    private static final String json1 =
            "{\n" +
                    "        \"l1v\":\"1111\",\n" +
                    "        \"l1d\":{\n" +
                    "            \"l2v\":\"2222\",\n" +
                    "            \"l2d\":{\n" +
                    "                \"l3v\":\"aa3\"\n" +
                    "            },\n" +
                    "            \"l2a\":[\n" +
                    "                    {\"l3v1\":\"aaa4\"},\n" +
                    "                    {\"l3v2\":\"aaa4\"}\n" +
                    "                    ]\n" +
                    "        },\n" +
                    "        \"l1a\":[ \"22cv\",\"22cv\"]\n" +
                    "    }";

    @Test
    public void testGetSubJsonpathMapWithArrayIndex() {
        Map<String, String> jsonPathMap = convertJsonToMap(json1);
        jsonPathMap.entrySet().stream().forEach(logger::info);
        logger.info("============================================================================");
        getSubJsonPathMapWithArrayIndex(jsonPathMap, "$.l1d.l2a[*].l3v1").entrySet().stream()
                .forEach(item -> {
                    Assert.assertEquals("$.l1d.l2a[0].l3v1", item.getKey());
                    Assert.assertEquals("aaa4", item.getValue());

                });
        logger.info("============================================================================");
        getSubJsonPathMapWithArrayIndex(jsonPathMap, "$.l1d.l2a[*].l3v2").entrySet().stream()
                .forEach(item -> {
                    Assert.assertEquals("$.l1d.l2a[1].l3v2", item.getKey());
                    Assert.assertEquals("aaa4", item.getValue());

                });
        logger.info("============================================================================");
        getSubJsonPathMapWithArrayIndex(jsonPathMap, "$.l1d.l2a[*]").entrySet().stream()
                .forEach(item -> {
                    if (item.getKey().indexOf("l2a[0]") > -1) {
                        Assert.assertEquals("$.l1d.l2a[0].l3v1", item.getKey());
                    } else {
                        Assert.assertEquals("$.l1d.l2a[1].l3v2", item.getKey());
                    }
                    Assert.assertEquals("aaa4", item.getValue());
                });
        logger.info("============================================================================");
        getSubJsonPathMapWithArrayIndex(jsonPathMap, "$.l1a[*]").entrySet().stream()
                .forEach(item -> {
                    if (item.getKey().indexOf("l1a[1]") > -1) {
                        Assert.assertEquals("$.l1a[1]", item.getKey());
                    } else {
                        Assert.assertEquals("$.l1a[0]", item.getKey());
                    }
                    Assert.assertEquals("22cv", item.getValue());
                });
        ;
    }

    @Test
    public void testReplaceArrayIndexWithStar() {
        Assert.assertEquals("$.l1d.l2a[*].l3v1",
                replaceArrayIndexWithStar("$.l1d.l2a[*].l3v1", "$.l1d.l2a[1].l3v1"));
        Assert.assertEquals("$.l1d[1].l2a[*].l3v1",
                replaceArrayIndexWithStar("$.l1d[1].l2a[*].l3v1", "$.l1d[1].l2a[1].l3v1"));
        Assert.assertEquals("$.table1[*].label1.l1d[1].l2a[*].l3v1",
                replaceArrayIndexWithStar("$.table1[*].label1.l1d[1].l2a[*].l3v1", "$.table1[1].label1.l1d[1].l2a[18].l3v1"));
        Assert.assertEquals("$.table1[*].label1.l1d[*].l2a[1].l3v1",
                replaceArrayIndexWithStar("$.table1[*].label1.l1d[*].l2a[1].l3v1", "$.table1[2].label1.l1d[1].l2a[1].l3v1"));
        Assert.assertEquals("$.table1[1].label1.l1d[*].l2a[1].l3v1",
                replaceArrayIndexWithStar("$.table1[1].label1.l1d[*].l2a[1].l3v1", "$.table1[1].label1.l1d[1].l2a[1].l3v1"));
        Assert.assertEquals("$.table1[*].label1.l1d[*].l2a[1].l3v1",
                replaceArrayIndexWithStar("$.table1[*].label1.l1d[*]", "$.table1[2].label1.l1d[1].l2a[1].l3v1"));
        Assert.assertEquals("$.table1[*].label1.l1d[1].l2a[1].l3v1",
                replaceArrayIndexWithStar("$.table1[*]", "$.table1[2].label1.l1d[1].l2a[1].l3v1"));
    }

    @Test
    public void testSortedByTreeMap() {
        Map<String, String> treeMap = new TreeMap((Comparator<String>) (o1, o2) -> o2.compareTo(o1));

        treeMap.put("1", "2");
        treeMap.put("2", "1");
        treeMap.put("3", "3");
        treeMap.put("4", "4");
        logger.info(treeMap);
        Iterator iterator = treeMap.entrySet().iterator();
        Assert.assertEquals("4", ((Map.Entry) iterator.next()).getKey());
        Assert.assertEquals("3", ((Map.Entry) iterator.next()).getKey());
        Assert.assertEquals("2", ((Map.Entry) iterator.next()).getKey());
        Assert.assertEquals("1", ((Map.Entry) iterator.next()).getKey());

        Map<String, String> treeMapAsc = new TreeMap((Comparator<String>) (o1, o2) -> -(o2.compareTo(o1)));

        treeMapAsc.put("1", "2");
        treeMapAsc.put("2", "1");
        treeMapAsc.put("3", "3");
        treeMapAsc.put("4", "4");
        logger.info(treeMapAsc);
        iterator = treeMapAsc.entrySet().iterator();
        Assert.assertEquals("1", ((Map.Entry) iterator.next()).getKey());
        Assert.assertEquals("2", ((Map.Entry) iterator.next()).getKey());
        Assert.assertEquals("3", ((Map.Entry) iterator.next()).getKey());
        Assert.assertEquals("4", ((Map.Entry) iterator.next()).getKey());

    }

    @Test
    public void testLike() {
        Assert.assertTrue(like("abcde123", "*"));
        Assert.assertTrue(like("abcde123", "*a"));
        Assert.assertTrue(like("abcde123", "a*"));
        Assert.assertTrue(like("abcde123", "abcde*123"));
        Assert.assertFalse(like("abcde123", ""));
        Assert.assertFalse(like("abcde123", "1*a"));

    }
}

