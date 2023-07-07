package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.Scale_Observer;


public interface Scale_Observable {
    void addObserver(Scale_Observer observer);
    void removeObserver(Scale_Observer observer);
    void notifyObservers();
}
