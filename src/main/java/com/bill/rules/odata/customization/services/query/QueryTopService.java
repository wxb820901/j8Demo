package com.bill.rules.odata.customization.services.query;

import com.bill.rules.odata.customization.ODataQueryExpression;
import com.bill.rules.odata.customization.services.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * query function for $TOP
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class QueryTopService implements Rule {

    /**
     *  expression string like "n"
     *
     * @param originjsonPaths
     * @param topExpression
     * @return
     */
    @Override
    public Map<String, String> apply(
            Map<String, String> originjsonPaths,
            ODataQueryExpression topExpression,
            String perfix,
            List<String> expressionStrs) {
        Map<String, String> result = new HashMap();
        int top = Integer.parseInt(expressionStrs.get(0).trim());
        //collect all prefix[*]
        List<String> prefixs = new ArrayList();
        for (String key : originjsonPaths.keySet()) {
            String newPrefix = key.split("\\.")[1];
            if (key.indexOf(perfix) > -1
                    && !prefixs.contains(newPrefix)) {
                prefixs.add(newPrefix);
            }
        }
        //get TOP n prefix[*]
        List<String> topPrefixs = new ArrayList();
        int index = 0;
        while (index < top) {
            topPrefixs.add(perfix + "[" + index + "]");
            index++;
        }
        //get json by TOP n prefix[*] reindex
        int reIndex = 0;
        for (String keyTop : topPrefixs) {
            for (Map.Entry<String, String> keyAndValue : originjsonPaths.entrySet()) {
                if (keyAndValue.getKey().indexOf(keyTop) > -1) {
                    result.put(keyAndValue.getKey().replace(keyTop, perfix + "[" + reIndex + "]"), keyAndValue.getValue());
                }
            }
            reIndex++;
        }
        return result;
    }
}
