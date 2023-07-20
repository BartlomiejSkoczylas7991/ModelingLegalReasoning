package com.bskoczylas.modelinglegalreasoning.adapters;

import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.IncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import com.bskoczylas.modelinglegalreasoning.repositories.ProjectRepository;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.agent.AgentDeserializer;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.incompProp.IncompPropDeserializer;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.scale.ScaleSerializer;
import com.bskoczylas.modelinglegalreasoning.repositories.serializers.agent.AgentSerializer;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.proposition.PropositionDeserializer;
import com.bskoczylas.modelinglegalreasoning.repositories.serializers.incompProp.IncompPropSerializer;
import com.bskoczylas.modelinglegalreasoning.repositories.serializers.proposition.PropositionSerializer;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.value.ValueDeserializer;
import com.bskoczylas.modelinglegalreasoning.repositories.serializers.scale.ScaleDeserializer;
import com.bskoczylas.modelinglegalreasoning.repositories.serializers.value.ValueSerializer;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.weights.AgentValuePropositionKeyDeserializer;
import com.bskoczylas.modelinglegalreasoning.repositories.serializers.weights.AgentValuePropositionKeySerializer;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JsonFileProjectRepository implements ProjectRepository {
    private File file;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<ProjectData> projects = new ArrayList<>();

    public JsonFileProjectRepository() throws IOException {
        URL url = getClass().getResource("/com/bskoczylas/modelinglegalreasoning/database/projectData.json");
        file = new File(url.getPath());
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // Register your custom key deserializer here
        objectMapper.registerModule(new SimpleModule()
                .addSerializer(Agent.class, new AgentSerializer())
                .addDeserializer(Agent.class, new AgentDeserializer())
                .addSerializer(Proposition.class, new PropositionSerializer())
                .addDeserializer(Proposition.class, new PropositionDeserializer())
                .addSerializer(Value.class, new ValueSerializer())
                .addDeserializer(Value.class, new ValueDeserializer())
                .addSerializer(IncompProp.class, new IncompPropSerializer())
                .addDeserializer(IncompProp.class, new IncompPropDeserializer())
                        .addDeserializer(Scale.class, new ScaleDeserializer())
                        .addSerializer(Scale.class, new ScaleSerializer())
                        .
                .addKeySerializer(AgentValueProposition.class, new AgentValuePropositionKeySerializer())
                .addKeyDeserializer(AgentValueProposition.class, new AgentValuePropositionKeyDeserializer())
        );
    }

    public JsonFileProjectRepository(String filePath) throws IOException {
        file = new File(filePath);
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);

        // Register your custom key deserializer here
        objectMapper.registerModule(
                new SimpleModule().addKeyDeserializer(AgentValueProposition.class, new AgentValuePropositionKeyDeserializer())
        );
    }

    @Override
    public void saveProjectData(List<ProjectData> projectDataList) throws IOException {
        objectMapper.writeValue(file, projectDataList);
    }

    @Override
    public void save(Project project) {

    }

    @Override
    public ProjectData find(String projectId) {
        List<ProjectData> allProjectsData = loadAll();
        return allProjectsData.stream()
                .filter(projectData -> projectData.getId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Override
    public void delete(String projectId) {
        List<ProjectData> allProjectsData = loadAll();
        allProjectsData.removeIf(projectData -> projectData.getId().equals(projectId));
        saveToFile(allProjectsData);
    }

    private List<ProjectData> loadAll() {
        try {
            String filePath = "com/bskoczylas/modelinglegalreasoning/database/projectData.json";
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);

            if (inputStream != null && inputStream.available() > 0) {
                return objectMapper.readValue(inputStream, new TypeReference<List<ProjectData>>() {});
            } else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            throw new RuntimeException("Problem with reading projects from JSON file", e);
        }
    }

    @Override
    public List<Project> findAll() {
        return loadProjects();
    }

    @Override
    public void saveToFile(List<ProjectData> projectDataList) {
        try {
            objectMapper.writeValue(file, projectDataList);
        } catch (IOException e) {
            throw new RuntimeException("Problem with saving project data to JSON file", e);
        }
    }

    private List<Project> loadProjects() {
        List<Project> loadedProjects = new ArrayList<>();
        try {
            String filePath = "com/bskoczylas/modelinglegalreasoning/database/projectData.json";
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);

            if (inputStream != null && inputStream.available() > 0) {
                List<ProjectData> allProjectsData = objectMapper.readValue(inputStream, new TypeReference<List<ProjectData>>() {});

                for (ProjectData projectData : allProjectsData) {
                    Project project = new Project(projectData.getName(), projectData.getDescription()); // I assumed Project has a constructor that takes name and description
                    loadedProjects.add(project);
                }
            } else {
                // removed 'projectData = new ProjectData();' because 'projectData' was not defined
            }
        } catch (IOException e) {
            throw new RuntimeException("Problem with reading projects from JSON file", e);
        }

        return loadedProjects;
    }
}
