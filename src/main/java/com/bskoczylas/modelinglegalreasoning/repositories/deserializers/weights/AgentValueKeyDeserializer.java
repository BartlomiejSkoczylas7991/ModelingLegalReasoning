package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.weights;


import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.agent.AgentRepository;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.proposition.PropositionRepository;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.value.ValueRepository;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.KeyDeserializer;

import java.io.IOException;

public class AgentValueKeyDeserializer extends KeyDeserializer {
    private AgentRepository agentRepository;
    private ValueRepository valueRepository;

    public AgentValueKeyDeserializer(AgentRepository agentRepository,
                                                ValueRepository valueRepository) {
        this.agentRepository = agentRepository;
        this.valueRepository = valueRepository;
    }

    @Override
    public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
        String[] parts = key.split(", ");
        if (parts.length != 2) {
            throw new IOException("Cannot parse AgentValue from key: " + key);
        }

        // Assuming each part is unique and retrievable from their respective repository

        int agentId, valueId;
        try {
            agentId = Integer.parseInt(parts[0]);
            valueId = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new IOException("Cannot parse integer from parts[0], parts[1] or parts[2] in key: " + key);
        }

        Agent agent = agentRepository.find(agentId);
        Value value = valueRepository.find(valueId);

        if (agent == null || value == null) {
            throw new IOException("Cannot retrieve Agent or Value from key: " + key);
        }

        return new AgentValue(agent, value);
    }
}

