package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule.ListRules;

public interface RuleObserver {
    void updateRule(ListRules listRules);
}
