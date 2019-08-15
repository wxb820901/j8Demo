package com.bill.rules.odata.customization.services.factory;

import com.bill.rules.odata.customization.services.FilterRule;
import com.bill.rules.odata.customization.services.filter.*;
import com.bill.rules.odata.customization.FilterOperator;
import com.bill.rules.odata.customization.ODataQueryExpression;

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
    private static final FilterRule likesService = new FilterLikesService();
    private static final FilterRule startsWithService = new FilterStartWithService();
    private static final FilterRule endsWithService = new FilterEndWithService();

    private static final FilterRule orService = new FilterORService();

    public FilterRule getRule(FilterOperator filter) {
        switch (filter) {
            case EQ:
                return eqService;
            case LT:
                return ltService;
            case LE:
                return leService;
            case GT:
                return gtService;
            case GE:
                return geService;
            case CONTAINS:
                return containsService;
            case LIKES:
                return likesService;
            case STARTSWITH:
                return startsWithService;
            case ENDSWITH:
                return endsWithService;
            case OR:
                return orService;
            default:
                return null;
        }
    }

    public static FilterRule getInstance(FilterOperator filter) {
        return new FilterRuleFactory().getRule(filter);
    }
}
