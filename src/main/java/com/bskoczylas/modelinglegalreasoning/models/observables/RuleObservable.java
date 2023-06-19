package com.bskoczylas.modelinglegalreasoning.models.observables;

import com.bskoczylas.modelinglegalreasoning.models.observers.AVP_Observer;
import com.bskoczylas.modelinglegalreasoning.models.observers.RuleObserver;

public interface RuleObservable {
    void addObserver(RuleObserver observer);
    void removeObserver(RuleObserver observer);
    void notifyObservers();
}
