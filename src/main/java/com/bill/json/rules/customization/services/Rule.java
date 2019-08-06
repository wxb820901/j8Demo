package com.bill.json.rules.customization.services;

import com.bill.json.rules.customization.ODataQueryExpression;


import java.util.Map;

/**
 * abstract rule for all query and filter service
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public interface Rule {
    Map<String, String> apply(Map<String, String> originjsonPaths, ODataQueryExpression countExpression) throws Exception;
}
