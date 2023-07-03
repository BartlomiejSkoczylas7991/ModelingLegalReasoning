package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Proposition.Proposition;

public interface PropositionObserver {
    void updateProposition(Proposition proposition);
}
