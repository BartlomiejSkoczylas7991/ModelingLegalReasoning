package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.Consortium_Observer;

public interface Consortium_Observable {
    void addObserver(Consortium_Observer observer);
    void removeObserver(Consortium_Observer observer);
    void notifyObservers();
}
