package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.weights;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class WeightDeserializer  extends JsonDeserializer<Weight> {
    @Override
    public Weight deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);
        String valueStr = node.get("value").asText();
        Object value;
        if ("?".equals(valueStr)) {
            value = "?";
        } else {
            try {
                value = Integer.parseInt(valueStr);
            } catch (NumberFormatException e) {
                throw new JsonParseException(p, "Invalid weight value: " + valueStr);
            }
        }
        Scale scale = new Scale(0, 10);
        Weight weight = new Weight(scale, value);

        return weight;
    }
}
