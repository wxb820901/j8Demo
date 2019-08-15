package com.bill.rules.odata.customization.services.query;

import com.bill.rules.odata.customization.ODataQueryExpression;
import com.bill.rules.odata.customization.services.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * query function for $SKIP
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class QuerySkipService implements Rule {

    /**
     *  expression string like "n"
     *
     * @param originjsonPaths
     * @param skipExpression
     * @return
     */
    @Override
    public Map<String, String> apply(
            Map<String, String> originjsonPaths,
            ODataQueryExpression skipExpression,
            String perfix,
            List<String> expressionStrs) {
        Map<String, String> result = new HashMap();
        int skip = Integer.parseInt(expressionStrs.get(0).trim());
        //collect all prefix[*] distinct
        List<String> prefixs = new ArrayList();
        for (String key : originjsonPaths.keySet()) {
            String newPrefix = key.split("\\.")[1];
            if (key.indexOf(perfix) > -1
                    && !prefixs.contains(newPrefix)) {
                prefixs.add(newPrefix);
            }
        }
        //get TOP n prefix[*]
        List<String> skipedPrefixs = new ArrayList();
        int index = 0;
        while (index < prefixs.size()) {
            if (index > skip - 1) {
                skipedPrefixs.add(perfix + "[" + index + "]");
            }
            index++;
        }
        //get json by SKIP n prefix[*] reindex
        int reIndex = 0;
        for (String keySkip : skipedPrefixs) {
            for (Map.Entry<String, String> keyAndValue : originjsonPaths.entrySet()) {
                if (keyAndValue.getKey().indexOf(keySkip) > -1) {
                    result.put(keyAndValue.getKey().replace(keySkip, perfix + "[" + reIndex + "]"), keyAndValue.getValue());
                }
            }
            reIndex++;
        }
        return result;
    }
}
