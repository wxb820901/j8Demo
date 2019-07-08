package com.bill.demo.easyrulesplus;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import org.jeasy.rules.api.*;
import org.jeasy.rules.core.RuleBuilder;

import java.util.UUID;

public class RuleSetting {
    public static final String RULE_ID = "rule_id";
    public static final String RULE_RESULT = "rule_result";
    public static final String RULE_FILTER_CONTENT = "rule_filter_content";
    public static Rule generateRule(String ruleId, String[] actionLines){
        RuleBuilder ruleBuilder =  new RuleBuilder()
                .when(generateCandition(ruleId)) // is just a id
                .priority(0);

        for(String line : actionLines){

            ruleBuilder.then(generateAction(
                    content  -> {
                        Object document = Configuration.defaultConfiguration().jsonProvider().parse(content);
                        String result = JsonPath.read(document, line).toString();
                        System.out.println("==>"+result);
                        return result;//filter implement, filter by line
                    }
                )
            );
        }
        return ruleBuilder.build();
    }

    private static Condition generateCandition(String ruleId){
        return facts -> facts.get(RULE_ID).equals(ruleId);
    }

    private static Action generateAction(Filter filter){
        return facts -> {
            if(facts.get(RULE_FILTER_CONTENT)==null){
                throw new Exception("content is null");
            }else{
                String result = facts.get(RULE_FILTER_CONTENT);
                result = filter.filter(result);
                facts.put(RULE_RESULT, result);
                facts.put(RULE_FILTER_CONTENT, result);//for next filter

            }
        };
    }



}
