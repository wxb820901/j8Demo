package com.bill.rules.odata.customization.services.filter;

import com.bill.rules.odata.customization.FilterOperator;
import com.bill.rules.odata.customization.services.factory.FilterRuleFactory;
import com.bill.rules.odata.customization.services.NormalFilterRule;

import java.util.Map;


public class FilterORService extends DefaultNormalFilterService {
    /**
     * get each or expression and trigger concrete filter logic
     * @param map
     * @param filterExpression
     * @return
     * @throws Exception
     */
    @Override
    public boolean compare(Map map, String filterExpression) {

        for (String expression : filterExpression.split("or")) {
            if(((NormalFilterRule) FilterRuleFactory.getInstance(FilterOperator.getFilterOperator(expression.trim()))).compare(map, expression.trim())){
                return true;
            }
        }
        return false;
    }


}

