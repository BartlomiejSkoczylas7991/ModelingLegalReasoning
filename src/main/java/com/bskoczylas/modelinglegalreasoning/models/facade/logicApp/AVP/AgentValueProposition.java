package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.AVP;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.AV.AgentValue;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Value.Value;

import java.util.Objects;

public class AgentValueProposition {
    private Agent agent;
    private Value value;
    private Proposition proposition;

    public AgentValueProposition(Agent agent, Value value, Proposition proposition) {
        this.agent = agent;
        this.value = value;
        this.proposition = proposition;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

    public Proposition getProposition() {
        return proposition;
    }

    public void setProposition(Proposition proposition) {
        this.proposition = proposition;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AgentValueProposition that = (AgentValueProposition) o;
        return Objects.equals(agent, that.agent) &&
                Objects.equals(value, that.value) &&
                Objects.equals(proposition, that.proposition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(agent, value, proposition);
    }

    public boolean matchesAgentValue(AgentValue agentValue) {
        return agent.equals(agentValue.getAgent()) && value.equals(agentValue.getValue());
    }

    @Override
    public String toString() {
        return "(" + agent + ", " + value + ", " + proposition + ")";
    }
}