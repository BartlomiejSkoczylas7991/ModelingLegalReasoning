package com.bskoczylas.modelinglegalreasoning.models.observers;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Weight;

public interface AVP_Observer {
    void updateAVP(AgentValuePropWeight agentValuePropWeight);
}
