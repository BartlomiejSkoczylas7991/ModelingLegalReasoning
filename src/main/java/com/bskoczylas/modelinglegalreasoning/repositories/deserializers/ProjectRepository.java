package com.bskoczylas.modelinglegalreasoning.repositories.deserializers;

import com.bskoczylas.modelinglegalreasoning.domain.models.Project;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectRepository {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final File projectsDirectory;

    public ProjectRepository(String projectsDirectoryPath) {
        projectsDirectory = new File(projectsDirectoryPath);
    }

    public Optional<Project> getById(String id) {
        File projectFile = new File(projectsDirectory, id + ".json");
        if (!projectFile.exists()) {
            return Optional.empty();
        }

        try {
            Project project = objectMapper.readValue(projectFile, Project.class);
            return Optional.of(project);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read project file", e);
        }
    }

    public void save(Project project) {
        File projectFile = new File(projectsDirectory, project.getId() + ".json");
        try {
            objectMapper.writeValue(projectFile, project);
        } catch (IOException e) {
            throw new RuntimeException("Failed to write project file", e);
        }
    }

    public void delete(String id) {
        File projectFile = new File(projectsDirectory, id + ".json");
        if (projectFile.exists()) {
            if (!projectFile.delete()) {
                throw new RuntimeException("Failed to delete project file");
            }
        }
    }
}