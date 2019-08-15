package com.rules;

import com.bill.rules.odata.customization.RuleEngine;
import com.bill.rules.odata.customization.util.JsonUtil;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

public class RuleEngineJoinTest {

    private static final Logger logger = LogManager.getLogger(RuleEngineJoinTest.class);
    public static final String inputJson =
            "{\n" +
                    "    \"quote\":[\n" +
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
                    "    \"org\":[\n" +
                    "          {\n" +
                    "              \"label1\":\"1\",\n" +
                    "              \"label2\":\"dfdf1\",\n" +
                    "              \"label3\":\"fgfg1\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"label1\":\"2\",\n" +
                    "              \"label2\":\"dfdf2\",\n" +
                    "              \"label3\":\"fgfg2\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"label1\":\"3\",\n" +
                    "              \"label2\":\"dfdf3\",\n" +
                    "              \"label3\":\"fgfg3\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"label1\":\"4\",\n" +
                    "              \"label2\":\"dfdf4\",\n" +
                    "              \"label3\":\"fgfg4\"\n" +
                    "          }\n" +
                    "    ],\n" +
                    "    \"instrument\":[\n" +
                    "          {\n" +
                    "              \"label1\":\"1\",\n" +
                    "              \"label2\":\"dfdf1\",\n" +
                    "              \"label3\":\"fgfg1\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"label1\":\"2\",\n" +
                    "              \"label2\":\"dfdf2\",\n" +
                    "              \"label3\":\"fgfg2\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"label1\":\"3\",\n" +
                    "              \"label2\":\"dfdf3\",\n" +
                    "              \"label3\":\"fgfg3\"\n" +
                    "          },\n" +
                    "          {\n" +
                    "              \"label1\":\"4\",\n" +
                    "              \"label2\":\"dfdf4\",\n" +
                    "              \"label3\":\"fgfg4\"\n" +
                    "          }\n" +
                    "    ]\n" +
                    "}";

    @Test
    public void testJoin() {
        logger.info("testFilterEq");
        RuleEngine engine = new RuleEngine();
        String queryString = "quote?" +
                "join=org on org[*].label1 = quote[*].label1" +
                "&orderby=quote[*].label1" +
                "&count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        int index1 = result.indexOf("\"label1\":\"1\"");
        int index2 = result.indexOf("\"label1\":\"1\"", index1);
        Assert.assertTrue(index1 > -1 && index2 > -1);
        index1 = result.indexOf("\"label1\":\"2\"");
        index2 = result.indexOf("\"label1\":\"2\"", index1);
        Assert.assertTrue(index1 > -1 && index2 > -1);
        index1 = result.indexOf("\"label1\":\"3\"");
        index2 = result.indexOf("\"label1\":\"3\"", index1);
        Assert.assertTrue(index1 > -1 && index2 > -1);
        index1 = result.indexOf("\"label1\":\"4\"");
        index2 = result.indexOf("\"label1\":\"4\"", index1);
        Assert.assertTrue(index1 > -1 && index2 > -1);
    }

    @Test
    public void testSelfJoin() {
        logger.info("testFilterEq");
        RuleEngine engine = new RuleEngine();
        String queryString = "quote?" +
                "join=quote on quote[*].label1 = quote[*].label1" +
                "&orderby=quote[*].label1" +
                "&count";
        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);


        int index1 = result.indexOf("\"label1\":\"1\"");
        int index2 = result.indexOf("\"label1\":\"1\"", index1);
        Assert.assertTrue(index1 > -1 && index2 > -1);
        index1 = result.indexOf("\"label1\":\"2\"");
        index2 = result.indexOf("\"label1\":\"2\"", index1);
        Assert.assertTrue(index1 > -1 && index2 > -1);
        index1 = result.indexOf("\"label1\":\"3\"");
        index2 = result.indexOf("\"label1\":\"3\"", index1);
        Assert.assertTrue(index1 > -1 && index2 > -1);
        index1 = result.indexOf("\"label1\":\"4\"");
        index2 = result.indexOf("\"label1\":\"4\"", index1);
        Assert.assertTrue(index1 > -1 && index2 > -1);
    }

    @Test
    public void testJoinAndJoin() {
        logger.info("testFilterEq");
        RuleEngine engine = new RuleEngine();
        String queryString = "quote?" +
                "join=org on org[*].label1 = quote[*].label1" +
                "&join=instrument on instrument[*].label1 = quote[*].label1_org.org[*].label1" +
                "&orderby=quote[*].label1" +
                "&count";

        String result = engine.action(inputJson, queryString);
        JsonUtil.prettyPrint(result);

        int index1 = result.indexOf("\"label1\":\"1\"");
        int index2 = result.indexOf("\"label1\":\"1\"", index1);
        int index3 = result.indexOf("\"label1\":\"1\"", index2);
        Assert.assertTrue(index1 > -1 && index2 > -1 && index3 > -1);
        index1 = result.indexOf("\"label1\":\"2\"");
        index2 = result.indexOf("\"label1\":\"2\"", index1);
        index3 = result.indexOf("\"label1\":\"2\"", index2);
        Assert.assertTrue(index1 > -1 && index2 > -1 && index3 > -1);
        index1 = result.indexOf("\"label1\":\"3\"");
        index2 = result.indexOf("\"label1\":\"3\"", index1);
        index3 = result.indexOf("\"label1\":\"3\"", index2);
        Assert.assertTrue(index1 > -1 && index2 > -1 && index3 > -1);
        index1 = result.indexOf("\"label1\":\"4\"");
        index2 = result.indexOf("\"label1\":\"4\"", index1);
        index3 = result.indexOf("\"label1\":\"4\"", index2);
        Assert.assertTrue(index1 > -1 && index2 > -1 && index3 > -1);
    }
}
