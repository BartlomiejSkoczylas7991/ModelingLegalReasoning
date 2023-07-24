package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable;

import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.AgentController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.IncompPropController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.AgContrObserver;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.IncompControllerObserver;

public interface AgContrObservable {
    void addAgentContrObserver(AgContrObserver observer);
    void removeAgentContrObserver(AgContrObserver observer);
    void notifyAgentContrObservers();
}
