package com.alibaba.cola.ruleengine;

import com.alibaba.cola.ruleengine.api.Facts;
import com.alibaba.cola.ruleengine.api.Rule;
import com.alibaba.cola.ruleengine.api.RuleEngine;
import com.alibaba.cola.ruleengine.api.Rules;
import com.alibaba.cola.ruleengine.core.CompositeRule;
import com.alibaba.cola.ruleengine.core.DefaultRule;
import com.alibaba.cola.ruleengine.core.DefaultRuleEngine;
import com.alibaba.cola.ruleengine.core.RuleBuilder;
import org.junit.Test;

public class RuleBuilderTest {

    @Test
    public void testFizzBuzz() {
        RuleEngine fizzBuzzEngine = new DefaultRuleEngine();

        // create rules
        Rules rules = new Rules();
        Rule fizzRule = new RuleBuilder()
                .name("fizzRule")
                .description("fizz rule when input times 3, output is Fizz")
                .priority(10)
                .when(facts -> (int) facts.get("number") % 3 == 0)
                .then(facts -> System.out.print("Fizz"))
                .build();

        Rule buzzRule = new RuleBuilder()
                .name("buzzRule")
                .description("buzz rule when input times 5, output is buzz")
                .priority(1)
                .when(facts -> (int) facts.get("number") % 5 == 0)
                .then(facts -> System.out.print("Buzz"))
                .build();


        Rule fizzBuzzRule = CompositeRule.allOf(fizzRule, buzzRule)
                .name("fizzBuzzRule")
                .priority(30);

        Rule defaultRule = new RuleBuilder()
                .name("defaultRule")
                .description("default rule, output number")
                .priority(40)
                .when(facts -> true)
                .then(facts -> System.out.print((int) facts.get("number")))
                .build();

        Rule rule = CompositeRule.anyOf(fizzBuzzRule, fizzRule, buzzRule, defaultRule)
                .name("anyRule");

        rules.register(rule);

        // fire rules
        Facts facts = new Facts();
        facts.put("number", 15);
        fizzBuzzEngine.fire(rules, facts);
    }

}