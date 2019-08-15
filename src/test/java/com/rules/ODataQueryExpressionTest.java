package com.rules;

import org.junit.Assert;
import org.junit.Test;

import static com.bill.rules.odata.customization.ODataQuery.*;
import static com.bill.rules.odata.customization.ODataQueryExpression.*;

public class ODataQueryExpressionTest {
    @Test
    public void testGetOperationExpression() {
        Assert.assertEquals(COUNT_EXPRESSION, getOperationExpression(COUNT));
        Assert.assertEquals(SKIP_EXPRESSION, getOperationExpression(SKIP));
        Assert.assertEquals(TOP_EXPRESSION, getOperationExpression(TOP));
        Assert.assertEquals(ORDER_BY_EXPRESSION, getOperationExpression(ORDERBY));
        Assert.assertEquals(SELECT_EXPRESSION, getOperationExpression(SELECT));
        Assert.assertEquals(FILTER_EXPRESSION, getOperationExpression(FILTER));
    }
}
