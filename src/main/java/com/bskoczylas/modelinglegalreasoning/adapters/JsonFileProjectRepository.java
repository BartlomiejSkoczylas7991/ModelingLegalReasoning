package com.bskoczylas.modelinglegalreasoning.adapters;

import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.repositories.ProjectRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonFileProjectRepository implements ProjectRepository {
    private final File file;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private List<Project> projects = new ArrayList<>();

    public JsonFileProjectRepository() {
        String filePath = "com/bskoczylas/modelinglegalreasoning/projectData.json";
        this.file = new File(getClass().getClassLoader().getResource(filePath).getFile());
        loadProjects();
    }

    @Override
    public void save(Project project) {
        projects.add(project);
    }

    @Override
    public Project find(String projectId) {
        return projects.stream()
                .filter(project -> project.getId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Override
    public void delete(String projectId) {
        projects.removeIf(project -> project.getId().equals(projectId));
    }

    @Override
    public List<Project> findAll() {
        return loadProjects();
    }

    private List<Project> loadProjects() {
        if (file.exists()) {
            try {
                projects = objectMapper.readValue(file, new TypeReference<List<Project>>(){});
            } catch (IOException e) {
                throw new RuntimeException("Problem with reading projects from JSON file", e);
            }
        }
        return projects;
    }

    public void saveToFile() {
        try {
            objectMapper.writeValue(file, projects);
        } catch (IOException e) {
            throw new RuntimeException("Problem with saving project to JSON file", e);
        }
    }
}
