package com.bill.rules.odata.customization;

import java.util.List;
import java.util.Map;

/**
 * endpoint of current OData implementation
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQuery
 */
public class RuleEngine {
    /**
     * @param json        like below  data source name mapping result of fetching
     *                    "{\n" +
     *                    "    \"table1\":[\n" +
     *                    "         {\n" +
     *                    "             \"label1\":\"2\",\n" +
     *                    "             \"label2\":\"prefix1[0].label2.value\",\n" +
     *                    "             \"label3\":\"asas\"\n" +
     *                    "         },\n" +
     *                    "         {\n" +
     *                    "             \"label1\":\"1\",\n" +
     *                    "             \"label2\":\"prefix1[1].label2.value\",\n" +
     *                    "             \"label3\":\"fgfg\"\n" +
     *                    "         }\n" +
     *                    "    ],\n" +
     *                    "    \"table2\":[\n" +
     *                    "          {\n" +
     *                    "              \"label1\":\"asas\",\n" +
     *                    "              \"label2\":\"dfdf\",\n" +
     *                    "              \"label3\":\"fgfg\"\n" +
     *                    "          },\n" +
     *                    "          {\n" +
     *                    "              \"label1\":\"sdsdsdsd\",\n" +
     *                    "              \"label2\":\"dfdf\",\n" +
     *                    "              \"label3\":\"fgfg\"\n" +
     *                    "          }\n" +
     *                    "    ],\n" +
     *                    "    \"instrument\":[\n" +
     *                    "          {\n" +
     *                    "              \"label1\":\"asas\",\n" +
     *                    "              \"label2\":\"dfdf\",\n" +
     *                    "              \"label3\":\"fgfg\"\n" +
     *                    "          },\n" +
     *                    "          {\n" +
     *                    "              \"label1\":\"sdsdsdsd\",\n" +
     *                    "              \"label2\":\"dfdf\",\n" +
     *                    "              \"label3\":\"fgfg\"\n" +
     *                    "          }\n" +
     *                    "    ],\n" +
     *                    "    \"quote\":[\n" +
     *                    "          {\n" +
     *                    "              \"label1\":\"asas\",\n" +
     *                    "              \"label2\":\"dfdf\",\n" +
     *                    "              \"label3\":\"fgfg\"\n" +
     *                    "          },\n" +
     *                    "          {\n" +
     *                    "              \"label1\":\"sdsdsdsd\",\n" +
     *                    "              \"label2\":\"dfdf\",\n" +
     *                    "              \"label3\":\"fgfg\"\n" +
     *                    "          }\n" +
     *                    "    ],\n" +
     *                    "    \"org\":[\n" +
     *                    "          {\n" +
     *                    "              \"label1\":\"asas\",\n" +
     *                    "              \"label2\":\"dfdf\",\n" +
     *                    "              \"label3\":\"fgfg\"\n" +
     *                    "          },\n" +
     *                    "          {\n" +
     *                    "              \"label1\":\"sdsdsdsd\",\n" +
     *                    "              \"label2\":\"dfdf\",\n" +
     *                    "              \"label3\":\"fgfg\"\n" +
     *                    "          }\n" +
     *                    "    ]\n" +
     *                    "}"
     * @param queryString like below
     *                    table1?count
     *                    table2?skip=2
     *                    table2?top=1
     *                    table1?orderby=table1[*].label1
     *                    table1?select=table1[*].label1, table1[*].label2
     *                    table1?join=table2 on table2[*].label1 = table1[*].label2
     *                    table1?filter=label1 eq 1 or label1 eq 2
     * @return
     * @throws Exception
     */
    public String action(String json, String queryString){
        Map<String, Map<ODataQueryExpression, List<String>>> querys = ODataQueryParser.parseOperations(queryString);
        String prefix = querys.keySet().iterator().next();
        Map<ODataQueryExpression, List<String>> expressionMap = querys.get(prefix);
        String tempJsonResult = json;
        for (Map.Entry<ODataQueryExpression, List<String>> expression : expressionMap.entrySet()) {
            ODataQuery query = expression.getKey().getQuery();
            tempJsonResult = query.apply(expression.getKey(), tempJsonResult, prefix, expression.getValue());
        }
        return tempJsonResult;

    }
}
