package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.KBObserver;

public interface KBObservables {
    void addObserver(KBObserver observer);
    void removeObserver(KBObserver observer);
    void notifyObservers();
}
