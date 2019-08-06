package com.bill.json.rules.customization.services.filter;

import com.bill.json.rules.customization.FilterOperator;
import com.bill.json.rules.customization.ODataQueryExpression;

import java.util.Map;

/**
 * implement of filter operator eq
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class FilterEqService extends DefaultFilterService {

    @Override
    boolean apply(Map map, FilterOperator operator) {
        return operator.getItemAfterOperator().trim().equals(
                map.get(operator.getItemBeforeOperator().trim()
                ));
    }
}
