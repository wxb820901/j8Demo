package com.bill.demo.json.util.rules;

/**
 * wrapper result as required
 */
public enum ODataOperationResult {
    jsonResult{
        public String getResult(Object result){
            return result.toString();
        }
    },
    valueResult{
        public String getResult(Object result){
            return result.toString();
        }
    };

    abstract String getResult(Object result);
}
