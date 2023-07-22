package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PBCObserver;

public interface PBCObservable {
    void addObserver(PBCObserver observer);
    void removeObserver(PBCObserver observer);
    void notifyObservers();
}
