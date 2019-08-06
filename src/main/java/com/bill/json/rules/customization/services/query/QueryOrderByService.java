package com.bill.json.rules.customization.services.query;

import com.bill.json.rules.customization.ODataQueryExpression;
import com.bill.json.rules.customization.services.Rule;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.stream.Collectors;

import static com.bill.json.rules.customization.util.JsonUtil.getListByPrefix;


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
     * assume ODataQueryExpression.getExpression() like 'label1 desc/asc' default is asc
     * assume label1, label2 is sub element of prefix
     *
     * @param originjsonPaths
     * @param orderByExpression
     * @return
     */
    @Override
    public Map<String, String> apply(
            Map<String, String> originjsonPaths, ODataQueryExpression orderByExpression) throws Exception {
        logger.info("getOrdered " + orderByExpression);
        originjsonPaths = getListByPrefix(originjsonPaths, orderByExpression);
        logger.info("getOrdered originjsonPaths=" + originjsonPaths);

        String[] wantedStrKeys = orderByExpression.getExpression().split(",");
        if (wantedStrKeys.length != 1) {
            throw new Exception("not supported multi orderby - " + orderByExpression.getExpression());
        }
        Map<String, String> keyToAscOrDesc = new HashMap();
        Arrays.asList(wantedStrKeys)
                .stream().forEach(key ->
                {
                    if (key.indexOf("desc") > -1) {
                        keyToAscOrDesc.put(key.trim(), "desc");
                    } else {
                        keyToAscOrDesc.put(key.trim(), "asc");
                    }

                }
        );

        Map<String, String> treeMap;
        //select label and sort by value
        String orderBylable = (String) new ArrayList(keyToAscOrDesc.keySet()).get(0);
        if ("asc".equals(keyToAscOrDesc.get(orderBylable))) {
            treeMap = new TreeMap((Comparator<String>) (o1, o2) -> o2.compareTo(o1));
        } else {
            treeMap = new TreeMap((Comparator<String>) (o1, o2) -> -o2.compareTo(o1));
        }
        treeMap.putAll(
                originjsonPaths
                        .entrySet().stream().filter(
                        entry -> {
                            for (String wantedKey : keyToAscOrDesc.keySet()) {
                                if (entry.getKey().indexOf(orderByExpression.getPrefix()) > -1
                                        && entry.getKey().indexOf(wantedKey.trim()) > -1) {
                                    return true;
                                }
                            }
                            return false;
                        })
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))
        );
        /**
         *  here we got all entry with the order by label
         *  and with consequence by value
         *
         *  build result by sorted prefix[n] and reindex
         */
        Map<String, String> result = new HashMap();
        int reIndex = 0;
        for (String keySorted : treeMap.keySet()) {
            String prefixSorted = keySorted.substring(2, keySorted.indexOf(orderBylable) - 1);
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
