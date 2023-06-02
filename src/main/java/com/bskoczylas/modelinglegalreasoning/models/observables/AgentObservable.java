package com.bskoczylas.modelinglegalreasoning.models.observables;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Agent;
import com.bskoczylas.modelinglegalreasoning.models.observers.AgentObserver;

public interface AgentObservable {
    void addAgentObserver(AgentObserver observer);

    void removeAgentObserver(AgentObserver observer);

    void notifyAgentObservers(Agent agent);
}
