package com.bskoczylas.modelinglegalreasoning.services;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.projectBuilder.ProjectBuilder;
import com.bskoczylas.modelinglegalreasoning.repositories.ProjectRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ProjectManager {
    private static int nextProjectNumber = 1;
    private List<Project> projects = new ArrayList<>();
    private Project currentProject;
    private ProjectRepository projectRepository;

    public ProjectManager(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
        loadProjects();
    }

    private void loadProjects() {
        this.projects.addAll(projectRepository.findAll());
    }

    // Metoda do zapisywania projektu do pliku
    public void saveProject(Project project) throws IOException {
        projectRepository.save(project);
    }

    public void openProject(Project project) {
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

    public Stream<Project> getProjects() {
        return projects.stream();
    }

    public Project createProject(String name) {
        if (name == null || name.trim().isEmpty()) {
            name = "Project" + nextProjectNumber++;
        }
        Project project = new ProjectBuilder(name).build();
        projects.add(project);

        // save project to repository
        projectRepository.save(project);

        return project;
    }

    public void deleteProject(Project project) {
        if (this.currentProject != project) {
            projects.remove(project);
            projectRepository.delete(project.getId());
        }
    }

    public void saveProjectsToFile() {
        projectRepository.saveToFile();
    }
}
