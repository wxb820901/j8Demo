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
    public static final String PREFIX_END_WITH = "?";

    /**
     * parse operation string like 'EntityName?$filter=expression&select=expression...'
     * into Operation OperationExpression
     *
     * @param wholeOperationString
     * @return
     * @throws Exception
     */
    public static Map<ODataOperation, List<ODataOperationExpression>> parseOperations(String wholeOperationString) throws Exception {
        int prefixEndIndex = wholeOperationString.indexOf(PREFIX_END_WITH);
        if(prefixEndIndex == -1){
            throw new Exception(" operation string should be like 'EntityName?$filter=expression&select=expression...'. incompatible operation pattern - "+wholeOperationString);
        }

        String prefix = wholeOperationString.substring(0, wholeOperationString.indexOf(PREFIX_END_WITH));
        String operationString = wholeOperationString.substring(wholeOperationString.indexOf(PREFIX_END_WITH)+1);
        String[] operationStrs = operationString.split(AND);
        Map<ODataOperation, List<ODataOperationExpression>> operations = new HashMap<>();
        for(String operationStr: operationStrs){
            ODataOperation oDataOperation = getOperation(operationStr);
            if(oDataOperation != null){
                ODataOperationExpression expression = getOperationExpression(
                        oDataOperation,
                        prefix,
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

    /**
     * prepare json for operation
     *
     * @param json
     * @return
     */
    public static String parseOperationsInput(String json){
        return json;
    }


}
