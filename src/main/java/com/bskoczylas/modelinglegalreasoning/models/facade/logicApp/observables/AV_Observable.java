package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.AV_Observer;

public interface AV_Observable {
    void addAVObserver(AV_Observer observer);
    void removeAVObserver(AV_Observer observer);
    void notifyAVObservers();
}
