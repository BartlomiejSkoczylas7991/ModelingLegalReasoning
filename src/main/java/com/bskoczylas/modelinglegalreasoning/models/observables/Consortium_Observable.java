package com.bskoczylas.modelinglegalreasoning.models.observables;

import com.bskoczylas.modelinglegalreasoning.models.observers.Consortium_Observer;

public interface Consortium_Observable {
    void addObserver(Consortium_Observer observer);
    void removeObserver(Consortium_Observer observer);
    void notifyObservers();
}
