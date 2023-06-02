package com.bskoczylas.modelinglegalreasoning.models.observers;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Agent;

public interface AgentObserver {
    void updateAgent(Agent agent);
}
