package com.bskoczylas.modelinglegalreasoning.repositories;

import com.bskoczylas.modelinglegalreasoning.adapters.ProjectData;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;

import java.util.List;

public interface ProjectRepository {
    void save(Project project);
    Project find(String projectId);
    void delete(String projectId);
    List<Project> findAll();
    void saveToFile(List<ProjectData> projectDataList);
}
