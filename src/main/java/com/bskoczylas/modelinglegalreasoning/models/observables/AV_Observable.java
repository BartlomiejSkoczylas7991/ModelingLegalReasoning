package com.bskoczylas.modelinglegalreasoning.models.observables;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.AgentValue;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Weight;
import com.bskoczylas.modelinglegalreasoning.models.observers.AV_Observer;
import com.bskoczylas.modelinglegalreasoning.models.observers.AgentObserver;

public interface AV_Observable {
    void addAVObserver(AV_Observer observer);
    void removeAVObserver(AV_Observer observer);
    void notifyAVObservers();
}
