package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.IncompProp_Observer;

public interface IncompProp_Observable {
    void addObserver(IncompProp_Observer observer);
    void removeObserver(IncompProp_Observer observer);
    void notifyObservers();
}
