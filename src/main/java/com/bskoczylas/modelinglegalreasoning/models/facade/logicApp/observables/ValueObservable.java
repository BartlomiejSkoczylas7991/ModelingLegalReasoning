package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Value.Value;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.ValueObserver;

public interface ValueObservable {
    void addObserver(ValueObserver observer);
    void removeObserver(ValueObserver observer);
    void notifyObservers(Value value);
}
