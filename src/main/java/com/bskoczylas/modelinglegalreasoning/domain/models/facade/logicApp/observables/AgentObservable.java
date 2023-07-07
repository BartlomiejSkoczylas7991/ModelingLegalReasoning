package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AgentObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;

public interface AgentObservable {
    void addAgentObserver(AgentObserver observer);
    void removeAgentObserver(AgentObserver observer);
    void notifyAgentObservers(Agent agent);
}
