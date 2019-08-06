package com.bill.json.rules.customization.services.query;

import com.bill.json.rules.customization.ODataQueryExpression;
import com.bill.json.rules.customization.services.Rule;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * query function for $top
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class QueryTopService implements Rule {
    private static final Logger logger = LogManager.getLogger(QueryTopService.class);
    /**
     * assume ODataQueryExpression.getExpression() like "n"
     *
     * @param originjsonPaths
     * @param topExpression
     * @return
     */
    @Override
    public Map<String, String> apply(Map<String, String> originjsonPaths, ODataQueryExpression topExpression) {
        logger.info("getTop " + topExpression);
        Map<String, String> result = new HashMap();
        String prefixSorted = topExpression.getPrefix();
        int top = Integer.parseInt(topExpression.getExpression().trim());

        //collect all prefix[*]
        List<String> prefixs = new ArrayList();
        for (String key : originjsonPaths.keySet()) {
            String newPrefix = key.split("\\.")[1];
            if (key.indexOf(prefixSorted) > -1
                    && !prefixs.contains(newPrefix)) {
                prefixs.add(newPrefix);
            }
        }
        //get top n prefix[*]
        List<String> topPrefixs = new ArrayList();
        int index = 0;
        while (index < top) {
            topPrefixs.add(topExpression.getPrefix() + "[" + index + "]");
            index++;
        }
        logger.info("getTop topPrefixs=" + topPrefixs);
        //get json by top n prefix[*] reindex
        int reIndex = 0;
        for (String keyTop : topPrefixs) {
            for (String key : originjsonPaths.keySet()) {
                if (key.indexOf(keyTop) > -1) {
                    String value = originjsonPaths.get(key);
                    result.put(key.replace(keyTop,topExpression.getPrefix() + "[" + reIndex + "]"), value);
//                    result.put(key, value);
                }
            }
            reIndex++;
        }
        logger.info("getTop originjsonPaths=" + originjsonPaths);
        logger.info("getTop result=" + result);
        return result;
    }
}
