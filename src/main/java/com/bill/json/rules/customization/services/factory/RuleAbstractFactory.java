package com.bill.json.rules.customization.services.factory;


import com.bill.json.rules.customization.services.Rule;

/**
 * abstract factory class of rules
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public interface RuleAbstractFactory<T> {
    Rule getRule(T queryOrFilter);
}
