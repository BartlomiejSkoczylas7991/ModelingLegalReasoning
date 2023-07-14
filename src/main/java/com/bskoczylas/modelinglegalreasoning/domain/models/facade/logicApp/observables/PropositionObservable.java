package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PropositionObserver;

public interface PropositionObservable {
    void addObserver(PropositionObserver observer);
    void removeObserver(PropositionObserver observer);
    void notifyObservers(ListProposition listProposition);
}
