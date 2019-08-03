package com.bill.demo.json.util.rules;

import java.util.ArrayList;
import java.util.List;

import static com.bill.demo.json.util.rules.ODataOperation.*;

/**
 *  classify operation expression
 */
public enum ODataOperationExpression {
    nullExpression($count, $value),//for instance of $count $value
    intExpression($skip, $top), //for instance of $skip=2 $top=1
    listExpression($orderBy, $select),//for instance of $select=$.data[name,id]
    filterExpresion($filter);//for instance of $filter=$.data[?(@.name=='xxyy')]



    private List<ODataOperation> ODataOperations = new ArrayList<>();
    private String expression;
    ODataOperationExpression(ODataOperation... ops){
        for(ODataOperation ODataOperation : ops) {
            ODataOperations.add(ODataOperation);
        }

    }


    public String getExpression(){
        return this.expression;
    }
    ODataOperationExpression withExression(String expression){
        this.expression = expression;
        return this;
    }

    public static ODataOperationExpression getOperationExpression(ODataOperation ODataOperation, String expression) throws Exception {
        if(ODataOperation != null){
            for(ODataOperationExpression ODataOperationExpression : values()){
                if(ODataOperationExpression.ODataOperations.contains(ODataOperation)){
                    return ODataOperationExpression.withExression(expression.substring(1,expression.length()));
                }
            }
        }
        throw new Exception("not supported ODataOperation "+ ODataOperation.name());
    }


}
