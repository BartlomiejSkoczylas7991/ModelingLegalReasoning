package com.bskoczylas.modelinglegalreasoning.repositories.serializers.value;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class ValueSerializer  extends StdSerializer<Value> {

    public ValueSerializer() {
        super(Value.class);
    }

    @Override
    public void serialize(Value value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", value.getId());
        gen.writeStringField("name", value.getName());
        gen.writeStringField("created", value.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        gen.writeEndObject();
    }
}
