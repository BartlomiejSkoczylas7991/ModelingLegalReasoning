package com.bskoczylas.modelinglegalreasoning.models.observables;

import com.bskoczylas.modelinglegalreasoning.models.observers.RC_Observer;
public interface RC_Observable {
    void addObserver(RC_Observer observer);
    void removeObserver(RC_Observer observer);
    void notifyObservers();
}
