package com.bill.json.rules.customization.services.query;

import com.bill.json.rules.customization.ODataQueryExpression;
import com.bill.json.rules.customization.services.Rule;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * query function for $count
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class QueryCountService implements Rule {
    /**
     * assume ODataQueryExpression.getExpression() like ""
     *
     * @param originjsonPaths
     * @param countExpression
     * @return
     */
    @Override
    public Map<String, String> apply(Map<String, String> originjsonPaths, ODataQueryExpression countExpression) {
        Map<String, String> result = new HashMap();
        String prefixSorted = countExpression.getPrefix();
        Set<String> prefixs = new HashSet();
        for (String key : originjsonPaths.keySet()) {
            if (key.indexOf(prefixSorted) > -1) {
                String value = originjsonPaths.get(key);
                result.put(key, value);
                prefixs.add(key.split("\\.")[1]);
            }
        }
        result.put("$.count", new Integer(prefixs.size()).toString());
        return result;
    }
}
