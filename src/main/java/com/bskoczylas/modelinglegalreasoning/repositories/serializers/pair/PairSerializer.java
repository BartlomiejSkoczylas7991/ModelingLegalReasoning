package com.bskoczylas.modelinglegalreasoning.repositories.serializers.pair;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class PairSerializer extends JsonSerializer<Pair<?, ?>> {
    @Override
    public void serialize(Pair<?, ?> value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("first", value.getFirst());
        gen.writeObjectField("second", value.getSecond());
        gen.writeEndObject();
    }
}
