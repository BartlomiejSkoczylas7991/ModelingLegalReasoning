package com.bskoczylas.modelinglegalreasoning.repositories.deserializers.agent;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AgentRepository {
    private static final ObjectMapper mapper = new ObjectMapper();
    private List<Agent> agents;
    private File file;
    private static AgentRepository instance;

    private AgentRepository(String fileName) throws IOException {
        file = new File(fileName);
        if (file.exists()) {
            // Read the agents from the file
            TypeReference<List<Agent>> typeReference = new TypeReference<>() {};
            agents = mapper.readValue(file, typeReference);
        } else {
            // Initialize an empty list if the file does not exist
            agents = new ArrayList<>();
        }
    }

    public static synchronized AgentRepository getInstance(String fileName) throws IOException {
        if (instance == null) {
            instance = new AgentRepository(fileName);
        }
        return instance;
    }

    public Agent find(int id) {
        for (Agent agent : agents) {
            if (agent.getId() == id) {
                return agent;
            }
        }
        return null;
    }

    public void save(Agent agent) throws IOException {
        agents.add(agent);
        mapper.writeValue(file, agents);
    }

    public void delete(int id) throws IOException {
        agents.removeIf(agent -> agent.getId() == id);
        mapper.writeValue(file, agents);
    }

    public List<Agent> findAll() {
        return agents;
    }
}