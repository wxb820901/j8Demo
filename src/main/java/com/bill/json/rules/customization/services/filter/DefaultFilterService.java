package com.bill.json.rules.customization.services.filter;



import com.bill.json.rules.customization.FilterOperator;
import com.bill.json.rules.customization.ODataQueryExpression;
import com.bill.json.rules.customization.services.Rule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bill.json.rules.customization.util.JsonUtil.*;


/**
 * common implement of filter different operator
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public abstract class DefaultFilterService implements Rule {
    @Override
    public Map<String, String> apply(Map<String, String> originJsonPaths, ODataQueryExpression filterExpression) throws Exception {
        FilterOperator operator = filterExpression.getFilterOperators().get(0);
        originJsonPaths = getListByPrefix(originJsonPaths, filterExpression);

        List<Map<String, Object>> elementOfPrefix =
                ((List<Map<String, Object>>) (((Map<String, List>) getJsonMap(originJsonPaths))
                        .get(filterExpression.getPrefix())))
                        .stream().filter(map -> apply(map, operator)).collect(Collectors.toList());
        Map<String, Object> prefix = new HashMap();
        prefix.put(filterExpression.getPrefix(), elementOfPrefix);
        Map<String, Object> root = new HashMap();
        root.put("$", prefix);
        String json = getJsonByMap(root);
        return convertJsonToMap(json);
    }

    abstract boolean apply(Map map, FilterOperator operator);
}
