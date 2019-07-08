package com.bill.demo.j8;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RuleBuilder;
import org.jeasy.rules.core.RulesEngineParameters;
import org.jeasy.rules.support.UnitRuleGroup;
import org.junit.Test;

public class EasyRulesTest {
    @Test
    public void test(){
        // create a rules engine
        RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstAppliedRule(true);
        RulesEngine fizzBuzzEngine = new DefaultRulesEngine(parameters);

        org.jeasy.rules.api.Rule zRule = new RuleBuilder()
//                .name("with0")
//                .description("if number with 0")
                .when(facts -> facts.get("number").toString().contains("0"))
                .then(facts -> System.out.print("It's with 0,"))
                .priority(0)
                .build();

        // create rules
        Rules rules = new Rules();
        rules.register(zRule);
        rules.register(new FizzRule());
        rules.register(new BuzzRule());
        rules.register(new EasyRulesTest.FizzBuzzRule(new EasyRulesTest.FizzRule(), new EasyRulesTest.BuzzRule()));
        rules.register(new NonFizzBuzzRule());



        // fire rules
        Facts facts = new Facts();
        for (int i = 1; i <= 100; i++) {
            facts.put("number", i);
            fizzBuzzEngine.fire(rules, facts);
            System.out.println();
        }

        System.out.println("=====>"+new Integer(101).toString().contains("0"));
    }

    public static class FizzBuzzRule extends UnitRuleGroup {

        public FizzBuzzRule(Object... rules) {
            for (Object rule : rules) {
                addRule(rule);
            }
        }

        @Override
        public int getPriority() {
            return 0;
        }
    }

    @Rule(priority = 1)
    public static class FizzRule {
        @Condition
        public boolean isFizz(@Fact("number") Integer number) {
            return number % 5 == 0;
        }

        @Action
        public void printFizz() {
            System.out.print("fizz");
        }
    }

    @Rule(priority = 2)
    public static class BuzzRule {
        @Condition
        public boolean isBuzz(@Fact("number") Integer number) {
            return number % 7 == 0;
        }

        @Action
        public void  printBuzz() {
            System.out.print("buzz");
        }
    }

    @Rule(priority = 3)
    public static class NonFizzBuzzRule {

        @Condition
        public boolean isNotFizzNorBuzz(@Fact("number") Integer number) {
            // can return true, because this is the latest rule to trigger according to
            // assigned priorities
            // and in which case, the number is not fizz nor buzz
            return number % 5 != 0 || number % 7 != 0;
        }

        @Action
        public void printInput(@Fact("number") Integer number) {
            System.out.print(number);
        }
    }
}
