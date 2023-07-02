package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.RC_Observer;
public interface RC_Observable {
    void addObserver(RC_Observer observer);
    void removeObserver(RC_Observer observer);
    void notifyObservers();
}
