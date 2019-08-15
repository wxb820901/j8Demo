package com.bill.rules.odata.customization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * wrap result as require
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQuery
 */
public enum ODataQueryResult {

    JSON_RESULT(ODataQuery.FILTER, ODataQuery.SELECT, ODataQuery.ORDERBY, ODataQuery.COUNT, ODataQuery.TOP, ODataQuery.SKIP, ODataQuery.JOIN) {
        @Override
        public Map apply(Map<String, String> jsonPathMapValue, ODataQuery query, ODataQueryExpression expression) {
            return jsonPathMapValue;
        }
    };

    private List<ODataQuery> operations = new ArrayList<>();

    ODataQueryResult(ODataQuery... ops) {
        Arrays.asList(ops).stream().forEach(oDataOperation -> operations.add(oDataOperation));
    }


    public abstract Map apply(Map<String, String> jsonPathMapValue, ODataQuery query, ODataQueryExpression expression);

    public static ODataQueryResult getODataOperationResult(ODataQuery oDataQuery){
        if (oDataQuery != null) {
            for (ODataQueryResult result : values()) {
                if (result.operations.contains(oDataQuery)) {
                    return result;
                }
            }
            throw new UnsupportedOperationException("not supported ODataQuery " + oDataQuery.name());
        }
        throw new UnsupportedOperationException("ODataQuery is null");
    }
}
