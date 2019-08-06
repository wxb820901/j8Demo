package com.bill.json.rules.customization;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.bill.json.rules.customization.FilterOperator.getFilterOperator;
import static com.bill.json.rules.customization.ODataQuery.$filter;
import static com.bill.json.rules.customization.ODataQueryExpression.getOperationExpression;


/**
 * parse query string into ODataQuery and ODataQueryExpression
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQuery , ODataQueryResult
 */
public class ODataQueryParser {

    public static final String AND = "&";
    public static final String PREFIX_END_WITH = "?";

    /**
     * parse customization string like 'EntityName?$filter=expression&select=expression...'
     * into Operation OperationExpression
     *
     * @param wholeOperationString
     * @return
     * @throws Exception
     */
    public static Map<ODataQuery, List<ODataQueryExpression>> parseOperations(String wholeOperationString) throws Exception {
        int prefixEndIndex = wholeOperationString.indexOf(PREFIX_END_WITH);
        if (prefixEndIndex == -1) {
            throw new Exception(" customization string should be like 'EntityName?$filter=expression&select=expression...'. incompatible customization pattern - " + wholeOperationString);
        }

        String prefix = wholeOperationString.substring(0, prefixEndIndex);
        String operationString = wholeOperationString.substring(prefixEndIndex + 1);
        String[] operationStrs = operationString.split(AND);
        Map<ODataQuery, List<ODataQueryExpression>> operations = new LinkedHashMap<>();
        for (String operationStr : operationStrs) {
            ODataQuery oDataQuery = ODataQuery.getQuery(operationStr).withPrefix(prefix);
            if (oDataQuery != null) {


                ODataQueryExpression expression = getOperationExpression(oDataQuery)
                        .withPrefix(prefix)
                        .withExression(
                                "".equals(operationStr.replace(oDataQuery.name(), "").trim())
                                        ? ""
                                        : operationStr.replace(oDataQuery.name(), "").substring(1));
                if (operations.get(oDataQuery) != null) {
                    operations.get(oDataQuery).add(expression);
                } else {
                    operations.put(oDataQuery, Arrays.asList(expression));
                }

                if (oDataQuery == $filter) {
                    expression.withFilterOperators(
                            Arrays.asList(
                                    getFilterOperator(
                                            expression
                                    )
                            )
                    );
                }

            }
        }
        return operations;
    }

}
