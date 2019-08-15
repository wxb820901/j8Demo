package com.bill.rules.odata.customization;

import com.bill.rules.odata.customization.services.factory.FilterRuleFactory;

import java.util.Map;

/**
 * FILTER expression operator
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public enum FilterOperator {
    /**
     * all FilterOperator at level of ODataQuery are filtersContainer,
     * and fill other FilterOperator with method withSubFilters
     * and only support 'or' joint such like 'prefix?filter=label1 eq 1 or abel1 eq 2'
     * since 'and' joint can be implemented as 'prefix?filter=label1 eq 1&filter=abel1 eq 2'
     */

    OR,
    EQ,
    LT,
    GT,
    GE,
    LE,
    CONTAINS,
    LIKES, //match value with *
    STARTSWITH,
    ENDSWITH;

    public Map<String, String> apply(Map<String, String> jsonpathMapValue, String prefix, String filterExpression) {
        return FilterRuleFactory.getInstance(this).apply(jsonpathMapValue, prefix, filterExpression);
    }

    public static FilterOperator getFilterOperator(String expression){
        for (FilterOperator operator : values()) {
            if (expression.toLowerCase().contains(" " + operator.name().toLowerCase() + " ")) {
                return operator;
            }
        }
        throw new UnsupportedOperationException("not supported FilterOperator " + expression);
    }

    public static String getItemBeforeOperator(String expression) {
        if (!expression.toLowerCase().contains(" "+OR.name().toLowerCase()+" ")) {
            for (FilterOperator operator : values()) {
                if (expression.toLowerCase().contains(" " + operator.name().toLowerCase() + " ")) {
                    return expression.split(" " + operator.name().toLowerCase() + " ")[0].trim();
                }
            }
        }
        return "";
    }

    public static String getItemAfterOperator(String expression) {
        if (!expression.toLowerCase().contains(" "+OR.name().toLowerCase()+" ")) {
            for (FilterOperator operator : values()) {
                if (expression.toLowerCase().contains(" " + operator.name().toLowerCase() + " ")) {
                    return expression.split(" " + operator.name().toLowerCase() + " ").length == 2 ? expression.split(" " + operator.name().toLowerCase() + " ")[1].trim() : "";
                }
            }
        }
        return "";
    }

}
