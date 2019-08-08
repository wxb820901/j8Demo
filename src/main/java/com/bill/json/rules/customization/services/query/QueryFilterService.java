package com.bill.json.rules.customization.services.query;

import com.bill.json.rules.customization.ODataQueryExpression;
import com.bill.json.rules.customization.services.Rule;
import com.bill.json.rules.customization.services.factory.FilterRuleFactory;

import java.util.Map;

import static com.bill.json.rules.customization.FilterOperator.getFilterOperator;

/**
 * query function for filter
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class QueryFilterService  implements Rule {
    @Override
    public Map<String, String> apply(Map<String, String> originjsonPaths, ODataQueryExpression filterExpression) throws Exception {
        //invoke each operator of filter
        Map<String, String> tempoResult = originjsonPaths;
        for(String expression: filterExpression.getExpressions()){
            tempoResult =
                    FilterRuleFactory.getInstance(
                            getFilterOperator(expression)
                    ).apply(tempoResult, filterExpression.getPrefix(),expression);
        }
        return tempoResult;
    }
}
