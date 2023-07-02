package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Proposition;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.PropositionObserver;

public interface PropositionObservable {
    void addObserver(PropositionObserver observer);
    void removeObserver(PropositionObserver observer);
    void notifyObservers(Proposition proposition);
}
