package com.bill.json.rules.customization.services;


import java.util.Map;

public interface NormalFilterRule extends FilterRule{
    boolean compare(Map map, String expression) throws Exception;
}
