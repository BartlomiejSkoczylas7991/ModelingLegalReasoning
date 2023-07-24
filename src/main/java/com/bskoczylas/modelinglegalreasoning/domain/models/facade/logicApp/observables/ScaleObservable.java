package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ScaleObserver;


public interface ScaleObservable {
    void addObserver(ScaleObserver observer);
    void removeObserver(ScaleObserver observer);
    void notifyObservers();
}
