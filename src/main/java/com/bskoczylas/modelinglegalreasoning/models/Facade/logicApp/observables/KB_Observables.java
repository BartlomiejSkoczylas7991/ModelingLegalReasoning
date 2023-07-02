package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.KB_Observer;

public interface KB_Observables {
    void addObserver(KB_Observer observer);
    void removeObserver(KB_Observer observer);
    void notifyObservers();
}
