package com.bskoczylas.modelinglegalreasoning.repositories.serializers.weights;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class AgentValuePropositionSerializer extends JsonSerializer<AgentValueProposition> {
    @Override
    public void serialize(AgentValueProposition value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("agentId", value.getAgent().getId());
        gen.writeNumberField("valueId", value.getValue().getId());
        gen.writeNumberField("propositionId", value.getProposition().getId());
        gen.writeEndObject();
    }
}
