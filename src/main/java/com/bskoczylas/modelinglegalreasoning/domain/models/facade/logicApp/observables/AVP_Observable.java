package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;


import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AVP_Observer;

public interface AVP_Observable {
    void addObserver(AVP_Observer observer);
    void removeObserver(AVP_Observer observer);
    void notifyAVPObservers();
}
