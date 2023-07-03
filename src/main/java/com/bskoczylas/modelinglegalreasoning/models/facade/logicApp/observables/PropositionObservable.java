package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.PropositionObserver;

public interface PropositionObservable {
    void addObserver(PropositionObserver observer);
    void removeObserver(PropositionObserver observer);
    void notifyObservers(Proposition proposition);
}
