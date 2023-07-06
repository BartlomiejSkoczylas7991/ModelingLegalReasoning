package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.Consortium_Observer;

public interface Consortium_Observable {
    void addObserver(Consortium_Observer observer);
    void removeObserver(Consortium_Observer observer);
    void notifyObservers();
}
