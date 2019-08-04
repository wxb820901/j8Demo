package com.bill.demo.json.util.rules;

import com.jayway.jsonpath.JsonPath;

import static com.bill.demo.json.util.JsonUtil.getJsonByMap;
import static com.bill.demo.json.util.rules.ODataOperationResult.getODataOperationResult;

/**
 * implement OData concrete operations
 */
public enum ODataOperation {
    $filter{
        String apply(ODataOperationExpression expression, String json) throws Exception {
            String tempoJsonStr = getJsonByJsonPath(json, expression.getExpression());
            ODataOperationResult result = getODataOperationResult(this);
            return result.wrapResult(tempoJsonStr);
        }
    },
    $select{
        String apply(ODataOperationExpression expression, String json) throws Exception {
            String tempoJsonStr = getJsonByJsonPath(json, expression.getExpression());
            return tempoJsonStr;
        }
    },
    $orderBy{
        String apply(ODataOperationExpression expression, String json) throws Exception {
            return json;
        }
    },
    $count{
        String apply(ODataOperationExpression expression, String json) throws Exception {
            return json;
        }
    },
    $top{
        String apply(ODataOperationExpression expression, String json) throws Exception {
            return json;
        }
    },
    $skip{
        String apply(ODataOperationExpression expression, String json) throws Exception {
            return json;
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
