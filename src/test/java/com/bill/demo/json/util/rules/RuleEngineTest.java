package com.bill.demo.json.util.rules;

import com.bill.demo.json.util.JsonUtil;
import org.junit.Test;

import static com.jayway.jsonpath.internal.JsonFormatter.prettyPrint;

public class RuleEngineTest {


    public static final String inputJson =
            "{\n" +
                    "    \"prefix1\":[\n" +
                    "         {\n" +
                    "             \"label1\":\"2\",\n" +
                    "             \"label2\":\"prefix1[0].label2.value\",\n" +
                    "             \"label3\":\"asas\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "             \"label1\":\"1\",\n" +
                    "             \"label2\":\"prefix1[1].label2.value\",\n" +
                    "             \"label3\":\"fgfg\"\n" +
                    "         }\n" +
                    "    ],\n" +
                    "    \"prefix2\":[\n" +
                    "          {\n" +
                    "              \"label1\":\"asas\",\n" +
                    "              \"label2\":\"dfdf\",\n" +
                    "              \"label3\":\"fgfg\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"label1\":\"sdsdsdsd\",\n" +
                    "              \"label2\":\"dfdf\",\n" +
                    "              \"label3\":\"fgfg\"\n" +
                    "          }\n" +
                    "    ]\n" +
                    "}";

//    @Test
//    public void testFilter() throws Exception {
//        RuleEngine engine = new RuleEngine();
//        String queryString = "prefix1?$filter=$.data[?(@.label1=='sdsd')]";
//        String result = engine.action(inputJson, queryString);
//        JsonUtil.prettyPrint(result);
//    }

    @Test
    public void testSelect() throws Exception {
        RuleEngine engine = new RuleEngine();
        String queryString = "prefix1?$select=label2, label3";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

//    @Test
//    public void testFilterAndSelect() throws Exception {
//        RuleEngine engine = new RuleEngine();
//        String queryString = "prefix2?$filter=$.data[?(@.label1=='sdsdsdsd')]&$select=$.data[*]['label1','label2']";
//        String result = engine.action(inputJson, queryString);
//        JsonUtil.prettyPrint(result);
//    }

    @Test
    public void testCount() throws Exception {
        RuleEngine engine = new RuleEngine();
        String queryString = "prefix1?$count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testOrderBy() throws Exception {
        RuleEngine engine = new RuleEngine();
        String queryString = "prefix1?$orderBy=label1";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testOrderByAndTop() throws Exception {
        RuleEngine engine = new RuleEngine();
        String queryString = "prefix1?$orderBy=label1&$top=1";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testOrderByAndSkip() throws Exception {
        RuleEngine engine = new RuleEngine();
        String queryString = "prefix1?$orderBy=label1&$skip=1";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

}
