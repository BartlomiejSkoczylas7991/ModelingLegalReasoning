package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.value;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ValueDeserializer extends StdDeserializer<Value> {

    public ValueDeserializer() {
        super(Value.class);
    }

    @Override
    public Value deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        int id = node.get("id").asInt();
        String name = node.get("name").asText();
        LocalDateTime created = LocalDateTime.parse(node.get("created").asText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Value value = new Value(name);
        Field field = null;
        try {
            field = Value.class.getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        try {
            field.set(value, id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            field = Value.class.getDeclaredField("created");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        try {
            field.set(value, created);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return value;
    }

}
