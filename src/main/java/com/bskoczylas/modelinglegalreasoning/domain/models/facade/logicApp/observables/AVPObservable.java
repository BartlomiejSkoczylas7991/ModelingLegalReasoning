package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AVPObserver;

public interface AVPObservable {
    void addObserver(AVPObserver observer);
    void removeObserver(AVPObserver observer);
    void notifyObservers();
}
