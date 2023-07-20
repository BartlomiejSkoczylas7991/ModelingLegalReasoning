package com.bskoczylas.modelinglegalreasoning.repositories.serializers.rules;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class RuleSerializer extends JsonSerializer<Rule> {
    @Override
    public void serialize(Rule rule, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("id", rule.getId());
        gen.writeObjectField("premises", rule.getPremises());
        gen.writeObjectField("conclusion", rule.getConclusion());
        gen.writeStringField("label", rule.getLabel());
        gen.writeStringField("created", rule.getCreated().toString());
        gen.writeEndObject();
    }
}
