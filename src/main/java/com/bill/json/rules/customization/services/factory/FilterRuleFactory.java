package com.bill.json.rules.customization.services.factory;

import com.bill.json.rules.customization.FilterOperator;
import com.bill.json.rules.customization.ODataQueryExpression;
import com.bill.json.rules.customization.services.Rule;
import com.bill.json.rules.customization.services.filter.*;

/**
 * factory class of filter rules
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class FilterRuleFactory implements RuleAbstractFactory<FilterOperator> {

    private static final Rule eqService = new FilterEqService();
    private static final Rule ltService = new FilterLtService();
    private static final Rule leService = new FilterLeService();
    private static final Rule gtService = new FilterGtService();
    private static final Rule geService = new FilterGeService();
    private static final Rule containsService = new FilterContainsService();
    private static final Rule startWithService = new FilterStartWithService();
    private static final Rule endWithService = new FilterEndWithService();
    private static final Rule isExistService = new FilterIsExistService();

    public Rule getRule(FilterOperator filter) {
        switch (filter) {
            case eq:
                return eqService;
            case lt:
                return ltService;
            case le:
                return leService;
            case gt:
                return gtService;
            case ge:
                return geService;
            case contains:
                return containsService;
            case startWith:
                return startWithService;
            case endWith:
                return endWithService;
            case isExist:
                return isExistService;
            default:
                return null;
        }
    }

    public static Rule getInstance(FilterOperator filter) {
        return new FilterRuleFactory().getRule(filter);
    }
}
