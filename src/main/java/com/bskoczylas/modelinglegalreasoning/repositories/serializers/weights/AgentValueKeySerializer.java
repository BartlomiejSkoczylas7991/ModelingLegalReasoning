package com.bskoczylas.modelinglegalreasoning.repositories.serializers.weights;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class AgentValueKeySerializer extends JsonSerializer<AgentValue>  {
    @Override
    public void serialize(AgentValue value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        // Assuming the Agent and Value objects have unique and constant IDs
        String key = value.getAgent().getId() + ", " + value.getValue().getId();
        gen.writeFieldName(key);
    }
}