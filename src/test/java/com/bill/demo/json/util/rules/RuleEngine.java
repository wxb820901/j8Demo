package com.bill.demo.json.util.rules;

import java.util.HashMap;
import java.util.Map;

import static com.bill.demo.json.util.rules.ODataOperationParser.parseOperations;

public class RuleEngine {

    private Map<ODataOperation, String> context = new HashMap<>();

    public Map<ODataOperation, String> getContext() {
        return context;
    }

    public void action(String json, String filterString) throws Exception {

        Map<ODataOperation, ODataOperationExpression> operations = parseOperations(filterString);


        ODataOperation previousOperation = null;
        String previousJsonResult = null;
        for(ODataOperation operation: operations.keySet()){

            if(previousOperation == null){//first step
                previousOperation = operation;
                previousJsonResult = operation.apply(operations.get(operation), json);
                context.put(operation, previousJsonResult);

            }else{
                previousJsonResult = operation.apply(operations.get(operation), previousOperation.getTempoResult());
                context.put(operation, previousJsonResult);
                previousOperation = operation;
            }


        }
    }
}
