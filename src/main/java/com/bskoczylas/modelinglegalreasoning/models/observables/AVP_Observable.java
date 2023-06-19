package com.bskoczylas.modelinglegalreasoning.models.observables;


import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Weight;
import com.bskoczylas.modelinglegalreasoning.models.observers.AVP_Observer;

public interface AVP_Observable {
    void addObserver(AVP_Observer observer);
    void removeObserver(AVP_Observer observer);
    void notifyAVPObservers();
}
