package com.bill.rules.odata.customization.services.query;

import com.bill.rules.odata.customization.ODataQueryExpression;
import com.bill.rules.odata.customization.services.Rule;
import com.bill.rules.odata.customization.util.JsonUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * query function for $select
 * SELECT expression string as 'table1?$select=table1[*].label1'
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class QuerySelectService implements Rule {
    /**
     * query function for select
     * SELECT expression string as 'table1?select=table1[*].label1'
     *
     * @param originjsonPaths
     * @param selectExpression
     * @return
     */
    @Override
    public Map<String, String> apply(
            Map<String, String> originjsonPaths,
            ODataQueryExpression selectExpression,
            String perfix,
            List<String> expressionStrs) {
        String[] wantedKeys = expressionStrs.get(0).split(",");
        return originjsonPaths
                .entrySet().stream().filter(
                        entry -> {
                            for (String wantedKey : wantedKeys) {
                                if (entry.getKey().indexOf(perfix) > -1
                                        && JsonUtil.replaceArrayIndexWithStar("$." + wantedKey.trim(), entry.getKey()).indexOf("$." + wantedKey.trim()) == 0) {
                                    return true;
                                }
                            }
                            return false;
                        })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
