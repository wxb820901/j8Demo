package com.bill.json.rules.customization.services;


import java.util.Map;

/**
 * abstract rule for all query and filter service
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see ODataQueryExpression , ODataQueryResult
 */
public interface FilterRule{
    Map<String, String> apply(Map<String, String> originjsonPaths, String prefix, String filterExpression) throws Exception;
}
