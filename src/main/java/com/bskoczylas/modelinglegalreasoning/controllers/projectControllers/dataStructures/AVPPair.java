package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.dataStructures;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;

public class AVPPair {
    private final AgentValueProposition agentValueProposition;
    private final Weight weight;

    public AVPPair(AgentValueProposition agentValueProposition, Weight weight) {
        this.agentValueProposition = agentValueProposition;
        this.weight = weight;
    }

    public AgentValueProposition getAgentValueProposition() {
        return agentValueProposition;
    }

    public Weight getWeight() {
        return weight;
    }

    public String getKey() {
        return getAgentValueProposition().getAgent().toString();
    }

    public String getValue() {
        return getAgentValueProposition().getValue().toString();
    }

    public String getProposition() {return getAgentValueProposition().getProposition().toString();}
}
