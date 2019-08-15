package com.rules;

import com.bill.rules.odata.customization.RuleEngine;
import com.bill.rules.odata.customization.util.JsonUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class RuleEngineFilterOperatorTest {
    private static final Logger logger = LogManager.getLogger(RuleEngineFilterOperatorTest.class);
    public static final String inputJson =
            "{\n" +
                    "    \"table1\":[\n" +
                    "         {\n" +
                    "             \"label1\":\"2\",\n" +
                    "             \"label2\":\"prefix1[0].label2.value\",\n" +
                    "             \"label3\":\"asas\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "             \"label1\":\"1\",\n" +
                    "             \"label2\":\"prefix1[1].label2.value\",\n" +
                    "             \"label3\":\"fgfg\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "             \"label1\":\"3\",\n" +
                    "             \"label2\":\"cccc\",\n" +
                    "             \"label3\":\"fgfg\"\n" +
                    "         },\n" +
                    "         {\n" +
                    "             \"label1\":\"4\",\n" +
                    "             \"label2\":\"ccca\",\n" +
                    "             \"label3\":\"fgfg\"\n" +
                    "         }\n" +
                    "    ],\n" +
                    "    \"table2\":[\n" +
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
    public void testFilterEq() {
        logger.info("testFilterEq");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?" +
                "filter=label1 eq 1&count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertTrue(result.contains("\"label1\":\"1\""));
        Assert.assertFalse(result.contains("\"label1\":\"2\""));
        Assert.assertFalse(result.contains("\"label1\":\"3\""));
        Assert.assertFalse(result.contains("\"label1\":\"4\""));
    }

    @Test
    public void testFilterGe() {
        logger.info("testFilterGe");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?" +
                "filter=label1 ge 1&count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertTrue(result.contains("\"label1\":\"1\""));
        Assert.assertTrue(result.contains("\"label1\":\"2\""));
        Assert.assertTrue(result.contains("\"label1\":\"3\""));
        Assert.assertTrue(result.contains("\"label1\":\"4\""));
    }

    @Test
    public void testFilterGt() {
        logger.info("testFilterGt");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?" +
                "filter=label1 gt 1&count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertFalse(result.contains("\"label1\":\"1\""));
        Assert.assertTrue(result.contains("\"label1\":\"2\""));
        Assert.assertTrue(result.contains("\"label1\":\"3\""));
        Assert.assertTrue(result.contains("\"label1\":\"4\""));
    }

    @Test
    public void testFilterLe() {
        logger.info("testFilterLe");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?" +
                "filter=label1 le 2&count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertTrue(result.contains("\"label1\":\"1\""));
        Assert.assertTrue(result.contains("\"label1\":\"2\""));
        Assert.assertFalse(result.contains("\"label1\":\"3\""));
        Assert.assertFalse(result.contains("\"label1\":\"4\""));
    }

    @Test
    public void testFilterlt() {
        logger.info("testFilterlt");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?" +
                "filter=label1 lt 2&count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertTrue(result.contains("\"label1\":\"1\""));
        Assert.assertFalse(result.contains("\"label1\":\"2\""));
        Assert.assertFalse(result.contains("\"label1\":\"3\""));
        Assert.assertFalse(result.contains("\"label1\":\"4\""));
    }

    @Test
    public void testFilterContains() {
        logger.info("testFilterContains");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?" +
                "filter=label3 contains gf&count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertTrue(result.contains("\"label1\":\"1\""));
        Assert.assertFalse(result.contains("\"label1\":\"2\""));
        Assert.assertTrue(result.contains("\"label1\":\"3\""));
        Assert.assertTrue(result.contains("\"label1\":\"4\""));
    }

    @Test
    public void testFilterStartWith() {
        logger.info("testFilterStartWith");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?" +
                "filter=label3 startswith as&count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertFalse(result.contains("\"label1\":\"1\""));
        Assert.assertTrue(result.contains("\"label1\":\"2\""));
        Assert.assertFalse(result.contains("\"label1\":\"3\""));
        Assert.assertFalse(result.contains("\"label1\":\"4\""));
    }

    @Test
    public void testFilterEndWith() {
        logger.info("testFilterEndWith");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?" +
                "filter=label3 endswith fg&count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertTrue(result.contains("\"label1\":\"1\""));
        Assert.assertFalse(result.contains("\"label1\":\"2\""));
        Assert.assertTrue(result.contains("\"label1\":\"3\""));
        Assert.assertTrue(result.contains("\"label1\":\"4\""));
    }

    @Test
    public void testFilterOrFilter() {
        logger.info("testFilterOrFilter");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?" +
                "filter=label1 eq 1 or label1 eq 1&count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertTrue(result.contains("\"label1\":\"1\""));
        Assert.assertFalse(result.contains("\"label1\":\"2\""));
        Assert.assertFalse(result.contains("\"label1\":\"3\""));
        Assert.assertFalse(result.contains("\"label1\":\"4\""));
    }

}
