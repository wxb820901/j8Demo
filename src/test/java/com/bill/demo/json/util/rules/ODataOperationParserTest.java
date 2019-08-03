package com.bill.demo.json.util.rules;

import org.junit.Test;
import org.testng.Assert;
import java.util.Map;
import static com.bill.demo.json.util.rules.ODataOperationParser.parseOperations;

public class ODataOperationParserTest {


    @Test
    public void testFilterQueryString() throws Exception {
        String queryString = "$filter=$.data[?(@.name = xxyy)]";
        Map<ODataOperation, ODataOperationExpression> oDataOperationODataOperationExpressionMap =  parseOperations(queryString);
        Assert.assertTrue(oDataOperationODataOperationExpressionMap.size() == 1);
        Assert.assertNotNull(oDataOperationODataOperationExpressionMap.get(ODataOperation.$filter));
        Assert.assertEquals("filterExpresion",oDataOperationODataOperationExpressionMap.get(ODataOperation.$filter).name());
        Assert.assertEquals("$.data[?(@.name = xxyy)]",
                oDataOperationODataOperationExpressionMap.get(ODataOperation.$filter).getExpression());

    }
}
