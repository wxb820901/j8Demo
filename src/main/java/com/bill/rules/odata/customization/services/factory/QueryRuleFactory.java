package com.bill.rules.odata.customization.services.factory;

import com.bill.rules.odata.customization.ODataQuery;
import com.bill.rules.odata.customization.ODataQueryExpression;
import com.bill.rules.odata.customization.services.Rule;
import com.bill.rules.odata.customization.services.query.*;

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
    private static final Rule queryJoinService = new QueryJoinService();

    public Rule getRule(ODataQuery query) {
        switch (query) {
            case FILTER:
                return queryFilterService;
            case SELECT:
                return querySelectService;
            case ORDERBY:
                return queryOrderByService;
            case COUNT:
                return queryCountService;
            case TOP:
                return queryTopService;
            case SKIP:
                return querySkipService;
            case JOIN:
                return queryJoinService;
            default:
                return null;
        }
    }

    public static Rule getInstance(ODataQuery query) {
        return new QueryRuleFactory().getRule(query);
    }
}
