package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.consortium;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;

import java.util.*;
import java.util.stream.Collectors;


// Set Agents, in full agreement on the following conclusion (same RC agents)
public class Consortium {
    private ReasoningChain reasoningChain;
    private Set<Agent> agents;// enum ConsortiumType

    public Consortium() {
    }

    public Consortium(ReasoningChain reasoningChain) {
        this.reasoningChain = reasoningChain;
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


    @Override
    public String toString() {
        return "Consortium{" +
                "ReasoningChain=" + reasoningChain.toString() +
                ", Agents=" + agents.stream()
                .map(Agent::getName)
                .collect(Collectors.joining(", ")) +
                '}';
    }
}


