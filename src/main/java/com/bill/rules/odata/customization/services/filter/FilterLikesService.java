package com.bill.rules.odata.customization.services.filter;

import com.bill.rules.odata.customization.FilterOperator;
import com.bill.rules.odata.customization.ODataQueryExpression;
import com.bill.rules.odata.customization.util.JsonUtil;

import java.util.Map;

/**
 * implement of filter operator contains
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-06 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class FilterLikesService extends DefaultNormalFilterService {

    @Override
    public boolean compare(Map map, String expresison) {
        return JsonUtil.getSubJsonPathMapWithArrayIndex(
                map,
                FilterOperator.getItemBeforeOperator(expresison).trim(),
                param -> JsonUtil.like(param[0], param[1]),
                FilterOperator.getItemAfterOperator(expresison).trim()
        );
    }
}
