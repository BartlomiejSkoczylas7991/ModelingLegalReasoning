package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValueToWeight;

public interface AV_Observer {
    void updateAV(AgentValueToWeight agentValueToWeight);
}
