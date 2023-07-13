package com.bskoczylas.modelinglegalreasoning.domain.models.projectObserver;

import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AgentObserver;

public interface ProjectObservable {
    void addProjectObserver(ProjectObserver observer);
    void removeProjectObserver(ProjectObserver observer);
    void notifyProjectObservers(Project project);
}
