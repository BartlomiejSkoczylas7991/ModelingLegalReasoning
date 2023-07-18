package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.ListProposition;

public interface PropositionObserver {
    void updateProposition(ListProposition listProposition);
}
