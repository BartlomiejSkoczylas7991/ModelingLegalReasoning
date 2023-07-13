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
        project.getListAgent().addAgent(agent);
        updateAgentTable();
    }

    public void updateAgentTable() {
        // Aktualizuj tabelę agentów na podstawie aktualnego projektu
        agentTable.setItems(FXCollections.observableArrayList(project.getListAgent().getAgents()));
    }

}
