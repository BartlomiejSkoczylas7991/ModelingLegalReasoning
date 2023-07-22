package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.DecisionObserver;

public interface DecisionObservable {
    void addObserver(DecisionObserver observer);
    void removeObserver(DecisionObserver observer);
    void notifyObservers();
}
