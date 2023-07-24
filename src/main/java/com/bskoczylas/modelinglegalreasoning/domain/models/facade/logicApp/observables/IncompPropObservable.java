package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.IncompPropObserver;

public interface IncompPropObservable {
    void addObserver(IncompPropObserver observer);
    void removeObserver(IncompPropObserver observer);
    void notifyObservers();
}
