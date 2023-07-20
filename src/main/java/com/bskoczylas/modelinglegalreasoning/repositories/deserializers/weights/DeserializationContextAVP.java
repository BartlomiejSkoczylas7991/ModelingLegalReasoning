package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.weights;

import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.agent.AgentRepository;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.proposition.PropositionRepository;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.value.ValueRepository;

public class DeserializationContextAVP {
    private AgentRepository agentRepository;
    private ValueRepository valueRepository;
    private PropositionRepository propositionRepository;

    public DeserializationContextAVP() {
    }

    public AgentRepository getAgentRepository() {
        return agentRepository;
    }

    public void setAgentRepository(AgentRepository agentRepository) {
        this.agentRepository = agentRepository;
    }

    public ValueRepository getValueRepository() {
        return valueRepository;
    }

    public void setValueRepository(ValueRepository valueRepository) {
        this.valueRepository = valueRepository;
    }

    public PropositionRepository getPropositionRepository() {
        return propositionRepository;
    }

    public void setPropositionRepository(PropositionRepository propositionRepository) {
        this.propositionRepository = propositionRepository;
    }
}
