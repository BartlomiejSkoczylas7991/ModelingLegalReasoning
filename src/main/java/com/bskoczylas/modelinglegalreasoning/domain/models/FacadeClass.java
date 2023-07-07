package com.bskoczylas.modelinglegalreasoning.domain.models;

import com.bskoczylas.modelinglegalreasoning.repositories.ProjectRepository;

// FacadeClass.java
public class FacadeClass {
    private ProjectRepository projectRepository;

    public FacadeClass(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void createProject(Project project) {
        // tutaj mogą być różne operacje związane z tworzeniem projektu
        projectRepository.save(project);
    }

    public Project getProject(String id) {
        // tutaj mogą być różne operacje związane z pobieraniem projektu
        return projectRepository.find(id);
    }
}
