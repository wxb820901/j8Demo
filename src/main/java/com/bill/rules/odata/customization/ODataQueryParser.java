package com.bill.rules.odata.customization;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.bill.rules.odata.customization.ODataQuery.getQuery;

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

    private ODataQueryParser() {
    }

    /**
     * parse customization string like 'EntityName?$FILTER=expression&SELECT=expression...'
     * into Operation OperationExpression
     *
     * @param wholeQueryString
     * @return Map<String, Map < ODataQueryExpression, List < String>>>
     * Map<prefix, Map<ODataQueryExpression, List<expression string>>>
     * @throws Exception
     */
    public static Map<String, Map<ODataQueryExpression, List<String>>> parseOperations(String wholeQueryString) {
        int prefixEndIndex = wholeQueryString.indexOf(PREFIX_END_WITH);
        if (prefixEndIndex == -1) {
            throw new UnsupportedOperationException(" customization string should be like 'EntityName?select=expression&filter=expression...'. incompatible customization pattern - " + wholeQueryString);
        }

        String prefix = wholeQueryString.substring(0, prefixEndIndex);
        String queryString = wholeQueryString.substring(prefixEndIndex + 1);
        String[] uueryStrs = queryString.split(AND);
        Map<String, Map<ODataQueryExpression, List<String>>> querys = new LinkedHashMap<>();
        querys.put(prefix, new LinkedHashMap());
        Map<ODataQueryExpression, List<String>> expressionMap = querys.get(prefix);
        for (String queryStr : uueryStrs) {
            ODataQuery oDataQuery = getQuery(queryStr);
            ODataQueryExpression oDataQueryExpression = ODataQueryExpression.getOperationExpression(oDataQuery);
            expressionMap.get(oDataQueryExpression);
            if (expressionMap.get(oDataQueryExpression) == null) {
                expressionMap.put(oDataQueryExpression, new ArrayList());
            }
            expressionMap.get(oDataQueryExpression)
                    .add(
                            "".equals(queryStr.toLowerCase().replace(oDataQuery.name().toLowerCase(), "").trim())
                                    ? ""
                                    : queryStr.replace(oDataQuery.name().toLowerCase(), "").substring(1).trim()
                    );
        }
        return querys;
    }

}
