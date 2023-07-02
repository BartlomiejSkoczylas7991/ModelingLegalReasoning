package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.RuleObserver;

public interface RuleObservable {
    void addObserver(RuleObserver observer);
    void removeObserver(RuleObserver observer);
    void notifyObservers();
}
