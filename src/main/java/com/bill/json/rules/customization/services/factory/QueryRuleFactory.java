package com.bill.json.rules.customization.services.factory;

import com.bill.json.rules.customization.ODataQuery;
import com.bill.json.rules.customization.services.Rule;
import com.bill.json.rules.customization.services.query.*;


/**
 * factory class of query rules
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class QueryRuleFactory {

    private static final Rule queryCountService = new QueryCountService();
    private static final Rule queryOrderByService = new QueryOrderByService();
    private static final Rule querySelectService = new QuerySelectService();
    private static final Rule querySkipService = new QuerySkipService();
    private static final Rule queryTopService = new QueryTopService();
    private static final Rule queryFilterService = new QueryFilterService();

    public Rule getRule(ODataQuery query) {
        switch (query) {
            case $filter:
                return queryFilterService;
            case $select:
                return querySelectService;
            case $orderBy:
                return queryOrderByService;
            case $count:
                return queryCountService;
            case $top:
                return queryTopService;
            case $skip:
                return querySkipService;
            default:
                return null;
        }
    }

    public static Rule getInstance(ODataQuery query) {
        return new QueryRuleFactory().getRule(query);
    }
}
