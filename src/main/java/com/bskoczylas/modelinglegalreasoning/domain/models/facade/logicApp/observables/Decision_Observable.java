package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.Decision_Observer;

public interface Decision_Observable {
    void addObserver(Decision_Observer observer);
    void removeObserver(Decision_Observer observer);
    void notifyObservers();
}
