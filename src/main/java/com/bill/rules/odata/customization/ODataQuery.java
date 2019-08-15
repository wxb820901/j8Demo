package com.bill.rules.odata.customization;

import com.bill.rules.odata.customization.util.JsonUtil;

import java.util.List;

import static com.bill.rules.odata.customization.ODataQueryResult.getODataOperationResult;

/**
 * implement OData concrete operations
 *
 *      FILTER,
 *              eq
 *              lt
 *              gt
 *              ge
 *              le
 *              contains (if value contains specific string)
 *              startWith
 *              endWith
 *              isExist (if label is exist)
 *              or
 *      SELECT,
 *      ORDERBY,(just support 1 label) ??? if label iw element of a array, sort the array value or sort outer array of prefix
 *      COUNT,
 *      TOP,
 *      SKIP,

 *      JOIN (not support)
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public enum ODataQuery {
    FILTER,
    SELECT,
    ORDERBY,
    COUNT,
    TOP,
    SKIP,
    JOIN;

    /**
     * default implement of query apply
     * return json after current customization
     *
     * @param expression
     * @param json
     * @return
     * @throws Exception
     */
    public String apply(ODataQueryExpression expression, String json, String prefix, List<String> expressionStrs)  {
        return JsonUtil.convertMapToJson(
                        getODataOperationResult(this).apply(
                                expression.apply(
                                        JsonUtil.convertJsonToMap(
                                                json
                                        ),
                                        prefix,
                                        expressionStrs
                                ), this, expression
                        )
                );
    }

    public static ODataQuery getQuery(String string){
        if (string != null) {
            for (ODataQuery oDataQuery : values()) {
                if (string.toLowerCase().contains(oDataQuery.name().toLowerCase())) {
                    return oDataQuery;
                }
            }
            throw new UnsupportedOperationException("not supported query function " + string);
        }
        throw new UnsupportedOperationException("query function is null");
    }

}
