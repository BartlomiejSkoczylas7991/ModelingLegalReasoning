package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.AgentObserver;

public interface AgentObservable {
    void addAgentObserver(AgentObserver observer);
    void removeAgentObserver(AgentObserver observer);
    void notifyAgentObservers(Agent agent);
}
