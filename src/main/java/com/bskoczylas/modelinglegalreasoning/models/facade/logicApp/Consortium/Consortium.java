package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Consortium;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.ReasoningChain.ReasoningChain;

import java.util.*;


// Set Agentów, w całkowiciej zgodzie co do następującej konkluzji (to samo RC agentów)
 public class Consortium {
        private ReasoningChain reasoningChain;
        private Set<Agent> agents;// typ enum ConsortiumType

        // konstruktor, gettery, settery

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
}


