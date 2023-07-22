package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ConsortiumObserver;

public interface ConsortiumObservable {
    void addObserver(ConsortiumObserver observer);
    void removeObserver(ConsortiumObserver observer);
    void notifyObservers();
}
