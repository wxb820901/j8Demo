package com.bill.json.rules.customization.services.filter;

import com.bill.json.rules.customization.FilterOperator;
import com.bill.json.rules.customization.ODataQueryExpression;

import java.util.Map;

/**
 * implement of filter operator start with
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-06 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class FilterStartWithService  extends DefaultFilterService {
    @Override
    boolean apply(Map map, FilterOperator operator) {
        return map.get(operator.getItemBeforeOperator().trim()).toString()
                .startsWith(operator.getItemAfterOperator().trim());
    }
}
