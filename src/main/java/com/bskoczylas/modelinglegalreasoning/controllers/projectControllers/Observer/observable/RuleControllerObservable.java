package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable;

import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.RuleControllerObserver;

public interface RuleControllerObservable {
    void addRuleContrObserver(RuleControllerObserver observer);
    void removeRuleContrObserver(RuleControllerObserver observer);
    void notifyRuleContrObservers();
}
