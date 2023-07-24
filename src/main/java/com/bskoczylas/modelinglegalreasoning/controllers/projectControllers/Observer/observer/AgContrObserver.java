package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer;

import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.AgentController;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.ListAgent;

public interface AgContrObserver {
    void updateAgentController(AgentController agentController);
}
