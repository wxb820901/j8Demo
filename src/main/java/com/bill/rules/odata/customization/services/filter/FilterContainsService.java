package com.bill.rules.odata.customization.services.filter;

import com.bill.rules.odata.customization.FilterOperator;
import com.bill.rules.odata.customization.ODataQueryExpression;

import java.util.Map;

import static com.bill.rules.odata.customization.util.JsonUtil.getSubJsonPathMapWithArrayIndex;

/**
 * implement of filter operator contains
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-06 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class FilterContainsService extends DefaultNormalFilterService {

    @Override
    public boolean compare(Map map, String expresison) {
        return getSubJsonPathMapWithArrayIndex(
                map,
                FilterOperator.getItemBeforeOperator(expresison).trim(),
                param -> param[0].contains(param[1]),
                FilterOperator.getItemAfterOperator(expresison).trim()
        );
    }
}
