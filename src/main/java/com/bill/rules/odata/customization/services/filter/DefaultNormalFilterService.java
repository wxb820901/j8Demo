package com.bill.rules.odata.customization.services.filter;

import com.bill.rules.odata.customization.ODataQueryExpression;
import com.bill.rules.odata.customization.services.NormalFilterRule;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bill.rules.odata.customization.util.JsonUtil.*;

/**
 * common implement of filter different operator
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public abstract class DefaultNormalFilterService implements NormalFilterRule {
    /**
     * common method to FILTER from list of prefix
     * @param originJsonPaths
     * @param prefix
     * @param expression
     * @return
     */
    @Override
    public Map<String, String> apply(Map<String, String> originJsonPaths, String prefix, String expression) {

        originJsonPaths = getListByPrefix(originJsonPaths, prefix);

        List<Map<String, Object>> elementOfPrefix =
                ((List<Map<String, Object>>) (((Map<String, List>) getJsonMap(originJsonPaths)).get(prefix)))
                        .stream().filter(map -> {
                    try {
                        return compare(map, expression);
                    } catch (Exception e) {
                        return false;
                    }
                })
                        .collect(Collectors.toList());
        Map<String, Object> prefixMap = new HashMap();
        prefixMap.put(prefix, elementOfPrefix);
        String json = getJsonByMap(prefixMap);
        return convertJsonToMap(json);
    }
}
