package com.bill.json.rules.customization.services.filter;


import com.bill.json.rules.customization.services.NormalFilterRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bill.json.rules.customization.FilterOperator.getItemAfterOperator;
import static com.bill.json.rules.customization.util.JsonUtil.*;


/**
 * implement of filter operator check label is exist
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-06 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class FilterIsExistService implements NormalFilterRule {
    @Override
    public Map<String, String> apply(Map<String, String> originjsonPaths, String prefix, String filterExpression) {

        originjsonPaths = getListByPrefix(originjsonPaths, prefix);
        List<Map<String, Object>> elementOfPrefix =
                (List<Map<String, Object>>) ((Map<String, List>) getJsonMap(originjsonPaths)).get(prefix)
                        .stream().filter(
                                map -> {
                                    String[] labels = getItemAfterOperator(filterExpression).split(",");
                                    for (String label : labels) {
                                        if (!((Map)map).keySet().contains(label.trim())) {
                                            return false;
                                        }
                                    }
                                    return true;
                                }
                        ).collect(Collectors.toList());


        Map<String, Object> prefixMap = new HashMap();
        prefixMap.put(prefix, elementOfPrefix);
        String json = getJsonByMap(prefixMap);
        return convertJsonToMap(json);
    }

    @Override
    public boolean compare(Map map, String expression) {
        String[] labels = getItemAfterOperator(expression).split(",");
        for (String label : labels) {
            if (!map.keySet().contains(label.trim())) {
                return false;
            }
        }
        return true;

    }

}