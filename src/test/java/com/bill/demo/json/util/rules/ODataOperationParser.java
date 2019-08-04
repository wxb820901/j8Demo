package com.bill.demo.json.util.rules;

import javax.management.OperationsException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bill.demo.json.util.rules.ODataOperation.getOperation;
import static com.bill.demo.json.util.rules.ODataOperationExpression.getOperationExpression;

public class ODataOperationParser {

    public static final String AND = "&";
    public static Map<ODataOperation, List<ODataOperationExpression>> parseOperations(String operationString) throws Exception {
        String[] operationStrs = operationString.split(AND);
        Map<ODataOperation, List<ODataOperationExpression>> operations = new HashMap<>();
        for(String operationStr: operationStrs){
            ODataOperation oDataOperation = getOperation(operationStr);

            if(oDataOperation != null){
                ODataOperationExpression expression = getOperationExpression(
                        oDataOperation,
                        operationStr.replace(oDataOperation.name(),"").substring(1)
                );
                if(operations.get(oDataOperation) != null){
                    operations.get(oDataOperation).add(expression);
                }else{
                    operations.put(oDataOperation,Arrays.asList(expression));
                }
            }
        }
        return operations;
    }


}
