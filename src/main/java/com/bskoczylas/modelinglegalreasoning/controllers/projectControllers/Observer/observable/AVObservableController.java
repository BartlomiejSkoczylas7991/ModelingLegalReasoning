package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable;

import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.AVObserverController;

public interface AVObservableController {
    void addAVObserver(AVObserverController observer);
    void removeAVObserver(AVObserverController observer);
    void notifyAVObservers();
}
