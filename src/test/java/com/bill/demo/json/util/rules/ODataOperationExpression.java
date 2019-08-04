package com.bill.demo.json.util.rules;

import java.util.ArrayList;
import java.util.Arrays;
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



    private List<ODataOperation> operations = new ArrayList<>();
    private String expression;
    ODataOperationExpression(ODataOperation... ops){
        Arrays.asList(ops).stream().forEach(oDataOperation -> operations.add(oDataOperation));
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
            for(ODataOperationExpression operationExpression : values()){
                if(operationExpression.operations.contains(ODataOperation)){
                    return operationExpression.withExression(expression);
                }
            }
        }
        throw new Exception("not supported ODataOperation "+ ODataOperation.name());
    }


}
