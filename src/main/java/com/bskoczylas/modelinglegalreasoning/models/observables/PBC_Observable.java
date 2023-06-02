package com.bskoczylas.modelinglegalreasoning.models.observables;

import com.bskoczylas.modelinglegalreasoning.models.observers.PBC_Observer;

public interface PBC_Observable {
    void addObserver(PBC_Observer observer);
    void removeObserver(PBC_Observer observer);
    void notifyObservers();
}
