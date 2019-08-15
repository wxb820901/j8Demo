package com.bill.rules.odata.customization;

import com.bill.rules.odata.customization.services.Rule;

import java.util.List;
import java.util.Map;

import static com.bill.rules.odata.customization.ODataQuery.*;
import static com.bill.rules.odata.customization.services.factory.QueryRuleFactory.getInstance;
import static com.bill.rules.odata.customization.util.ValidateUtil.validateExpression;

/**
 * customization expressions
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQuery , ODataQueryResult
 */
public enum ODataQueryExpression {
    COUNT_EXPRESSION(COUNT, getInstance(COUNT)),
    SKIP_EXPRESSION(SKIP, getInstance(SKIP)),
    TOP_EXPRESSION(TOP, getInstance(TOP)),
    ORDER_BY_EXPRESSION(ORDERBY, getInstance(ORDERBY)),
    SELECT_EXPRESSION(SELECT, getInstance(SELECT)),
    FILTER_EXPRESSION(FILTER, getInstance(FILTER)),
    JOIN_EXPRESSION(JOIN, getInstance(JOIN));


    public Map<String, String> apply(Map<String, String> jsonPathMapValue, String prefix, List<String> expresionStr) {
        validateExpression(this, jsonPathMapValue, prefix, expresionStr);
        return queryRule.apply(jsonPathMapValue, this, prefix, expresionStr);
    }

    private ODataQuery query;
    private Rule queryRule;

    ODataQueryExpression(ODataQuery query, Rule queryRule) {
        this.query = query;
        this.queryRule = queryRule;
    }

    public ODataQuery getQuery() {
        return query;
    }

    public static ODataQueryExpression getOperationExpression(ODataQuery oDataQuery) {
        if (oDataQuery != null) {
            for (ODataQueryExpression expression : values()) {
                if (expression.query == oDataQuery) {
                    return expression;
                }
            }
            throw new UnsupportedOperationException("not supported ODataQuery " + oDataQuery.name());
        }
        throw new UnsupportedOperationException("ODataQuery is null");
    }


}
