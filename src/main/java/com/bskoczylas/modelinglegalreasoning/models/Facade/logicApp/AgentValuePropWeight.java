package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Objects.Agent;

import java.util.HashMap;

public class AgentValuePropWeight {
    private HashMap<AgentValueProposition, Weight> agent_value_prop_weights;

    public AgentValuePropWeight() {
        this.agent_value_prop_weights = new HashMap<>();
    }

    public void addValue(Agent agent, Value value, Proposition prop, Weight weight) {
        AgentValueProposition agentValueProp = new AgentValueProposition(agent, value, prop);
        this.agent_value_prop_weights.put(agentValueProp, weight);
    }

    public Weight getWeight(Agent agent, Value value, Proposition prop) {
        AgentValueProposition agentValueProp = new AgentValueProposition(agent, value, prop);
        return this.agent_value_prop_weights.get(agentValueProp);
    }
}