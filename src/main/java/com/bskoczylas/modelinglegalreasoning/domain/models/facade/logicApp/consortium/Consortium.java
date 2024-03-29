package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.consortium;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;

import java.util.*;
import java.util.stream.Collectors;


// Set Agents, in full agreement on the following conclusion (same RC agents)
public class Consortium {
    private ReasoningChain reasoningChain;
    private Set<Agent> agents ;// enum ConsortiumType

    public Consortium() {
        this.agents = new HashSet<>();
    }

    public Consortium(ReasoningChain reasoningChain) {
        this.reasoningChain = reasoningChain;
        this.agents = new HashSet<>();
    }

    public ReasoningChain getReasoningChain() {
        return reasoningChain;
    }

    public void setReasoningChain(ReasoningChain reasoningChain) {
        this.reasoningChain = reasoningChain;
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public void setAgents(Set<Agent> agents) {
        this.agents = agents;
    }

    public void addAgent(Agent agent) {
        this.agents.add(agent);
    }

    @Override
    public String toString() {
        return "{" +
                "ReasoningChain=" + reasoningChain.toString() +
                ", Agents=" + agents.stream()
                .map(Agent::getName)
                .collect(Collectors.joining(", ")) +
                '}';
    }
}


