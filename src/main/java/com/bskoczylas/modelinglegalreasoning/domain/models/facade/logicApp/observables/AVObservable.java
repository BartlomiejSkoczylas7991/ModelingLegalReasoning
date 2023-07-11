package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AVObserver;

public interface AVObservable {
    void addAVObserver(AVObserver observer);
    void removeAVObserver(AVObserver observer);
    void notifyAVObservers();
}
