package com.bskoczylas.modelinglegalreasoning.models.observables;

import com.bskoczylas.modelinglegalreasoning.models.observers.AV_Observer;
import com.bskoczylas.modelinglegalreasoning.models.observers.AgentObserver;

public interface AV_Observable {
    void addObserver(AV_Observer observer);
    void removeObserver(AV_Observer observer);
    void notifyObservers();
}
