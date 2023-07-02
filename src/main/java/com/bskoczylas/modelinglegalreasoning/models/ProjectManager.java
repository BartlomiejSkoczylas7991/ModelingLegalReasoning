package com.bskoczylas.modelinglegalreasoning.models;

import java.util.ArrayList;
import java.util.List;

public class ProjectManager {
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

    // Pozostałe metody...
}