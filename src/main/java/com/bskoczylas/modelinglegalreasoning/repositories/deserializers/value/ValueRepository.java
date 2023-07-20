package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.value;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.agent.AgentRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ValueRepository {
    private static final ObjectMapper mapper = new ObjectMapper();
    private List<Value> values;
    private File file;
    private static ValueRepository instance;
    private ValueRepository(String fileName) throws IOException {
        file = new File(fileName);
        if (file.exists()) {
            // Read the propositions from the file
            TypeReference<List<Value>> typeReference = new TypeReference<>() {};
            values = mapper.readValue(file, typeReference);
        } else {
            // Initialize an empty list if the file does not exist
            values = new ArrayList<>();
        }
    }

    public static synchronized ValueRepository getInstance(String fileName) throws IOException {
        if (instance == null) {
            instance = new ValueRepository(fileName);
        }
        return instance;
    }

    public Value find(int id) {
        for (Value value : values) {
            if (value.getId() == id) {
                return value;
            }
        }
        return null;
    }

    public void save(Value value) throws IOException {
        values.add(value);
        mapper.writeValue(file, values);
    }

    public void delete(int id) throws IOException {
        values.removeIf(value -> value.getId() == id);
        mapper.writeValue(file, values);
    }

    public List<Value> findAll() {
        return values;
    }
}
