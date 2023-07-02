package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observables;


import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.AVP_Observer;

public interface AVP_Observable {
    void addObserver(AVP_Observer observer);
    void removeObserver(AVP_Observer observer);
    void notifyAVPObservers();
}
