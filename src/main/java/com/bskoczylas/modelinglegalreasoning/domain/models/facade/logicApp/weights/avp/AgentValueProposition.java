package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;

import java.io.Serializable;
import java.util.Objects;

public class AgentValueProposition implements Serializable {
    private static final long serialVersionUID = 1L;
    private Agent agent;
    private Value value;
    private Proposition proposition;

    public AgentValueProposition(Agent agent, Value value, Proposition proposition) {
        this.agent = agent;
        this.value = value;
        this.proposition = proposition;
    }

    public AgentValueProposition(AgentValueProposition other) {
        this.agent = new Agent(other.agent);
        this.value = new Value(other.value);
        this.proposition = new Proposition(other.proposition);
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

    @Override
    public String toString() {
        return "(" + agent + ", " + value + ", " + proposition + ")";
    }
}