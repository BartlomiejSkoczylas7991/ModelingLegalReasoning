package com.bskoczylas.modelinglegalreasoning.repositories.serializers.incompProp;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.IncompProp;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class IncompPropSerializer extends JsonSerializer<IncompProp> {

    @Override
    public void serialize(IncompProp value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {

        jgen.writeStartObject();
        jgen.writeNumberField("id", value.getId());

        jgen.writeObjectFieldStart("propositionsPair");
        jgen.writeObjectField("first", value.getPropositionsPair().getFirst());
        jgen.writeObjectField("second", value.getPropositionsPair().getSecond());
        jgen.writeEndObject();

        jgen.writeStringField("created", value.getCreated().toString());
        jgen.writeBooleanField("isDecision", value.isDecision());
        jgen.writeEndObject();
    }
}
