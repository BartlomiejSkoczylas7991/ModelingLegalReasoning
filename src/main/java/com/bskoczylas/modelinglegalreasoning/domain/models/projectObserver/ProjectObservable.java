package com.bskoczylas.modelinglegalreasoning.domain.models.projectObserver;

import com.bskoczylas.modelinglegalreasoning.domain.models.Project;

public interface ProjectObservable {
    void addProjectObserver(ProjectObserver observer);
    void removeProjectObserver(ProjectObserver observer);
    void notifyProjectObservers(Project project);
}
