package com.bskoczylas.modelinglegalreasoning.models.observers;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.AgentValue;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Weight;

public interface AV_Observer {
    void updateAV(AgentValueToWeight agentValueToWeight);
}
