package com.bill.json.rules.customization;

import com.bill.json.rules.customization.services.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bill.json.rules.customization.services.factory.FilterRuleFactory.getInstance;

/**
 * filter expression operator
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public enum FilterOperator {
    eq,
    lt,
    gt,
    ge,
    le,
    contains,
    startWith,
    endWith,
    isExist; //isprefix list element map has labels

    public Map<String, String> apply(Map<String, String> jsonpathMapValue) throws Exception{
        return getRule().apply(jsonpathMapValue,getExpression());
    }

    private String itemBeforeOperator;
    private String itemAfterOperator;

    public FilterOperator withItemBeforeOperator(String itemBeforeOperator){
        this.itemBeforeOperator = itemBeforeOperator;
        return this;
    }
    public String getItemBeforeOperator(){
        return this.itemBeforeOperator;
    }
    public String getItemAfterOperator(){
        return this.itemAfterOperator;
    }
    public FilterOperator withItemAfterOperator(String itemAfterOperator){
        this.itemAfterOperator = itemAfterOperator;
        return this;
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

    //tempo sut support single customization, multi-customization will with and or not .. logic operator
    public static FilterOperator getFilterOperator( ODataQueryExpression expression){
        for(FilterOperator operator: values()){
            List itemAroundOperator = Arrays.asList(expression.getExpression().split(operator.name()));
            if(itemAroundOperator.size() == 2) {
                operator.withItemBeforeOperator((String) itemAroundOperator.get(0));
                operator.withItemAfterOperator((String) itemAroundOperator.get(1));
                operator.withRule(getInstance(operator));
                operator.withExpression(expression);
                return operator;
            }
        }
        return null;
    }

}
