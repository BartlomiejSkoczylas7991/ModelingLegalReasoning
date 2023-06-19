package com.bskoczylas.modelinglegalreasoning.models.observables;

import com.bskoczylas.modelinglegalreasoning.models.observers.Decision_Observer;

public interface Decision_Observable {
    void addObserver(Decision_Observer observer);
    void removeObserver(Decision_Observer observer);
    void notifyObservers();
}
