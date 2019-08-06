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
 * implement of filter operator check label is exist
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-06 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class FilterIsExistService implements Rule {
    @Override
    public Map<String, String> apply(Map<String, String> originjsonPaths, ODataQueryExpression filterExpression) throws Exception {
        FilterOperator operator = filterExpression.getFilterOperators().get(0);
        originjsonPaths = getListByPrefix(originjsonPaths, filterExpression);


        List<Map<String, Object>> elementOfPrefix =
                ((List<Map<String, Object>>) (((Map<String, List>) getJsonMap(originjsonPaths))
                        .get(filterExpression.getPrefix())))
                        .stream().filter(
                                map -> {
                                    String[] labels = operator.getItemAfterOperator().split(",");
                                    for(String label: labels){
                                        if(!map.keySet().contains(label.trim())){
                                            return false;
                                        }
                                    }
                                    return true;
                                }
                ).collect(Collectors.toList());
        Map<String, Object> prefix = new HashMap();
        prefix.put(filterExpression.getPrefix(), elementOfPrefix);
        Map<String, Object> root = new HashMap();
        root.put("$", prefix);
        String json = getJsonByMap(root);
        return convertJsonToMap(json);
    }
}