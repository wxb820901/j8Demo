package com.bill.json.rules.customization;

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
     *
     * @param json like below  data source name mapping result of fetching
     *                     "{\n" +
     *                     "    \"ceg\":[\n" +
     *                     "         {\n" +
     *                     "             \"label1\":\"2\",\n" +
     *                     "             \"label2\":\"prefix1[0].label2.value\",\n" +
     *                     "             \"label3\":\"asas\"\n" +
     *                     "         },\n" +
     *                     "         {\n" +
     *                     "             \"label1\":\"1\",\n" +
     *                     "             \"label2\":\"prefix1[1].label2.value\",\n" +
     *                     "             \"label3\":\"fgfg\"\n" +
     *                     "         }\n" +
     *                     "    ],\n" +
     *                     "    \"rs\":[\n" +
     *                     "          {\n" +
     *                     "              \"label1\":\"asas\",\n" +
     *                     "              \"label2\":\"dfdf\",\n" +
     *                     "              \"label3\":\"fgfg\"\n" +
     *                     "          },\n" +
     *                     "          {\n" +
     *                     "              \"label1\":\"sdsdsdsd\",\n" +
     *                     "              \"label2\":\"dfdf\",\n" +
     *                     "              \"label3\":\"fgfg\"\n" +
     *                     "          }\n" +
     *                     "    ],\n" +
     *                     "    \"instrument\":[\n" +
     *                     "          {\n" +
     *                     "              \"label1\":\"asas\",\n" +
     *                     "              \"label2\":\"dfdf\",\n" +
     *                     "              \"label3\":\"fgfg\"\n" +
     *                     "          },\n" +
     *                     "          {\n" +
     *                     "              \"label1\":\"sdsdsdsd\",\n" +
     *                     "              \"label2\":\"dfdf\",\n" +
     *                     "              \"label3\":\"fgfg\"\n" +
     *                     "          }\n" +
     *                     "    ],\n" +
     *                     "    \"quote\":[\n" +
     *                     "          {\n" +
     *                     "              \"label1\":\"asas\",\n" +
     *                     "              \"label2\":\"dfdf\",\n" +
     *                     "              \"label3\":\"fgfg\"\n" +
     *                     "          },\n" +
     *                     "          {\n" +
     *                     "              \"label1\":\"sdsdsdsd\",\n" +
     *                     "              \"label2\":\"dfdf\",\n" +
     *                     "              \"label3\":\"fgfg\"\n" +
     *                     "          }\n" +
     *                     "    ],\n" +
     *                     "    \"org\":[\n" +
     *                     "          {\n" +
     *                     "              \"label1\":\"asas\",\n" +
     *                     "              \"label2\":\"dfdf\",\n" +
     *                     "              \"label3\":\"fgfg\"\n" +
     *                     "          },\n" +
     *                     "          {\n" +
     *                     "              \"label1\":\"sdsdsdsd\",\n" +
     *                     "              \"label2\":\"dfdf\",\n" +
     *                     "              \"label3\":\"fgfg\"\n" +
     *                     "          }\n" +
     *                     "    ]\n" +
     *                     "}"
     *
     * @param queryString like below
     *      ceg?$count
     *      ceg?$value=label
     *      rs?$skip=2
     *      rs?$top=1
     *      ceg?$orderBy=label1
     *      ceg?$select=label1, label2
     *      ceg?$filter= label1 eq 1
     *
     * @return
     * @throws Exception
     */
    public String action(String json, String queryString) throws Exception {
        Map<ODataQuery, List<ODataQueryExpression>> operations = ODataQueryParser.parseOperations(queryString);
        String tempJsonResult = json;
        for (ODataQuery operation : operations.keySet()) {
            for (ODataQueryExpression expression : operations.get(operation)) {
                tempJsonResult = operation.apply(expression, tempJsonResult);
            }
        }
        return tempJsonResult;

    }
}
