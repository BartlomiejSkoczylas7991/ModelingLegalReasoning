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
    private final ObjectMapper objectMapper;
    private List<Project> projects;

    public JsonFileProjectRepository() {
        String filePath = "com/bskoczylas/modelinglegalreasoning/projectData.json";
        this.file = new File(getClass().getClassLoader().getResource(filePath).getFile());
        this.objectMapper = new ObjectMapper();
        loadProjects();
    }

    @Override
    public void save(Project project) {
        List<Project> projects = new ArrayList<>();
        if (file.exists()) {
            try {
                projects = objectMapper.readValue(file, new TypeReference<List<Project>>(){});
            } catch (IOException e) {
                throw new RuntimeException("Problem with reading project to JSON file", e);
            }
        }
        projects.add(project);
        try {
            objectMapper.writeValue(file, projects);
        } catch (IOException e) {
            throw new RuntimeException("Problem with saving project to JSON file", e);
        }
    }

    @Override
    public Project find(String projectId) {
        List<Project> projects = loadProjectsFromFile();
        return projects.stream()
                .filter(project -> project.getId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Override
    public void delete(String projectId) {
        List<Project> projects = loadProjectsFromFile();
        projects.removeIf(project -> project.getId().equals(projectId));
        try {
            objectMapper.writeValue(file, projects);
        } catch (IOException e) {
            throw new RuntimeException("Problem with saving project to JSON file", e);
        }
    }

    @Override
    public List<Project> findAll() {
        return loadProjects();
    }

    private List<Project> loadProjectsFromFile() {
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, new TypeReference<List<Project>>(){});
            } catch (IOException e) {
                throw new RuntimeException("Problem z odczytem projektów z pliku JSON", e);
            }
        }
        return new ArrayList<>();
    }

    private List<Project> loadProjects() {
        if (file.exists()) {
            try {
                return objectMapper.readValue(file, new TypeReference<List<Project>>(){});
            } catch (IOException e) {
                throw new RuntimeException("Problem z odczytem projektów z pliku JSON", e);
            }
        }
        return new ArrayList<>();
    }
}
