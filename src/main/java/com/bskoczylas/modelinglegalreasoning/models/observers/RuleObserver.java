package com.bskoczylas.modelinglegalreasoning.models.observers;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.ListRules;

public interface RuleObserver {
    void updateRule(ListRules listRules);
}
