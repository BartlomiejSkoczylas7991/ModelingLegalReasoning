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

    public JsonFileProjectRepository(String filePath) {
        this.file = new File(filePath);
        this.objectMapper = new ObjectMapper();
        this.projects = new ArrayList<>();
        loadProjects();
    }

    @Override
    public void save(Project project) {
        projects.add(project);
        try {
            objectMapper.writeValue(file, projects);
        } catch (IOException e) {
            throw new RuntimeException("Problem z zapisem projektu do pliku JSON", e);
        }
    }

    @Override
    public Project find(String projectId) {
        return projects.stream()
                .filter(project -> project.getId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Projekt nie został znaleziony"));
    }

    @Override
    public void delete(String projectId) {

    }

    @Override
    public List<Project> findAll() {
        return null;
    }

    private void loadProjects() {
        if (file.exists()) {
            try {
                this.projects = objectMapper.readValue(file, new TypeReference<List<Project>>(){});
            } catch (IOException e) {
                throw new RuntimeException("Problem z odczytem projektów z pliku JSON", e);
            }
        }
    }
}
