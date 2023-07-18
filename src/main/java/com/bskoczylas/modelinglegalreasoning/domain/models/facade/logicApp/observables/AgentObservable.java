package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AgentObserver;

public interface AgentObservable {
    void addAgentObserver(AgentObserver observer);
    void removeAgentObserver(AgentObserver observer);
    void notifyAgentObservers(ListAgent listAgent);
}
