package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.proposition;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class PropositionRepository {
    private static final ObjectMapper mapper = new ObjectMapper();
    private List<Proposition> propositions;
    private File file;

    public PropositionRepository(String fileName) throws IOException {
        file = new File(fileName);
        if (file.exists()) {
            // Read the propositions from the file
            TypeReference<List<Proposition>> typeReference = new TypeReference<>() {};
            propositions = mapper.readValue(file, typeReference);
        } else {
            // Initialize an empty list if the file does not exist
            propositions = new ArrayList<>();
        }
    }

    public Proposition find(int id) {
        for (Proposition proposition : propositions) {
            if (proposition.getId() == id) {
                return proposition;
            }
        }
        return null;
    }

    public void save(Proposition proposition) throws IOException {
        propositions.add(proposition);
        mapper.writeValue(file, propositions);
    }

    public void delete(int id) throws IOException {
        propositions.removeIf(proposition -> proposition.getId() == id);
        mapper.writeValue(file, propositions);
    }

    public List<Proposition> findAll() {
        return propositions;
    }
}