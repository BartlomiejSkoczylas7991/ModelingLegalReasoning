package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable;


import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.AVPObserverController;

public interface AVPObservableController {
    void addObserver(AVPObserverController observer);
    void removeObserver(AVPObserverController observer);
    void notifyAVPObservers();
}
