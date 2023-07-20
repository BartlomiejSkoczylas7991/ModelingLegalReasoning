package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.weights;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.agent.AgentRepository;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.proposition.PropositionRepository;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.value.ValueRepository;
import
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.io.IOException;

public class AgentValuePropositionKeyDeserializer extends KeyDeserializer {

    public AgentValuePropositionKeyDeserializer() {
    }

    @Override
    public Object deserializeKey(String key, DeserializationContextAVP ctxt) throws IOException {
        DeserializationContextAVP deserializationContext = (DeserializationContextAVP) ctxt.f(DeserializationContextAVP.class.getName(), null, null);

        AgentRepository agentRepository = deserializationContext.getAgentRepository();
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
