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

    public String action(String json, String filterString) throws Exception {
        Map<ODataOperation, List<ODataOperationExpression>> operations = parseOperations(filterString);
        String tempJsonResult = json;
        for(ODataOperation operation: operations.keySet()) {
            for(ODataOperationExpression expression: operations.get(operation)) {
                tempJsonResult = operation.apply(expression, tempJsonResult);
            }
        }
        return tempJsonResult;

    }
}
