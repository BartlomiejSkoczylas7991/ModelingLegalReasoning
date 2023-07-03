package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.AV;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Value.Value;

import java.util.Objects;

public class AgentValue {
    private Agent agent;
    private Value value;

    public AgentValue(Agent agent, Value value) {
        this.agent = agent;
        this.value = value;
    }

    public Agent getAgent() { return agent; }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AgentValue)) return false;
        AgentValue that = (AgentValue) o;
        return agent.equals(that.agent) && value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agent, value);
    }

    @Override
    public String toString() {
        return "(" + agent + ", " + value + ")";
    }
}