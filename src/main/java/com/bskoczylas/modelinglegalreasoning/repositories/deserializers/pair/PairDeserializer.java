package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.pair;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class PairDeserializer extends JsonDeserializer<Pair<?, ?>> {
    @Override
    public Pair<?, ?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        Object first = node.get("first").asText();
        Object second = node.get("second").asText();
        return new Pair<>(first, second);
    }
}
