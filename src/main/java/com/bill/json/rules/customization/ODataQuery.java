package com.bill.json.rules.customization;


import static com.bill.json.rules.customization.ODataQueryResult.getODataOperationResult;
import static com.bill.json.rules.customization.util.JsonUtil.convertJsonToMap;
import static com.bill.json.rules.customization.util.JsonUtil.convertMapToJson;

/**
 * implement OData concrete operations
 *
 *      $filter,
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
 *      $select,
 *      $orderBy,(just support 1 label)
 *      $count,
 *      $top,
 *      $skip,
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public enum ODataQuery {
    $filter,
    $select,
    $orderBy,
    $count,
    $top,
    $skip,
    $value {
        /*
         * assume ODataQueryExpression.getExpressions() like '=label1'
         * */
        public String apply(ODataQueryExpression expression, String json) throws Exception {
            throw new Exception("not supported customization $expand");
        }
    },
    $expand {
        public String apply(ODataQueryExpression expression, String json) throws Exception {
            throw new Exception("not supported customization $expand");
        }
    };

    //string before $query_enum=
    private String prefix;

    ODataQuery withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public String getPrefix() {
        return this.prefix;
    }

    /**
     * default implement of query apply
     * return json after current customization
     *
     * @param expression
     * @param json
     * @return
     * @throws Exception
     */
    public String apply(ODataQueryExpression expression, String json) throws Exception {
        String tempoJsonStr =
                convertMapToJson(
                        getODataOperationResult(this).apply(
                                expression.apply(
                                        convertJsonToMap(
                                                json
                                        )
                                ), this, expression
                        )
                );
        return tempoJsonStr;
    }

    public static ODataQuery getQuery(String string) throws Exception {
        if (string != null) {
            for (ODataQuery oDataQuery : values()) {
                if (string.contains(oDataQuery.name())) {
                    return oDataQuery;
                }
            }
        }
        throw new Exception("not supported customization " + string);
    }

}
