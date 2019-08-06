package com.bill.json.rules.customization.services.query;

import com.bill.json.rules.customization.FilterOperator;
import com.bill.json.rules.customization.ODataQueryExpression;
import com.bill.json.rules.customization.services.Rule;

import java.util.Map;

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
    public Map<String, String> apply(Map<String, String> originjsonPaths, ODataQueryExpression countExpression) throws Exception {
        //invoke each filter
        Map<String, String> tempoResult = originjsonPaths;
        for(FilterOperator filterOperator: countExpression.getFilterOperators()){
            tempoResult = filterOperator.apply(tempoResult);
        }
        return tempoResult;
    }
}
