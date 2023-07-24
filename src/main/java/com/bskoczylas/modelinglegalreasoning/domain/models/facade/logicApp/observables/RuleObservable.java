package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.RuleObserver;

public interface RuleObservable {
    void addObserver(RuleObserver observer);
    void removeObserver(RuleObserver observer);
    void notifyObservers();
}
