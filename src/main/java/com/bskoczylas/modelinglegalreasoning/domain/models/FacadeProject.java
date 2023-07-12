package com.bskoczylas.modelinglegalreasoning.domain.models;

import com.bskoczylas.modelinglegalreasoning.repositories.ProjectRepository;

public class FacadeProject {
    private ProjectRepository projectRepository;

    public FacadeProject(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public void createProject(Project project) {
        // here can be various operations related to the creation of the project
        projectRepository.save(project);
    }

    public Project getProject(String id) {
        // here can be various operations related to downloading the project
        return projectRepository.find(id);
    }
}
