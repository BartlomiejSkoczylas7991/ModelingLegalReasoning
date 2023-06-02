package com.bskoczylas.modelinglegalreasoning.models.observables;

import com.bskoczylas.modelinglegalreasoning.models.observers.AgentObserver;

public interface AgentObservable {
    void addObserver(AgentObserver observer);
    void removeObserver(AgentObserver observer);
    void notifyObservers();
}
