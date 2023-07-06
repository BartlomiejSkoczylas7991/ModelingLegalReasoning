package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PBC_Observer;

public interface PBC_Observable {
    void addObserver(PBC_Observer observer);
    void removeObserver(PBC_Observer observer);
    void notifyObservers();
}
