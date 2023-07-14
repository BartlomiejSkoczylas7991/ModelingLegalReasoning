package com.bskoczylas.modelinglegalreasoning.domain.models;

import com.bskoczylas.modelinglegalreasoning.repositories.ProjectRepository;

import java.util.stream.Stream;

public class FacadeProject {
    private ProjectRepository projectRepository;

    public FacadeProject(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void saveProject(Project project) {
        projectRepository.save(project);
    }

    public Project getProject(String id) {
        return projectRepository.find(id);
    }

    public void deleteProject(String projectId) {
        projectRepository.delete(projectId);
    }

    public Stream<Project> getAllProjects() {
        return projectRepository.findAll().stream();
    }

    public void createProject(Project project) {
        projectRepository.save(project);
    }

    public void saveProjectsToFile() {
        projectRepository.saveToFile();
    }
}
