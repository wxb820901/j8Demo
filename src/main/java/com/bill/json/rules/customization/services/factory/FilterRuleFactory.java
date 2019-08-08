package com.bill.json.rules.customization.services.factory;

import com.bill.json.rules.customization.FilterOperator;
import com.bill.json.rules.customization.services.FilterRule;
import com.bill.json.rules.customization.services.filter.*;

/**
 * factory class of filter rules
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public class FilterRuleFactory {

    private static final FilterRule eqService = new FilterEqService();
    private static final FilterRule ltService = new FilterLtService();
    private static final FilterRule leService = new FilterLeService();
    private static final FilterRule gtService = new FilterGtService();
    private static final FilterRule geService = new FilterGeService();
    private static final FilterRule containsService = new FilterContainsService();
    private static final FilterRule startWithService = new FilterStartWithService();
    private static final FilterRule endWithService = new FilterEndWithService();
    private static final FilterRule isExistService = new FilterIsExistService();
    private static final FilterRule orService = new FilterORService();

    public FilterRule getRule(FilterOperator filter) {
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
            case or:
                return orService;
            default:
                return null;
        }
    }

    public static FilterRule getInstance(FilterOperator filter) {
        return new FilterRuleFactory().getRule(filter);
    }
}
