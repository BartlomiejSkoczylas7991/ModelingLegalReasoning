package com.bskoczylas.modelinglegalreasoning.repositories.serializers.weights;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class AgentValuePropositionKeySerializer extends JsonSerializer<AgentValueProposition> {

    @Override
    public void serialize(AgentValueProposition value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Assuming the Agent, Value, and Proposition objects have unique and constant IDs
        String key = value.getAgent().getId() + ", " + value.getValue().getId() + ", " + value.getProposition().getId();
        gen.writeFieldName(key);
    }
}
