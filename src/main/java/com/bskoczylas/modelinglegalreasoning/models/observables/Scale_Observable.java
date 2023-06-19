package com.bskoczylas.modelinglegalreasoning.models.observables;

import com.bskoczylas.modelinglegalreasoning.models.observers.Scale_Observer;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Scale;


public interface Scale_Observable {
    void addObserver(Scale_Observer observer);
    void removeObserver(Scale_Observer observer);
    void notifyObservers();
}
