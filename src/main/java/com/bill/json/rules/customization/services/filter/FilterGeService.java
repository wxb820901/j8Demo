package com.bill.json.rules.customization.services.filter;

import java.util.Map;

import static com.bill.json.rules.customization.FilterOperator.getItemAfterOperator;
import static com.bill.json.rules.customization.FilterOperator.getItemBeforeOperator;

/**
 * implement of filter operator ge
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class FilterGeService extends DefaultNormalFilterService {

    @Override
    public boolean compare(Map map, String expresison) {
        return Integer.parseInt(map.get(getItemBeforeOperator(expresison).trim()).toString()) >=
                Integer.parseInt(getItemAfterOperator(expresison).trim());
    }
}
