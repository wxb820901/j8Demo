package com.bill.json.rules.customization.services.query;

import com.bill.json.rules.customization.ODataQueryExpression;
import com.bill.json.rules.customization.services.Rule;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * query function for $select
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class QuerySelectService implements Rule {
    /**
     * assume ODataQueryExpression.getExpression() like 'label1, label2'
     *
     * @param originjsonPaths
     * @param filterOrSelect
     * @return
     */
    @Override
    public  Map<String, String> apply(Map<String, String> originjsonPaths, ODataQueryExpression filterOrSelect) {
        String[] wantedKeys = filterOrSelect.getExpression().split(",");
        return originjsonPaths
                .entrySet().stream().filter(
                        entry -> {
                            for (String wantedKey : wantedKeys) {
                                if (entry.getKey().indexOf(filterOrSelect.getPrefix()) > -1
                                        && entry.getKey().indexOf(wantedKey.trim()) > -1) {
                                    return true;
                                }
                            }
                            return false;
                        })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
