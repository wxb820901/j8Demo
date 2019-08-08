package com.bill.json.rules.customization.services.query;

import com.bill.json.rules.customization.ODataQueryExpression;
import com.bill.json.rules.customization.services.Rule;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bill.json.rules.customization.util.JsonUtil.replaceArrayIndexWithStar;

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
     * assume ODataQueryExpression.getExpressions() like 'label1, label2'
     *
     * @param originjsonPaths
     * @param selectExpression
     * @return
     */
    @Override
    public  Map<String, String> apply(Map<String, String> originjsonPaths, ODataQueryExpression selectExpression) throws Exception {

        if (selectExpression.getExpressions().size() != 1) {
            throw new Exception("not supported multi select expression - " + selectExpression.getExpressions());
        }
        String[] wantedKeys = selectExpression.getExpressions().get(0).split(",");
        return originjsonPaths
                .entrySet().stream().filter(
                        entry -> {
                            for (String wantedKey : wantedKeys) {
                                if (entry.getKey().indexOf(selectExpression.getPrefix()) > -1
                                        &&  replaceArrayIndexWithStar(wantedKey.trim(), entry.getKey()).indexOf(wantedKey.trim()) > -1) {
                                    return true;
                                }
                            }
                            return false;
                        })
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
