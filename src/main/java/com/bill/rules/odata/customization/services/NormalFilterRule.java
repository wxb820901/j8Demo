package com.bill.rules.odata.customization.services;


import java.util.Map;

/**
 * abstract rule for 1 FILTER service
 *
 * @author Bill Wang
 * @throws
 * @created 2019-08-05 10:30
 * @see FilterRule
 */
public interface NormalFilterRule extends FilterRule{
    boolean compare(Map map, String expression);
}
