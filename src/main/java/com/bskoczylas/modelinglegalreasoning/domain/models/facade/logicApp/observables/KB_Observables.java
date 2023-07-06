package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.KB_Observer;

public interface KB_Observables {
    void addObserver(KB_Observer observer);
    void removeObserver(KB_Observer observer);
    void notifyObservers();
}
