package com.bill.demo.json.util.rules;

import com.bill.demo.json.util.JsonUtil;
import com.jayway.jsonpath.JsonPath;

import static com.bill.demo.json.util.JsonUtil.getJsonByMap;
import static com.bill.demo.json.util.rules.ODataOperationResult.getODataOperationResult;

/**
 * implement OData concrete operations
 */
public enum ODataOperation {
    $filter{
        /*
         * assume ODataOperationExpression.getExpression() like 'prefix?filter=label1 eq 'value''
         * */
        String apply(ODataOperationExpression expression, String json) throws Exception {
            String listByPrefix = getJsonByJsonPath(json, "$."+getPrefix()+"[*]");
            String tempoJsonStr = getJsonByJsonPath(listByPrefix, expression.getExpression());
            ODataOperationResult result = getODataOperationResult(this);
            return result.wrapResult(tempoJsonStr);
        }
    },
    $select{
        /*
        * assume ODataOperationExpression.getExpression() like 'prefix?$select=label1, label2'
        * */
        String apply(ODataOperationExpression expression, String json) throws Exception {
            String tempoJsonStr =
                    JsonUtil.convertMapToJson(
                            JsonUtil.getSelected(
                                    JsonUtil.convertJsonToMap(
                                            json
                                    )
                                    ,expression
                            )
                    );
            ODataOperationResult result = getODataOperationResult(this);
            return result.wrapResult(tempoJsonStr);
        }
    },
    $orderBy{
        /*
         * assume ODataOperationExpression.getExpression() like 'prefix?$orderBy=label1'
         * tempo just support order by 1 label
         * */
        String apply(ODataOperationExpression expression, String json) throws Exception {
            String tempoJsonStr =
                    JsonUtil.convertMapToJson(
                            JsonUtil.getOrdered(
                                    JsonUtil.convertJsonToMap(
                                            json
                                    )
                                    ,expression
                            )
                    );
            ODataOperationResult result = getODataOperationResult(this);
            return result.wrapResult(tempoJsonStr);
        }
    },
    $count{
        /*
         * assume ODataOperationExpression.getExpression() like 'prefix?$count'
         * */
        String apply(ODataOperationExpression expression, String json) throws Exception {
            String tempoJsonStr =
                    JsonUtil.convertMapToJson(
                            JsonUtil.getCount(
                                    JsonUtil.convertJsonToMap(
                                            json
                                    )
                                    ,expression
                            )
                    );
            ODataOperationResult result = getODataOperationResult(this);
            return result.wrapResult(tempoJsonStr);
        }
    },
    $top{
        /*
         * assume ODataOperationExpression.getExpression() like 'prefix?$top=1'
         * */
        String apply(ODataOperationExpression expression, String json) throws Exception {
            String tempoJsonStr =
                    JsonUtil.convertMapToJson(
                            JsonUtil.getTop(
                                    JsonUtil.convertJsonToMap(
                                            json
                                    )
                                    ,expression
                            )
                    );
            ODataOperationResult result = getODataOperationResult(this);
            return result.wrapResult(tempoJsonStr);
        }
    },
    $skip{
        /*
         * assume ODataOperationExpression.getExpression() like 'prefix?$skip=1'
         * */
        String apply(ODataOperationExpression expression, String json) throws Exception {
            String tempoJsonStr =
                    JsonUtil.convertMapToJson(
                            JsonUtil.getSkip(
                                    JsonUtil.convertJsonToMap(
                                            json
                                    )
                                    ,expression
                            )
                    );
            ODataOperationResult result = getODataOperationResult(this);
            return result.wrapResult(tempoJsonStr);
        }
    },
    $value{
        String apply(ODataOperationExpression expression, String json) throws Exception {
            return json;
        }
    },
    $expand{
        String apply(ODataOperationExpression expression, String json) throws Exception {
            throw new Exception("not supported operation $expand");
        }
    };


    private String prefix;
    ODataOperation withPrefix(String prefix){
        this.prefix = prefix;
        return this;
    }
    public String getPrefix(){
        return this.prefix;
    }


    /* ========== may be move to util class begin ========== */
    /**
     * json path query used by $filter, $select
     * @param json
     * @param jsonPath
     * @return
     */
    public static String getJsonByJsonPath(String json, String jsonPath){
        Object appliedJson = JsonPath.parse(json).read(jsonPath, Object.class);
        return getJsonByMap(appliedJson);
    }
    /* ========== may be move to util class end ========== */


    /**
     * return json after current operation
     * @param expression
     * @param json
     * @return
     * @throws Exception
     */
    abstract String apply(ODataOperationExpression expression, String json) throws Exception ;

    public static ODataOperation getOperation(java.lang.String string) throws Exception {
        if(string != null){
            for(ODataOperation ODataOperation : values()){
                if(string.contains(ODataOperation.name())){
                    return ODataOperation;
                }
            }
        }
        throw new Exception("not supported operation "+string);
    }

}
