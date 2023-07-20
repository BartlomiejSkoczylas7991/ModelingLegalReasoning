package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.proposition;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class PropositionDeserializer extends StdDeserializer<Proposition> {

    public PropositionDeserializer() {
        super(Proposition.class);
    }

    @Override
    public Proposition deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        int id = node.get("id").asInt();
        String name = node.get("statement").asText();
        LocalDateTime created = LocalDateTime.parse(node.get("created").asText(), DateTimeFormatter.ISO_LOCAL_DATE_TIME);

        Proposition proposition = new Proposition(name);
        Field field = null;
        try {
            field = Agent.class.getDeclaredField("id");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        try {
            field.set(proposition, id);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        try {
            field = Agent.class.getDeclaredField("created");
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        try {
            field.set(proposition, created);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        return proposition;
    }

}
