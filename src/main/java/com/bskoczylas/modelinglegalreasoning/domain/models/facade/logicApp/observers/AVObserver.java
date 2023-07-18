package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValueToWeight;

public interface AVObserver {
    void updateAV(AgentValueToWeight agentValueToWeight);
}
