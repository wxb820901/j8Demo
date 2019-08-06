package com.bill.json.rules.customization;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bill.json.rules.customization.ODataQuery.*;


/**
 * wrap result as require
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQuery
 */
public enum ODataQueryResult {

    jsonResult($filter, $select, $orderBy, $count, $top, $skip, $expand) {
        @Override
        public Map apply(Map<String, String> jsonPathMapValue, ODataQuery query, ODataQueryExpression expression) throws Exception {
            return jsonPathMapValue;
        }
    },
    valueResult($value) {
        @Override
        public Map apply(Map<String, String> jsonPathMapValue, ODataQuery query, ODataQueryExpression expression) throws Exception {
            return jsonPathMapValue;
        }
    };

    private List<ODataQuery> operations = new ArrayList<>();

    ODataQueryResult(ODataQuery... ops) {
        Arrays.asList(ops).stream().forEach(oDataOperation -> operations.add(oDataOperation));
    }


    public abstract Map apply(Map<String, String> jsonPathMapValue, ODataQuery query, ODataQueryExpression expression) throws Exception;

    public static ODataQueryResult getODataOperationResult(ODataQuery ODataQuery) throws Exception {
        if (ODataQuery != null) {
            for (ODataQueryResult result : values()) {
                if (result.operations.contains(ODataQuery)) {
                    return result;
                }
            }
        }
        throw new Exception("not supported ODataQuery " + ODataQuery.name());
    }
}
