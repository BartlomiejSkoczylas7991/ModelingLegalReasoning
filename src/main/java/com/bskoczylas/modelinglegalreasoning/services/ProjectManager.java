package com.bskoczylas.modelinglegalreasoning.services;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.projectBuilder.ProjectBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectManager {
    private static int nextProjectNumber = 1;
    private List<Project> projects = new ArrayList<>();
    private Project currentProject;

    public void openProject(Project project) {
        projects.add(project);
        currentProject = project;
    }

    public void closeProject(Project project) {
        projects.remove(project);
        if (currentProject == project) {
            currentProject = projects.isEmpty() ? null : projects.get(0);
        }
    }

    public Project getCurrentProject() {
        return currentProject;
    }

    // Metoda do zapisywania projektu do pliku
    public void saveProjectToFile(Project project, File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(file, project);
    }

    public Project loadProjectFromFile(File file) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(file, Project.class);
    }


    public Project createProject(String name) {
        if (name == null || name.trim().isEmpty()) {
            name = "Project" + nextProjectNumber++;
        }
        Project project = new ProjectBuilder(name).build();
        projects.add(project);
        return project;
    }
}