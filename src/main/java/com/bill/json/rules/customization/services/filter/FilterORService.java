package com.bill.json.rules.customization.services.filter;

import com.bill.json.rules.customization.services.NormalFilterRule;
import com.bill.json.rules.customization.services.factory.FilterRuleFactory;

import java.util.Map;

import static com.bill.json.rules.customization.FilterOperator.*;


public class FilterORService extends DefaultNormalFilterService {
    @Override
    public boolean compare(Map map, String filterExpression) throws Exception {

        for (String expression : filterExpression.split("or")) {
            if(((NormalFilterRule) FilterRuleFactory.getInstance(getFilterOperator(expression.trim()))).compare(map, expression.trim())){
                return true;
            }

        }
        return false;
    }


}

