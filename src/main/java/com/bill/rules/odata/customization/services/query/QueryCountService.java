package com.bill.rules.odata.customization.services.query;

import com.bill.rules.odata.customization.ODataQueryExpression;
import com.bill.rules.odata.customization.services.Rule;

import java.util.*;

/**
 * query function for $COUNT
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class QueryCountService implements Rule {
    /**
     * expression string like ""
     * COUNT result list
     * @param originjsonPaths
     * @param countExpression
     * @return
     */
    @Override
    public Map<String, String> apply(Map<String, String> originjsonPaths, ODataQueryExpression countExpression, String perfix, List<String> expressionStrs) {
        Map<String, String> result = new HashMap();
        Set<String> prefixs = new HashSet();
        for (Map.Entry<String, String> keyAndValue : originjsonPaths.entrySet()) {
            if (keyAndValue.getKey().indexOf(perfix) > -1) {
                result.put(keyAndValue.getKey(), keyAndValue.getValue());
                prefixs.add(keyAndValue.getKey().split("\\.")[1]);
            }
        }
        result.put("$.COUNT", Integer.toString(prefixs.size()));
        return result;
    }
}
