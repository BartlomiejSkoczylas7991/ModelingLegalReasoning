package com.bskoczylas.modelinglegalreasoning.services;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.bskoczylas.modelinglegalreasoning.adapters.JsonFileProjectRepository;
import com.bskoczylas.modelinglegalreasoning.adapters.ProjectData;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.ProjectFactory;
import com.bskoczylas.modelinglegalreasoning.repositories.ProjectRepository;

public class ProjectManager {
    private static int nextProjectNumber = 1;
    private List<Project> projects = new ArrayList<>();
    private Project currentProject;
    private ProjectRepository projectRepository;
    private final ProjectFactory projectFactory = new ProjectFactory();

    public ProjectManager(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
        loadProjects();
    }

    private void loadAllProjects() {
        List<ProjectData> allProjectsData = projectRepository.findAll();

        for (ProjectData projectData : allProjectsData) {
            Project project = projectFactory.createProject(projectData);
            projects.add(project);
        }
    }

    public void saveCurrentProject() throws IOException {
        if(currentProject != null){
            ProjectData projectData = currentProject.toProjectData();
            projectRepository.save(projectData);
        }
    }

    private void loadProjects() {
        this.projects.addAll(.getAllProjects().collect(Collectors.toList()));
    }

    public void openProject(String projectId) {
        currentProject = projects.stream()
                .filter(project -> project.getId().equals(projectId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Project not found"));
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

    public Project createProject(String name, String description) {
        if (name == null || name.trim().isEmpty()) {
            name = "Project" + nextProjectNumber++;
        }
        Project project = this.projectFactory.createProject(name, description);
        projects.add(project);

        // Set current project to the newly created project
        currentProject = project;

        return project;
    }

    public void deleteProject(Project project) {
        if (this.currentProject != project) {
            projects.remove(project);
            projectRepository.delete(project.getId());
        }
    }
}
