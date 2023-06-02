package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

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
        if (!(o instanceof AgentValueProposition)) return false;
        AgentValueProposition that = (AgentValueProposition) o;
        return agent.equals(that.agent) && value.equals(that.value) && proposition.equals(that.proposition);
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