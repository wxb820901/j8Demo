package com.bill.json.rules.customization.services.query;

import com.bill.json.rules.customization.ODataQueryExpression;
import com.bill.json.rules.customization.services.Rule;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static com.bill.json.rules.customization.util.JsonUtil.getListByPrefix;
import static com.bill.json.rules.customization.util.JsonUtil.replaceArrayIndexWithStar;

/**
 * query function for $order
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class QueryOrderByService implements Rule {
    private static final Logger logger = LogManager.getLogger(QueryOrderByService.class);
    /**
     * assume ODataQueryExpression.getExpressions() like 'label1 desc/asc' default is asc
     * assume label1, label2 is sub element of prefix
     *
     * @param originjsonPaths
     * @param orderByExpression
     * @return
     */
    @Override
    public Map<String, String> apply(
            Map<String, String> originjsonPaths, ODataQueryExpression orderByExpression) throws Exception {
        if (orderByExpression.getExpressions().size() != 1) {
            throw new Exception("not supported multi orderby - " + orderByExpression.getExpressions());
        }
        logger.info("getOrdered " + orderByExpression);
        originjsonPaths = getListByPrefix(originjsonPaths, orderByExpression.getPrefix());
        logger.info("getOrdered originjsonPaths=" + originjsonPaths);

        String[] wantedStrKeys = orderByExpression.getExpressions().get(0).split(",");
        if (wantedStrKeys.length != 1) {
            throw new Exception("not supported multi orderby - " + orderByExpression.getExpressions());
        }
        Map<String, String> keyToAscOrDesc = new LinkedHashMap<>();
        Arrays.asList(wantedStrKeys)
                .stream().forEach(key ->
                {
                    if (key.indexOf("desc") > -1) {
                        keyToAscOrDesc.put(key.replace("desc","").trim(), "desc");
                    } else {
                        keyToAscOrDesc.put(key.replace("asc","").trim(), "asc");
                    }

                }
        );

        List<Map.Entry<String, String>> wantedEntrys = originjsonPaths
                .entrySet().stream().filter(
                entry -> {
                    for (String wantedKey : keyToAscOrDesc.keySet()) {
                        if (entry.getKey().startsWith("$." + orderByExpression.getPrefix())
                                && replaceArrayIndexWithStar(wantedKey.trim(), entry.getKey()).indexOf(wantedKey.trim()) > -1) {
                            return true;
                        }
                    }
                    return false;
                }).collect(Collectors.toList());
        wantedEntrys.sort(Map.Entry.comparingByValue());
        String orderBylable = (String) new ArrayList(keyToAscOrDesc.keySet()).get(0);
        if ("desc".equals(keyToAscOrDesc.get(orderBylable))) {
            Collections.reverse(wantedEntrys);
        }

        logger.info("getOrdered treeMap="+wantedEntrys);
        /**
         *  here we got all entry ordered by label
         *  and with consequence by value
         *
         *  build result by sorted prefix[n] and reindex
         */
        Map<String, String> result = new LinkedHashMap();
        int reIndex = 0;
        for (Map.Entry entrySortedbyValue : wantedEntrys) {
            String prefixSorted = entrySortedbyValue.getKey().toString().substring(2, entrySortedbyValue.getKey().toString().indexOf(orderBylable) - 1);
            for (String key : originjsonPaths.keySet()) {
                if (key.indexOf(prefixSorted) > -1) {
                    String value = originjsonPaths.get(key);
                    result.put(key.replace(prefixSorted, orderByExpression.getPrefix() + "[" + reIndex + "]"), value);
                }
            }
            reIndex++;
        }
        logger.info("getOrdered result=" + result);
        return result;
    }


}
