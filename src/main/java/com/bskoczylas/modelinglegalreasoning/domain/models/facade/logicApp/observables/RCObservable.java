package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.RCObserver;

public interface RCObservable {
    void addObserver(RCObserver observer);
    void removeObserver(RCObserver observer);
    void notifyObservers();
}
