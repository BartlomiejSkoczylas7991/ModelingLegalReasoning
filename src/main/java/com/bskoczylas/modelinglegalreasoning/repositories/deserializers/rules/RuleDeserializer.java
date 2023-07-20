package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.rules;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;

public class RuleDeserializer extends JsonDeserializer<Rule> {
    @Override
    public Rule deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectMapper mapper = (ObjectMapper) p.getCodec();
        JsonNode node = mapper.readTree(p);

        int id = node.get("id").asInt();
        Set<Proposition> premises = mapper.readValue(node.get("premises").toString(), new TypeReference<Set<Proposition>>(){});
        Proposition conclusion = mapper.readValue(node.get("conclusion").toString(), Proposition.class);
        String label = node.get("label").asText();
        LocalDateTime created = LocalDateTime.parse(node.get("created").asText());

        Rule rule = new Rule(premises, conclusion, label);
        rule.setId(id);
        rule.setCreated(created);

        return rule;
    }
}
