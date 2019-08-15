package com.bill.rules.odata.customization.services;

import java.util.Map;

/**
 * abstract rule for all filter service
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see NormalFilterRule
 */
public interface FilterRule {
    Map<String, String> apply(Map<String, String> originjsonPaths, String prefix, String filterExpression);
}
