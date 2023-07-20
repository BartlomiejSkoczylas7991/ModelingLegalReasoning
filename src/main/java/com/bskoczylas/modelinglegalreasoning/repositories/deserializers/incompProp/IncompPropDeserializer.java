package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.incompProp;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.IncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.time.LocalDateTime;

public class IncompPropDeserializer extends JsonDeserializer<IncompProp> {

    @Override
    public IncompProp deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException {

        JsonNode node = jp.getCodec().readTree(jp);

        int id = node.get("id").asInt();

        JsonNode propositionsPairNode = node.get("propositionsPair");
        Proposition first = propositionsPairNode.get("first").traverse(jp.getCodec()).readValueAs(Proposition.class);
        Proposition second = propositionsPairNode.get("second").traverse(jp.getCodec()).readValueAs(Proposition.class);
        Pair<Proposition, Proposition> propositionsPair = new Pair<>(first, second);

        LocalDateTime created = LocalDateTime.parse(node.get("created").asText());
        boolean isDecision = node.get("isDecision").asBoolean();

        IncompProp incompProp = new IncompProp(propositionsPair, isDecision);
        // assuming IncompProp has a method to set id and created date after initialization
        incompProp.setId(id);
        incompProp.setCreated(created);

        return incompProp;
    }
}
