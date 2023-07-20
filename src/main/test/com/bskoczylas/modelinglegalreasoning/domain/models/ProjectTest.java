package com.bskoczylas.modelinglegalreasoning.domain.models;

import com.bskoczylas.modelinglegalreasoning.services.ProjectManager;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProjectTest {
    @Test
    public void testCreateProject() {
        ProjectFactory mockFactory = mock(ProjectFactory.class);
        Project mockProject = mock(Project.class);
        when(mockFactory.createProject(anyString(), anyString())).thenReturn(mockProject);

        ProjectManager projectManager = new ProjectManager();
        Project result = projectManager.createProject("Test", "Test Description");

        assertEquals(mockProject, result);
    }
}