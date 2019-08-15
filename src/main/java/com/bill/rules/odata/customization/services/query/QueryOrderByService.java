package com.bill.rules.odata.customization.services.query;

import com.bill.rules.odata.customization.ODataQueryExpression;
import com.bill.rules.odata.customization.services.Rule;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bill.rules.odata.customization.util.JsonUtil.getListByPrefix;
import static com.bill.rules.odata.customization.util.JsonUtil.replaceArrayIndexWithStar;

/**
 * query function for orderby
 * ORDER expression string as 'table1?orderby=table1[*].label1'
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class QueryOrderByService implements Rule {
    private static final Logger logger = LogManager.getLogger(QueryOrderByService.class);
    public static final String DESC = "desc";

    /**
     *  expression string like 'label1 desc/asc' default is asc
     * assume label1, label2 is sub element of prefix
     *
     * @param originjsonPaths
     * @param orderByExpression
     * @return
     */
    @Override
    public Map<String, String> apply(
            Map<String, String> originjsonPaths,
            ODataQueryExpression orderByExpression,
            String perfix,
            List<String> expressionStrs
    ) {

        logger.info("getOrdered "+orderByExpression);
        originjsonPaths = getListByPrefix(originjsonPaths, perfix);
        logger.info("getOrdered originjsonPaths="+ originjsonPaths);

        boolean isDesc = false;
        if (expressionStrs.get(0).indexOf(DESC) > -1) {
            isDesc = true;
        }
        final String orderByLabel = expressionStrs.get(0).replace(DESC, "").trim();


        List<Map.Entry<String, String>> wantedEntrys = originjsonPaths
                .entrySet().stream().filter(
                        entry -> isMatchedJsonPath(entry, orderByLabel, perfix)
                ).collect(Collectors.toList());
        wantedEntrys.sort(Map.Entry.comparingByValue());

        if (isDesc) {
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
            String prefixSorted = entrySortedbyValue.getKey().toString().substring(2, entrySortedbyValue.getKey().toString().indexOf(']')+1);
            for (Map.Entry<String, String> keyAndCalue : originjsonPaths.entrySet()) {
                if (keyAndCalue.getKey().indexOf(prefixSorted) > -1) {
                    result.put(keyAndCalue.getKey().replace(prefixSorted, perfix + "[" + reIndex + "]"), keyAndCalue.getValue());
                }
            }
            reIndex++;
        }
        logger.info("getOrdered result="+result);
        return result;
    }

    private boolean isMatchedJsonPath(Map.Entry<String, String> entry, String orderByLabel, String perfix) {
        return entry.getKey().startsWith("$." + perfix)
                && replaceArrayIndexWithStar(
                orderByLabel.trim(), entry.getKey()
        ).equals("$."+orderByLabel.trim());
    }
}
