package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.scale;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class ScaleSerializer  extends JsonSerializer<Scale> {
    @Override
    public void serialize(Scale scale, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeObjectField("elements", scale.getElements());
        gen.writeEndObject();
    }
}
