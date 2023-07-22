package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PropositionObserver;

public interface PropositionObservable {
    void addObserver(PropositionObserver observer);
    void removeObserver(PropositionObserver observer);
    void notifyObservers(ListProposition listProposition);
}
