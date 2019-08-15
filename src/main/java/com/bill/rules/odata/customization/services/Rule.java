package com.bill.rules.odata.customization.services;

import com.bill.rules.odata.customization.ODataQueryExpression;

import java.util.List;
import java.util.Map;

/**
 * abstract rule for all query and FILTER service
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public interface Rule {
    Map<String, String> apply(Map<String, String> originjsonPaths, ODataQueryExpression expression, String prefix, List<String> expressionStrs);
}
