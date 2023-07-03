package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.Consortium_Observer;

public interface Consortium_Observable {
    void addObserver(Consortium_Observer observer);
    void removeObserver(Consortium_Observer observer);
    void notifyObservers();
}
