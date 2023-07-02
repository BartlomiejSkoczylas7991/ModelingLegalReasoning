package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.PBC_Observer;

public interface PBC_Observable {
    void addObserver(PBC_Observer observer);
    void removeObserver(PBC_Observer observer);
    void notifyObservers();
}
