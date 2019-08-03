package com.bill.demo.json.util.rules;

import java.util.HashMap;
import java.util.Map;

import static com.bill.demo.json.util.rules.ODataOperation.getOperation;
import static com.bill.demo.json.util.rules.ODataOperationExpression.getOperationExpression;

public class ODataOperationParser {

    public static final String AND = "&";
    public static Map<ODataOperation, ODataOperationExpression> parseOperations(String operationString) throws Exception {
        String[] operationStrs = operationString.split(AND);
        Map<ODataOperation, ODataOperationExpression> operations = new HashMap<>();
        for(String operationStr: operationStrs){
            ODataOperation oDataOperation = getOperation(operationStr);

            if(oDataOperation != null){
                if(operations.get(oDataOperation) != null){
                    throw new Exception("duplicated ODataOperation");
                }
                operations.put(oDataOperation, getOperationExpression(
                        oDataOperation,
                        operationStr.replace(oDataOperation.name(),"")));
            }
        }
        return operations;
    }


}
