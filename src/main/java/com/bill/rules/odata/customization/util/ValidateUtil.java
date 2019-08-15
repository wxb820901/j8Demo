package com.bill.rules.odata.customization.util;

import com.bill.rules.odata.customization.ODataQueryExpression;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.bill.rules.odata.customization.FilterOperator.getItemBeforeOperator;
import static com.bill.rules.odata.customization.util.JsonUtil.replaceArrayIndexWithStar;

/**
 * query and filter expression validation
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-14 10:30
 * @see
 */
public class ValidateUtil {
    private ValidateUtil() {

    }

    /**
     * validation implementation, invoke by
     *
     * @param expression
     * @param jsonPathMapValue
     * @param prefix
     * @param expresionStr
     */
    public static void validateExpression(ODataQueryExpression expression, Map<String, String> jsonPathMapValue, String prefix, List<String> expresionStr) {
        switch (expression) {
            case ORDER_BY_EXPRESSION: {
                if (expresionStr.size() != 1) {
                    throw new UnsupportedOperationException("orderby expression only support single expression: " + expresionStr);
                }
                throwExceptionIfNotMatchedJsonPath(jsonPathMapValue, null, expresionStr.get(0).replace("desc", "").replace("asc", "").trim());
            }
            break;

            case SELECT_EXPRESSION: {
                if (expresionStr.size() != 1) {
                    throw new UnsupportedOperationException("select expression only support single expression: " + expresionStr);
                }
                Arrays.asList(expresionStr.get(0).split(",")).stream().forEach(singleExpression ->
                        throwExceptionIfNotMatchedJsonPath(jsonPathMapValue, null, singleExpression.trim())
                );
            }
            break;

            case COUNT_EXPRESSION:
                if (expresionStr.size() != 1 || !"".equals(expresionStr.get(0).trim())) {
                    throw new UnsupportedOperationException("count expression only support single expression, and expression should be empty ");
                }
                break;

            case SKIP_EXPRESSION:
            case TOP_EXPRESSION:
                if (expresionStr.size() != 1 || !StringUtils.isNumeric(expresionStr.get(0).trim())) {
                    throw new UnsupportedOperationException("skip/top expression only support single expression, and expression should be integer  - " + expresionStr);
                }
                break;

            case FILTER_EXPRESSION:
                expresionStr.stream().forEach(filterExpression ->
                        Arrays.asList(filterExpression.split("or")).stream().forEach(filterSubExpression ->

                                throwExceptionIfNotMatchedJsonPath(jsonPathMapValue, prefix, getItemBeforeOperator(filterSubExpression))
                        )
                );
                break;

            case JOIN_EXPRESSION:
                expresionStr.stream().forEach(joinExpression -> {
                            if (joinExpression.split(" on ").length != 2) {
                                throw new UnsupportedOperationException("join expression without ' on ' ");
                            }
                            if (joinExpression.split(" on ")[1].split(" = ").length != 2) {
                                throw new UnsupportedOperationException("join expression without '  =  ' ");
                            }
                            /**
                             * here without json path is exist validation as below, since the the preview label case like RuleEngineJoinTest.testJoinAndJoin
                             * throwExceptionIfNotMatchedJsonPath(jsonPathMapValue, null, joinExpression.split(" on ")[1].split(" = ")[0].trim());
                             * throwExceptionIfNotMatchedJsonPath(jsonPathMapValue, null, joinExpression.split(" on ")[1].split(" = ")[1].trim());
                             */
                        }
                );
                break;

            default:
                throw new UnsupportedOperationException("");
        }
    }


    private static void throwExceptionIfNotMatchedJsonPath(Map<String, String> jsonPathMapValue, String prefix, String singleExpression) {
        boolean isFound = false;
        String withPrefix = prefix == null ? "$." : "$." + prefix + "[*].";
        for (String jsonpath : jsonPathMapValue.keySet()) {
            String singleExpressionWithPrefix = withPrefix + singleExpression;
            if (replaceArrayIndexWithStar(singleExpressionWithPrefix, jsonpath).startsWith(singleExpressionWithPrefix)) {
                isFound = true;
                break;
            }
        }
        if (!isFound) {
            throw new UnsupportedOperationException("there are not json path match " + singleExpression + " \n"
                    + "json path list below: \n" + jsonPathMapValue.keySet().stream().map(item -> item.replace("$.", "")).collect(Collectors.joining("\n")));
        }
    }
}
