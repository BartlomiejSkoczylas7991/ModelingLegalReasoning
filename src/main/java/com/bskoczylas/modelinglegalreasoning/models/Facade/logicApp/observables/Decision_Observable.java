package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.Decision_Observer;

public interface Decision_Observable {
    void addObserver(Decision_Observer observer);
    void removeObserver(Decision_Observer observer);
    void notifyObservers();
}
