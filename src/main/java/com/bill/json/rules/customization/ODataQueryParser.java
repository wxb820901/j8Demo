package com.bill.json.rules.customization;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.bill.json.rules.customization.ODataQuery.getQuery;
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
     * @param wholeQueryString
     * @return
     * @throws Exception
     */
    public static Map<ODataQuery, ODataQueryExpression> parseOperations(String wholeQueryString) throws Exception {
        ODataQueryExpression.resetExpressions();
        int prefixEndIndex = wholeQueryString.indexOf(PREFIX_END_WITH);
        if (prefixEndIndex == -1) {
            throw new Exception(" customization string should be like 'EntityName?$filter=expression&select=expression...'. incompatible customization pattern - " + wholeQueryString);
        }

        String prefix = wholeQueryString.substring(0, prefixEndIndex);//ceg/rs/syn/instrument/org/quote
        String queryString = wholeQueryString.substring(prefixEndIndex + 1);//$select=lable1 & $filter= label eq 1 or label eq 2 &...
        String[] uueryStrs = queryString.split(AND);//$select=lable1, $filter= label eq 1 or label eq 2, ...
        Map<ODataQuery, ODataQueryExpression> querys = new LinkedHashMap<>();
        for (String queryStr : uueryStrs) {
            //create ODataQuery
            ODataQuery oDataQuery = getQuery(queryStr).withPrefix(prefix);
            if (oDataQuery != null) {
                if(querys.get(oDataQuery) == null) {
                    //create expression by query
                    ODataQueryExpression expression = getOperationExpression(oDataQuery)
                            .withPrefix(prefix)
                            .withExression(
                                    "".equals(queryStr.replace(oDataQuery.name(), "").trim())
                                            ? ""
                                            : queryStr.replace(oDataQuery.name(), "").substring(1));
                    querys.put(oDataQuery, expression);
                }else{
                    querys.get(oDataQuery)
                            .withExression(
                                    "".equals(queryStr.replace(oDataQuery.name(), "").trim())
                                            ? ""
                                            : queryStr.replace(oDataQuery.name(), "").substring(1));
                }


            }
        }
        return querys;
    }

}
