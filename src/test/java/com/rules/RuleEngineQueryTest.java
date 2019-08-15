package com.rules;

import com.bill.rules.odata.customization.RuleEngine;
import com.bill.rules.odata.customization.util.JsonUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class RuleEngineQueryTest {
    private static final Logger logger = LogManager.getLogger(RuleEngineQueryTest.class);

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
    public void testSelect() {
        logger.info("testSelect");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?" +
                "select=table1[*].label2, table1[*].label3" +
                "&count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertFalse(result.contains("\"label1\""));
        Assert.assertTrue(result.contains("\"label2\""));
        Assert.assertTrue(result.contains("\"label3\""));

    }

    @Test
    public void testSelectWithSpecificIndex() {
        logger.info("testSelect");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?" +
                "select=table1[1].label2, table1[2].label3" +
                "&count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertFalse(result.contains("\"label1\""));
        Assert.assertTrue(result.contains("\"label2\""));
        Assert.assertTrue(result.contains("\"label3\""));

    }

    @Test
    public void testCount() {
        logger.info("testCount");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertTrue(result.contains("\"COUNT\":\"4\""));

    }

    @Test
    public void testOrderby() {
        logger.info("testorderby");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?" +
                "orderby=table1[*].label1 desc";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertTrue(result.contains("\"label1\":\"1\""));
        Assert.assertTrue(result.contains("\"label1\":\"2\""));
        Assert.assertTrue(result.contains("\"label1\":\"3\""));
        Assert.assertTrue(result.contains("\"label1\":\"4\""));

        Assert.assertTrue(result.indexOf("\"label1\":\"1\"") > result.indexOf("\"label1\":\"2\""));
        Assert.assertTrue(result.indexOf("\"label1\":\"2\"") > result.indexOf("\"label1\":\"3\""));
        Assert.assertTrue(result.indexOf("\"label1\":\"3\"") > result.indexOf("\"label1\":\"4\""));
    }

    @Test
    public void testOrderbyAndTop() {
        logger.info("testorderbyAndTop");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?" +
                "orderby=table1[*].label1" +
                "&top=1&count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertTrue(result.contains("\"label1\":\"1\""));
        Assert.assertFalse(result.contains("\"label1\":\"2\""));
        Assert.assertFalse(result.contains("\"label1\":\"3\""));
        Assert.assertFalse(result.contains("\"label1\":\"4\""));
    }

    @Test
    public void testOrderbyAndSkip() {
        logger.info("testorderbyAndSkip");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?" +
                "orderby=table1[*].label1" +
                "&skip=1" +
                "&count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertFalse(result.contains("\"label1\":\"1\""));
        Assert.assertTrue(result.contains("\"label1\":\"2\""));
        Assert.assertTrue(result.contains("\"label1\":\"3\""));
        Assert.assertTrue(result.contains("\"label1\":\"4\""));

        Assert.assertTrue(result.indexOf("\"label1\":\"1\"") < result.indexOf("\"label1\":\"2\""));
        Assert.assertTrue(result.indexOf("\"label1\":\"2\"") < result.indexOf("\"label1\":\"3\""));
        Assert.assertTrue(result.indexOf("\"label1\":\"3\"") < result.indexOf("\"label1\":\"4\""));
    }

    @Test
    public void testComplicatedQuery() {
        logger.info("testComplicatedQuery");
        RuleEngine engine = new RuleEngine();
        String queryString = "table1?" +
                "orderby=table1[*].label1" +
                "&skip=0" +
                "&top=4" +
                "&select=table1[*].label3, table1[*].label2, table1[*].label1" +
                "&filter=label1 eq 1 or label1 eq 2 or label1 eq 3 or label1 eq 4" +
                "&filter=label3 eq fgfg" +

                "&count";

        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        Assert.assertTrue(result.contains("\"label1\":\"1\""));
        Assert.assertFalse(result.contains("\"label1\":\"2\""));
        Assert.assertTrue(result.contains("\"label1\":\"3\""));
        Assert.assertTrue(result.contains("\"label1\":\"4\""));

    }

}
