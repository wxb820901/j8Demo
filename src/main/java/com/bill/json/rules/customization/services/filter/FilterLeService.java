package com.bill.json.rules.customization.services.filter;

import com.bill.json.rules.customization.FilterOperator;
import com.bill.json.rules.customization.ODataQueryExpression;

import java.util.Map;

/**
 * implement of filter operator le
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class FilterLeService extends DefaultFilterService {

    @Override
    boolean apply(Map map, FilterOperator operator) {
        return Integer.parseInt((String) map.get(operator.getItemBeforeOperator().trim()))
                <= Integer.parseInt(operator.getItemAfterOperator().trim());
    }
}
