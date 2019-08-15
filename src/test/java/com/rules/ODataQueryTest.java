package com.rules;

import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

import static com.bill.rules.odata.customization.ODataQuery.*;
import static com.bill.rules.odata.customization.ODataQueryExpression.*;


public class ODataQueryTest {
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
    public void testGetQuery() {
        Assert.assertEquals(COUNT, getQuery("count=expression"));
        Assert.assertEquals(FILTER, getQuery("filter=expression"));
        Assert.assertEquals(ORDERBY, getQuery("orderby=expression"));
        Assert.assertEquals(SELECT, getQuery("select=expression"));
        Assert.assertEquals(SKIP, getQuery("skip=expression"));
        Assert.assertEquals(TOP, getQuery("top=expression"));
    }

    @Test
    public void testApply() {
        String result = ORDERBY.apply(ORDER_BY_EXPRESSION, inputJson, "table1", Arrays.asList("table1[*].label1 desc"));
        Assert.assertTrue(result.indexOf("\"label1\":\"1\"") > result.indexOf("\"label1\":\"2\""));
        Assert.assertTrue(result.indexOf("\"label1\":\"2\"") > result.indexOf("\"label1\":\"3\""));
        Assert.assertTrue(result.indexOf("\"label1\":\"3\"") > result.indexOf("\"label1\":\"4\""));

        Assert.assertTrue(COUNT.apply(COUNT_EXPRESSION, inputJson, "table1", Arrays.asList("")).contains("\"COUNT\":\"4\""));

        Assert.assertFalse(SKIP.apply(SKIP_EXPRESSION, inputJson, "table1", Arrays.asList("1")).contains("\"label1\":\"2\""));
        Assert.assertTrue(SKIP.apply(SKIP_EXPRESSION, inputJson, "table1", Arrays.asList("1")).contains("\"label1\":\"1\""));

        Assert.assertFalse(TOP.apply(TOP_EXPRESSION, inputJson, "table1", Arrays.asList("1")).contains("\"label1\":\"1\""));
        Assert.assertTrue(TOP.apply(TOP_EXPRESSION, inputJson, "table1", Arrays.asList("1")).contains("\"label1\":\"2\""));

        Assert.assertFalse(SELECT.apply(SELECT_EXPRESSION, inputJson, "table1", Arrays.asList("table1[*].label2, table1[*].label3")).contains("\"label1\""));
        Assert.assertTrue(SELECT.apply(SELECT_EXPRESSION, inputJson, "table1", Arrays.asList("table1[*].label2, table1[*].label3")).contains("\"label2\""));

        Assert.assertFalse(FILTER.apply(FILTER_EXPRESSION, inputJson, "table1", Arrays.asList("label1 eq 1")).contains("\"label1\":\"2\""));
        Assert.assertTrue(FILTER.apply(FILTER_EXPRESSION, inputJson, "table1", Arrays.asList("label1 eq 1")).contains("\"label1\":\"1\""));

    }
}
