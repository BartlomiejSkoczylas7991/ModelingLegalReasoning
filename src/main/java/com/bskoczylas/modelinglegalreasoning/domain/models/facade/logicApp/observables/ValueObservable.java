package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.ListValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ValueObserver;

public interface ValueObservable {
    void addObserver(ValueObserver observer);
    void removeObserver(ValueObserver observer);
    void notifyObservers(ListValue listValue);
}
