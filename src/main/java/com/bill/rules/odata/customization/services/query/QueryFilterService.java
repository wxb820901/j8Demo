package com.bill.rules.odata.customization.services.query;

import com.bill.rules.odata.customization.ODataQueryExpression;
import com.bill.rules.odata.customization.services.factory.FilterRuleFactory;
import com.bill.rules.odata.customization.services.Rule;

import java.util.List;
import java.util.Map;

import static com.bill.rules.odata.customization.FilterOperator.getFilterOperator;

/**
 * query expression string as 'table1?join=rs on table2[*].label1 = table1[*].label1&count'
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class QueryFilterService implements Rule {
    /**
     * FILTER list with condition in expression
     *
     * @param originjsonPaths
     * @param filterExpression
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, String> apply(
            Map<String, String> originjsonPaths,
            ODataQueryExpression filterExpression,
            String perfix,
            List<String> expressionStrs) {
        //invoke each operator of FILTER
        Map<String, String> tempoResult = originjsonPaths;
        for (String expression : expressionStrs) {
            tempoResult =
                    FilterRuleFactory.getInstance(
                            getFilterOperator(expression)
                    ).apply(tempoResult, perfix, expression);
        }
        return tempoResult;
    }
}
