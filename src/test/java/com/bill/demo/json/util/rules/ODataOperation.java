package com.bill.demo.json.util.rules;

import com.jayway.jsonpath.JsonPath;

import java.util.Map;

import static com.bill.demo.json.util.JsonUtil.getJsonByMap;

/**
 * implement OData concrete operations
 */
public enum ODataOperation {
    $filter{
        String apply(ODataOperationExpression expression, String json) throws Exception {
            String tempoJsonStr = getJsonByJsonPath(json, expression.getExpression());
            setTempoResult(tempoJsonStr);//tempo result is filtered json
            return tempoJsonStr;
        }
    },
    $select{
        String apply(ODataOperationExpression expression, String json) throws Exception {
            String tempoJsonStr = getJsonByJsonPath(json, expression.getExpression());
            setTempoResult(tempoJsonStr);//tempo result is filtered json
            return tempoJsonStr;
        }
    },
    $orderBy{
        String apply(ODataOperationExpression expression, String json) throws Exception {
            setTempoResult(json);
            return json;
        }
    },
    $count{
        String apply(ODataOperationExpression expression, String json) throws Exception {
            String count = null;
            setTempoResult(count);//tempo result is $count value
            return json;//add label $count=n
        }
    },
    $top{
        String apply(ODataOperationExpression expression, String json) throws Exception {
            setTempoResult(json);
            return json;
        }
    },
    $skip{
        String apply(ODataOperationExpression expression, String json) throws Exception {
            setTempoResult(json);
            return json;
        }
    },
    $value{
        String apply(ODataOperationExpression expression, String json) throws Exception {
            String value = null;
            setTempoResult(value);
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

    private String tempoResult = null;

    /**
     * set tempo result of current apply
     * @param tempoResult
     */
    void setTempoResult(String tempoResult){
        this.tempoResult = tempoResult;
    }
    String getTempoResult(){
        return this.tempoResult;
    }
    public static ODataOperation getOperation(String string) throws Exception {
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
