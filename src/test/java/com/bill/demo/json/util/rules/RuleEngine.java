package com.bill.demo.json.util.rules;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bill.demo.json.util.rules.ODataOperationParser.parseOperations;

public class RuleEngine {

    private Map<ODataOperation, ODataOperationResult> context = new HashMap<>();

    public Map<ODataOperation, ODataOperationResult> getContext() {
        return context;
    }

    public String action(String json, String queryString) throws Exception {
        Map<ODataOperation, List<ODataOperationExpression>> operations = parseOperations(queryString);
        String tempJsonResult = json;
        for(ODataOperation operation: operations.keySet()) {
            for(ODataOperationExpression expression: operations.get(operation)) {
                tempJsonResult = operation.apply(expression, tempJsonResult);
            }
        }
        return tempJsonResult;

    }
}
