package com.bill.json.rules;

import com.bill.json.rules.customization.RuleEngine;
import com.bill.json.rules.customization.util.JsonUtil;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Test;

public class RuleEngineTest {
    private static final Logger logger = LogManager.getLogger(RuleEngineTest.class);

    public static final String inputJson =
            "{\n" +
                    "    \"ceg\":[\n" +
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
                    "    \"rs\":[\n" +
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

    @Test
    public void testFilterEq() throws Exception {
        logger.info("testFilterEq");
        RuleEngine engine = new RuleEngine();
        String queryString = "ceg?$filter=label1 eq 1&$count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testFilterGe() throws Exception {
        logger.info("testFilterGe");
        RuleEngine engine = new RuleEngine();
        String queryString = "ceg?$filter=label1 ge 1&$count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testFilterGt() throws Exception {
        logger.info("testFilterGt");
        RuleEngine engine = new RuleEngine();
        String queryString = "ceg?$filter=label1 gt 1&$count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testFilterLe() throws Exception {
        logger.info("testFilterLe");
        RuleEngine engine = new RuleEngine();
        String queryString = "ceg?$filter=label1 le 2&$count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testFilterlt() throws Exception {
        logger.info("testFilterlt");
        RuleEngine engine = new RuleEngine();
        String queryString = "ceg?$filter=label1 lt 2&$count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testFilterContains() throws Exception {
        logger.info("testFilterContains");
        RuleEngine engine = new RuleEngine();
        String queryString = "ceg?$filter=label3 contains gf&$count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testFilterStartWith() throws Exception {
        logger.info("testFilterStartWith");
        RuleEngine engine = new RuleEngine();
        String queryString = "ceg?$filter=label3 startWith as&$count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testFilterEndWith() throws Exception {
        logger.info("testFilterEndWith");
        RuleEngine engine = new RuleEngine();
        String queryString = "ceg?$filter=label3 endWith fg&$count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testFilterisExistTrue() throws Exception {
        logger.info("testFilterisExistTrue");
        RuleEngine engine = new RuleEngine();
        String queryString = "ceg?$filter=isExist label1,label2&$count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }
    @Test
    public void testFilterisExistFalse() throws Exception {
        logger.info("testFilterisExistFalse");
        RuleEngine engine = new RuleEngine();
        String queryString = "ceg?$filter=isExist label4&$count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testSelect() throws Exception {
        logger.info("testSelect");
        RuleEngine engine = new RuleEngine();
        String queryString = "ceg?$select=label2, label3&$count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testCount() throws Exception {
        logger.info("testCount");
        RuleEngine engine = new RuleEngine();
        String queryString = "ceg?$count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testOrderBy() throws Exception {
        logger.info("testOrderBy");
        RuleEngine engine = new RuleEngine();
        String queryString = "ceg?$orderBy=label1";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testOrderByAndTop() throws Exception {
        logger.info("testOrderByAndTop");
        RuleEngine engine = new RuleEngine();
        String queryString = "ceg?$orderBy=label1&$top=1&$count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

    @Test
    public void testOrderByAndSkip() throws Exception {
        logger.info("testOrderByAndSkip");
        RuleEngine engine = new RuleEngine();
        String queryString = "ceg?$orderBy=label1&$skip=1&$count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);
    }

}
