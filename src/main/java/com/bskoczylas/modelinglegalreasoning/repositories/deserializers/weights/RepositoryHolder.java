package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.weights;

import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.agent.AgentRepository;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.proposition.PropositionRepository;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.value.ValueRepository;

public class RepositoryHolder {
    private final AgentRepository agentRepository;
    private final ValueRepository valueRepository;
    private final PropositionRepository propositionRepository;

    public RepositoryHolder(AgentRepository agentRepository, ValueRepository valueRepository, PropositionRepository propositionRepository) {
        this.agentRepository = agentRepository;
        this.valueRepository = valueRepository;
        this.propositionRepository = propositionRepository;
    }

    public AgentRepository getAgentRepository() {
        return agentRepository;
    }

    public ValueRepository getValueRepository() {
        return valueRepository;
    }

    public PropositionRepository getPropositionRepository() {
        return propositionRepository;
    }
}
