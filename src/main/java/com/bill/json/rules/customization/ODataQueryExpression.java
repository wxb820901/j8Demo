package com.bill.json.rules.customization;

import com.bill.json.rules.customization.services.Rule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.bill.json.rules.customization.ODataQuery.*;
import static com.bill.json.rules.customization.services.factory.QueryRuleFactory.getInstance;

/**
 * customization expressions
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQuery , ODataQueryResult
 */
public enum ODataQueryExpression {
    countExpression($count, getInstance($count)),//for instance of $count $value
    skipExpression($skip, getInstance($skip)),//for instance of $skip=2 $top=1
    topExpression($top, getInstance($top)),
    orderExpression($orderBy, getInstance($orderBy)),//for instance of $select=$.data[name,id]
    selectExpression($select, getInstance($select)),
    filterExpression($filter, getInstance($filter)),
    valueExpression($value, null);//for instance of $filter=$.data[?(@.name=='xxyy')]

    public Map<String, String> apply(Map<String, String> jsonPathMapValue) throws Exception {
        return queryRule.apply(jsonPathMapValue, this);
    }

    private ODataQuery query;
    private Rule queryRule;

    ODataQueryExpression(ODataQuery query, Rule queryRule) {
        this.query = query;
        this.queryRule = queryRule;
    }

    //string after $query=
    private List<String> expressions;
    public ODataQueryExpression withExression(String expressionStr) {
        if(expressions == null){
            expressions = new ArrayList();//fix seq
        }
        if(!this.expressions.contains(expressionStr)) {
            this.expressions.add(expressionStr);
        }
        return this;
    }
    public List<String> getExpressions() {
        return this.expressions;
    }

    //string before $query_enum=
    private String prefix;
    public ODataQueryExpression withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }
    public String getPrefix() {
        return this.prefix;
    }



    //static
    public static void resetExpressions() {
        for (ODataQueryExpression expression : values()) {
            expression.expressions = null;
            expression.prefix = null;
        }
    }
    public static ODataQueryExpression getOperationExpression(ODataQuery oDataQuery) throws Exception {
        if (oDataQuery != null) {
            for (ODataQueryExpression expression : values()) {
                if (expression.query == oDataQuery) {
                    return expression;
                }
            }
        }
        throw new Exception("not supported ODataQuery " + oDataQuery.name());
    }


}
