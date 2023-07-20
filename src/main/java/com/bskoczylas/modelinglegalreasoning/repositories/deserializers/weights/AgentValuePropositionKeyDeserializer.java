package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.weights;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.agent.AgentRepository;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.proposition.PropositionRepository;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.value.ValueRepository;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.io.IOException;

public class AgentValuePropositionKeyDeserializer extends KeyDeserializer {
    private AgentRepository agentRepository;
    private ValueRepository valueRepository;
    private PropositionRepository propositionRepository;

    public AgentValuePropositionKeyDeserializer() {
        this.agentRepository = AgentRepository.getInstance("agents.json"); // expected 1 arguement but found 0
        this.valueRepository = ValueRepository.getInstance("values.json");
        this.propositionRepository = PropositionRepository.getInstance("propositions.json");
    }

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        DeserializationContext deserializationContext = (DeserializationContext) ctxt.findInjectableValue(DeserializationContext.class.getName(), null, null);

        AgentRepository agentRepository = deserializationContext.getAgentRepository(); // mam błąd Cannot resolve method 'getAgentRepository' in 'DeserializationContext'
        ValueRepository valueRepository = deserializationContext.getValueRepository();
        PropositionRepository propositionRepository = deserializationContext.getPropositionRepository();

        String[] parts = key.split(", ");
        if (parts.length != 3) {
            throw new IOException("Cannot parse AgentValueProposition from key: " + key);
        }

        int agentId, valueId, propositionId;
        try {
            agentId = Integer.parseInt(parts[0]);
            valueId = Integer.parseInt(parts[1]);
            propositionId = Integer.parseInt(parts[2]);
        } catch (NumberFormatException e) {
            throw new IOException("Cannot parse integer from parts[0], parts[1] or parts[2] in key: " + key);
        }

        Agent agent = agentRepository.find(agentId);
        Value value = valueRepository.find(valueId);
        Proposition proposition = propositionRepository.find(propositionId);

        if (agent == null || value == null || proposition == null) {
            throw new IOException("Cannot retrieve Agent, Value, or Proposition from key: " + key);
        }

        return new AgentValueProposition(agent, value, proposition);
    }
}
