package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.pair;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class PairDeserializer extends JsonDeserializer<Pair<?, ?>> {
    @Override
    public Pair<?, ?> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        JavaType javaType = ctxt.constructType(new TypeReference<Pair<?, ?>>(){}.getClass());
        JavaType firstType = javaType.containedType(0);
        JavaType secondType = javaType.containedType(1);
        Object first = p.getCodec().treeToValue(node.get("first"), firstType.getRawClass());
        Object second = p.getCodec().treeToValue(node.get("second"), secondType.getRawClass());
        return new Pair<>(first, second);
    }
}
