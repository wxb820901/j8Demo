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
 * query function for $skip
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class QuerySkipService implements Rule {
    private static final Logger logger = LogManager.getLogger(QuerySkipService.class);
    /**
     * assume ODataQueryExpression.getExpressions() like "n"
     *
     * @param originjsonPaths
     * @param skipExpression
     * @return
     */
    @Override
    public Map<String, String> apply(Map<String, String> originjsonPaths, ODataQueryExpression skipExpression) throws Exception {
        if (skipExpression.getExpressions().size() != 1) {
            throw new Exception("not supported multi skip expression - " + skipExpression.getExpressions());
        }

        logger.info("getSkip " + skipExpression);
        Map<String, String> result = new HashMap();
        String prefixSorted = skipExpression.getPrefix();
        int skip = Integer.parseInt(skipExpression.getExpressions().get(0).trim());

        //collect all prefix[*] distinct
        List<String> prefixs = new ArrayList();
        for (String key : originjsonPaths.keySet()) {
            String newPrefix = key.split("\\.")[1];
            if (key.indexOf(prefixSorted) > -1
                    && !prefixs.contains(newPrefix)) {
                prefixs.add(newPrefix);
            }
        }
        //get top n prefix[*]
        List<String> skipedPrefixs = new ArrayList();
        int index = 0;
        while (index < prefixs.size()) {
            if (index > skip - 1) {
                skipedPrefixs.add(skipExpression.getPrefix() + "[" + index + "]");
            }
            index++;
        }
        logger.info("getSkip skipedPrefixs=" + skipedPrefixs);
        //get json by skip n prefix[*] reindex
        int reIndex = 0;
        for (String keySkip : skipedPrefixs) {
            for (String key : originjsonPaths.keySet()) {
                if (key.indexOf(keySkip) > -1) {
                    String value = originjsonPaths.get(key);
                    result.put(key.replace(keySkip,skipExpression.getPrefix() + "[" + reIndex + "]"), value);
//                    result.put(key, value);
                }
            }
            reIndex++;
        }
        logger.info("getSkip originjsonPaths=" + originjsonPaths);
        logger.info("getSkip result=" + result);
        return result;
    }
}
