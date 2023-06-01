package com.bskoczylas.modelinglegalreasoning.models.observables;

import com.bskoczylas.modelinglegalreasoning.models.observers.ValueObserver;

public interface ValueObservable {
    void addObserver(ValueObserver observer);
    void removeObserver(ValueObserver observer);
    void notifyObservers();
}
