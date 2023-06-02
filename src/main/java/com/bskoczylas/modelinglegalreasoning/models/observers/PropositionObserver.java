package com.bskoczylas.modelinglegalreasoning.models.observers;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Proposition;

public interface PropositionObserver {
    void updateProposition(Proposition proposition);
}
