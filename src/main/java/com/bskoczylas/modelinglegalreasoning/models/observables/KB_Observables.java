package com.bskoczylas.modelinglegalreasoning.models.observables;

import com.bskoczylas.modelinglegalreasoning.models.observers.IncompProp_Observer;
import com.bskoczylas.modelinglegalreasoning.models.observers.KB_Observer;

public interface KB_Observables {
    void addObserver(KB_Observer observer);
    void removeObserver(KB_Observer observer);
    void notifyObservers();
}
