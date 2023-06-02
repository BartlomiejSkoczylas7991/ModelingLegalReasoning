package com.bskoczylas.modelinglegalreasoning.models.observables;

import com.bskoczylas.modelinglegalreasoning.models.observers.IncompProp_Observer;
import com.bskoczylas.modelinglegalreasoning.models.observers.PBC_Observer;

public interface IncompProp_Observable {
    void addObserver(IncompProp_Observer observer);
    void removeObserver(IncompProp_Observer observer);
    void notifyObservers();
}
