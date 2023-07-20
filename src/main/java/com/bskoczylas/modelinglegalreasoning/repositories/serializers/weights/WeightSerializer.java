package com.bskoczylas.modelinglegalreasoning.repositories.serializers.weights;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class WeightSerializer extends JsonSerializer<Weight> {
    @Override
    public void serialize(Weight weight, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("value", weight.getValue().toString());
        gen.writeEndObject();
    }
}
