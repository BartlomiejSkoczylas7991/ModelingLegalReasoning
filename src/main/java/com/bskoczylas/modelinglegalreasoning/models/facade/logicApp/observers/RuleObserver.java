package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Rule.ListRules;

public interface RuleObserver {
    void updateRule(ListRules listRules);
}
