package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.dataStructures;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;

public class AVPair {
    private final AgentValue agentValue;
    private final Weight weight;

    public AVPair(AgentValue agentValue, Weight weight) {
        this.agentValue = agentValue;
        this.weight = weight;
    }

    public AgentValue getAgentValue() {
        return agentValue;
    }

    public Weight getWeight() {
        return weight;
    }

    public String getKey() {
        return getAgentValue().getAgent().toString();
    }

    public String getValue() {
        return getAgentValue().getValue().toString();
    }
}
