package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.decision.DecisionVoting;

public interface DecisionObserver {
    public void update(DecisionVoting decision);
}
