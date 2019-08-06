package com.bill.json.rules.customization;

import com.bill.json.rules.customization.services.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.bill.json.rules.customization.ODataQuery.*;
import static com.bill.json.rules.customization.services.factory.QueryRuleFactory.getInstance;

/**
 * customization expression
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
    filterExpresion($filter, getInstance($filter)),
    valueExpression($value, null);//for instance of $filter=$.data[?(@.name=='xxyy')]

    public Map<String, String> apply(Map<String, String> jsonPathMapValue) throws Exception {
        return queryRule.apply(jsonPathMapValue, this);
    }

    private List<ODataQuery> operations = new ArrayList<>();
    private Rule queryRule;


    ODataQueryExpression(ODataQuery query, Rule queryRule) {
        Arrays.asList(query).stream().forEach(oDataOperation -> operations.add(oDataOperation));
        this.queryRule = queryRule;
    }

    //string after $query_enum=
    private String expression;

    public ODataQueryExpression withExression(String expression) {
        this.expression = expression;
        return this;
    }

    public String getExpression() {
        return this.expression;
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

    //only for $filter
    private List<FilterOperator> filterOperators = new ArrayList<>();

    public ODataQueryExpression withFilterOperators(List<FilterOperator> filterOperators) {
        this.filterOperators = filterOperators;
        return this;
    }

    public List<FilterOperator> getFilterOperators() {
        return this.filterOperators;
    }


    public static ODataQueryExpression getOperationExpression(ODataQuery oDataQuery) throws Exception {
        if (oDataQuery != null) {
            for (ODataQueryExpression operationExpression : values()) {
                if (operationExpression.operations.contains(oDataQuery)) {
                    return operationExpression;
                }
            }
        }
        throw new Exception("not supported ODataQuery " + oDataQuery.name());
    }


}
