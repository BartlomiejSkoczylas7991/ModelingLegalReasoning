package com.bskoczylas.modelinglegalreasoning.repositories.serializers.scale;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class ScaleDeserializer extends JsonDeserializer<Scale> {
    @Override
    public Scale deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        Pair<Integer, Integer> elements = mapper.readValue(node.get("elements").toString(), new TypeReference<Pair<Integer, Integer>>(){});

        Scale scale = new Scale(elements.getFirst(), elements.getSecond());

        return scale;
    }
}
