package com.bskoczylas.modelinglegalreasoning.repositories.serializers.agent;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class AgentSerializer extends StdSerializer<Agent> {

    public AgentSerializer() {
        super(Agent.class);
    }

    @Override
    public void serialize(Agent agent, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", agent.getId());
        gen.writeStringField("name", agent.getName());
        gen.writeStringField("created", agent.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        gen.writeEndObject();
    }
}
