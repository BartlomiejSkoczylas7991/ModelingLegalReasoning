package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import javafx.collections.FXCollections;
import javafx.scene.control.TableView;

public class AgentController {
    private TableView<Agent> agentTable;
    private ProjectController projectController;
    private Project project;

    public AgentController(TableView<Agent> agentTable, ProjectController projectController) {
        this.agentTable = agentTable;
        this.projectController = projectController;
        this.project = projectController.getProject();
    }

    public void addAgent(Agent agent) {
        if (!project.getListAgent().getAgents().stream().anyMatch(existingAgent -> existingAgent.getName().equals(agent.getName()))) {
            project.getListAgent().addAgent(agent);
            updateAgentTable();
        } else {
            // TODO: Show error to user, agent with this name already exists
        }
    }

    public void editAgent(Agent oldAgent, Agent newAgent) {
        int index = project.getListAgent().getAgents().indexOf(oldAgent);
        if (index != -1) {
            project.getListAgent().getAgents().set(index, newAgent);
            updateAgentTable();
        } else {
            // TODO: Show error to user, agent does not exist
        }
    }

    public void removeAgent(Agent agent) {
        if (project.getListAgent().getAgents().remove(agent)) {
            updateAgentTable();
        } else {
            // TODO: Show error to user, agent does not exist
        }
    }

    public void updateAgentTable() {
        agentTable.setItems(FXCollections.observableArrayList(project.getListAgent().getAgents()));
    }
}
