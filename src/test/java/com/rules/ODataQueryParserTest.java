package com.rules;

import com.bill.rules.odata.customization.ODataQueryExpression;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.bill.rules.odata.customization.ODataQueryParser.parseOperations;

public class ODataQueryParserTest {
    private static final Logger logger = LogManager.getLogger(ODataQueryParserTest.class);

    @Test
    public void testParseOperations() {
        String queryString = "table1?filter=label1 eq 1 or label1 eq 2&count";
        Map<String, Map<ODataQueryExpression, List<String>>> oDataOperationODataOperationExpressionMap = parseOperations(queryString);
        logger.info(oDataOperationODataOperationExpressionMap);
        Assert.assertTrue(oDataOperationODataOperationExpressionMap.size() == 1);
    }
}
