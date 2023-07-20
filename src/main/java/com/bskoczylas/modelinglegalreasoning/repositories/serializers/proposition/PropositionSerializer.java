package com.bskoczylas.modelinglegalreasoning.repositories.serializers.proposition;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class PropositionSerializer extends StdSerializer<Proposition> {

    public PropositionSerializer() {
        super(Proposition.class);
    }

    @Override
    public void serialize(Proposition proposition, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", proposition.getId());
        gen.writeStringField("statement", proposition.getStatement());
        gen.writeStringField("created", proposition.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        gen.writeEndObject();
    }
}
