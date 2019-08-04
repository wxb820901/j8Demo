package com.bill.demo.json.util.rules;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.bill.demo.json.util.rules.ODataOperation.*;

/**
 * wrapper result as required
 */
public enum ODataOperationResult {
    jsonResult($filter, $select, $orderBy, $count, $top, $skip, $expand){
        public java.lang.String wrapResult(Object result){
            return result.toString();
        }
    },
    valueResult($value){
        public java.lang.String wrapResult(Object result){
            return result.toString();
        }
    };

    private List<ODataOperation> operations = new ArrayList<>();
    ODataOperationResult(ODataOperation... ops){
        Arrays.asList(ops).stream().forEach(oDataOperation -> operations.add(oDataOperation));
    }
    abstract java.lang.String wrapResult(Object result);

    public static ODataOperationResult getODataOperationResult(ODataOperation ODataOperation) throws Exception {
        if(ODataOperation != null){
            for(ODataOperationResult result : values()){
                if(result.operations.contains(ODataOperation)){
                    return result;
                }
            }
        }
        throw new Exception("not supported ODataOperation "+ ODataOperation.name());
    }
}
