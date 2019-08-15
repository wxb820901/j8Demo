package com.rules;

import com.bill.rules.odata.customization.FilterOperator;

import org.junit.Assert;
import org.junit.Test;

import static com.bill.rules.odata.customization.FilterOperator.*;


public class FilterOperatorTest {
    @Test
    public void testGetFilterOperator(){
        Assert.assertEquals(FilterOperator.OR, getFilterOperator("expression or expression "));
        Assert.assertEquals(FilterOperator.EQ, getFilterOperator("a eq b "));
        Assert.assertEquals(FilterOperator.GE, getFilterOperator("a ge b "));
        Assert.assertEquals(FilterOperator.GT, getFilterOperator("a gt b "));
        Assert.assertEquals(FilterOperator.LE, getFilterOperator("a le b "));
        Assert.assertEquals(FilterOperator.LT, getFilterOperator("a lt b "));
        Assert.assertEquals(FilterOperator.LIKES, getFilterOperator("a likes b "));
        Assert.assertEquals(FilterOperator.STARTSWITH, getFilterOperator("a startsWith b "));
        Assert.assertEquals(FilterOperator.ENDSWITH, getFilterOperator("a endswith b "));
        Assert.assertEquals(FilterOperator.CONTAINS, getFilterOperator("a contains b "));
    }

    @Test
    public void testGetItemBeforeOperator(){
        Assert.assertEquals("a", getItemBeforeOperator("a eq b "));
        Assert.assertEquals("a", getItemBeforeOperator("a ge b "));
        Assert.assertEquals("a", getItemBeforeOperator("a gt b "));
        Assert.assertEquals("a", getItemBeforeOperator("a le b "));
        Assert.assertEquals("a", getItemBeforeOperator("a lt b "));
        Assert.assertEquals("a", getItemBeforeOperator("a likes b "));
        Assert.assertEquals("a", getItemBeforeOperator("a startswith b "));
        Assert.assertEquals("a", getItemBeforeOperator("a endswith b "));
        Assert.assertEquals("a", getItemBeforeOperator("a contains b "));
    }

    @Test
    public void testGetItemAfterOperator(){
        Assert.assertEquals("b", getItemAfterOperator("a eq b"));
        Assert.assertEquals("b", getItemAfterOperator("a ge b"));
        Assert.assertEquals("b", getItemAfterOperator("a gt b"));
        Assert.assertEquals("b", getItemAfterOperator("a le b"));
        Assert.assertEquals("b", getItemAfterOperator("a lt b"));
        Assert.assertEquals("b", getItemAfterOperator("a likes b"));
        Assert.assertEquals("b", getItemAfterOperator("a startswith b"));
        Assert.assertEquals("b", getItemAfterOperator("a endswith b"));
        Assert.assertEquals("b", getItemAfterOperator("a contains b"));
    }
}
