package com.bill.demo.json.util.rules;

import org.junit.Test;
import org.testng.Assert;

import java.util.List;
import java.util.Map;
import static com.bill.demo.json.util.rules.ODataOperationParser.parseOperations;

public class ODataOperationParserTest {


    @Test
    public void testFilterQueryString() throws Exception {
        String queryString = "type1?$filter=$.data[?(@.name=='xxyy')]";
        Map<ODataOperation, List<ODataOperationExpression>> oDataOperationODataOperationExpressionMap =  parseOperations(queryString);
        Assert.assertTrue(oDataOperationODataOperationExpressionMap.size() == 1);
        Assert.assertNotNull(oDataOperationODataOperationExpressionMap.get(ODataOperation.$filter));
        Assert.assertTrue(oDataOperationODataOperationExpressionMap.get(ODataOperation.$filter).size()==1);
        Assert.assertEquals("filterExpresion",oDataOperationODataOperationExpressionMap.get(ODataOperation.$filter).get(0).name());
        Assert.assertEquals("$.data[?(@.name=='xxyy')]",
                oDataOperationODataOperationExpressionMap.get(ODataOperation.$filter).get(0).getExpression());
        oDataOperationODataOperationExpressionMap
                .keySet().stream().forEach(
                        oDataOperation -> Assert.assertEquals("type1", oDataOperation.getPrefix()));

    }

    @Test
    public void testFilterAndSelectQueryString() throws Exception {
        String queryString = "type1?$filter=$.data[?(@.name=='xxyy')]&$select=$.data[*]['name','desc']";
        Map<ODataOperation, List<ODataOperationExpression>> oDataOperationODataOperationExpressionMap =  parseOperations(queryString);
        Assert.assertTrue(oDataOperationODataOperationExpressionMap.size() == 2);
        Assert.assertNotNull(oDataOperationODataOperationExpressionMap.get(ODataOperation.$filter));
        Assert.assertNotNull(oDataOperationODataOperationExpressionMap.get(ODataOperation.$select));
        Assert.assertTrue(oDataOperationODataOperationExpressionMap.get(ODataOperation.$filter).size()==1);
        Assert.assertTrue(oDataOperationODataOperationExpressionMap.get(ODataOperation.$select).size()==1);

        Assert.assertEquals("$.data[?(@.name=='xxyy')]",
                oDataOperationODataOperationExpressionMap.get(ODataOperation.$filter).get(0).getExpression());


        Assert.assertEquals("$.data[*]['name','desc']",
                oDataOperationODataOperationExpressionMap.get(ODataOperation.$select).get(0).getExpression());
        oDataOperationODataOperationExpressionMap
                .keySet().stream().forEach(
                        oDataOperation -> Assert.assertEquals("type1", oDataOperation.getPrefix()));


    }
}
