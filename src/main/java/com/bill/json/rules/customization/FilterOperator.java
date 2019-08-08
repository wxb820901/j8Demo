package com.bill.json.rules.customization;

import com.bill.json.rules.customization.services.Rule;

import java.util.Map;

/**
 * filter expression operator
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
     * and only support 'or' joint such like 'prefix?$filter=label1 eq 1 or abel1 eq 2'
     * since 'and' joint can be implemented as 'prefix?$filter=label1 eq 1&$filter=abel1 eq 2'
     */

    or,
    eq,
    lt,
    gt,
    ge,
    le,
    contains,
    startWith,
    endWith,
    isExist; //is prefix list element map has labels

    public Map<String, String> apply(Map<String, String> jsonpathMapValue) throws Exception{
        return getRule().apply(jsonpathMapValue, getExpression());
    }



    private Rule rule;
    public FilterOperator withRule(Rule rule){
        this.rule = rule;
        return this;
    }
    public Rule getRule(){
        return this.rule;
    }

    private ODataQueryExpression expression;
    public FilterOperator withExpression(ODataQueryExpression expression){
        this.expression = expression;
        return this;
    }
    public ODataQueryExpression getExpression(){
        return this.expression;
    }

    public static FilterOperator getFilterOperator(String expression) throws Exception {
        for (FilterOperator operator : values()) {
            if (expression.contains(operator.name())) {
                return operator;
            }
        }
        throw new Exception("not supported FilterOperator " + expression);
    }

    public static String getItemBeforeOperator(String expression){
        if(!expression.contains(or.name())) {
            for (FilterOperator operator : values()) {
                if (expression.contains(operator.name())) {
                    return expression.split(operator.name())[0];
                }
            }
        }
        return "";
    }
    public static String getItemAfterOperator(String expression){
        if(!expression.contains(or.name())) {
            for (FilterOperator operator : values()) {
                if (expression.contains(operator.name())) {
                    return expression.split(operator.name()).length == 2 ? expression.split(operator.name())[1] : "";
                }
            }
        }
        return "";
    }

}
