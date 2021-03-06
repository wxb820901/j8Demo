package com.bill.rules.odata.customization.services.filter;

import com.bill.rules.odata.customization.ODataQueryExpression;

import java.util.Map;

import static com.bill.rules.odata.customization.FilterOperator.getItemAfterOperator;
import static com.bill.rules.odata.customization.FilterOperator.getItemBeforeOperator;
import static com.bill.rules.odata.customization.util.JsonUtil.getSubJsonPathMapWithArrayIndex;


/**
 * implement of filter operator end with
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-06 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class FilterEndWithService extends DefaultNormalFilterService {
    @Override
    public boolean compare(Map map, String expresison) {
        return getSubJsonPathMapWithArrayIndex(
                map,
                getItemBeforeOperator(expresison).trim(),
                param -> param[0].endsWith(param[1]),
                getItemAfterOperator(expresison).trim()
        );
    }
}
