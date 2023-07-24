package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable;

import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.ValueControllerObserver;

public interface ValueControllerObservable {
    void addValueContrObserver(ValueControllerObserver observer);
    void removeValueContrObserver(ValueControllerObserver observer);
    void notifyValueContrObservers();
}
