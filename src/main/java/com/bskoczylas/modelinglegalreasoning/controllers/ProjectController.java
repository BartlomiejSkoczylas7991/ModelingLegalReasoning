package com.bskoczylas.modelinglegalreasoning.controllers;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.ProjectManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;


public class ProjectController {
        private ProjectManager projectManager;

        @FXML
        private TableView<Agent> agentTable;

        @FXML
        private Button addAgentButton;

        public void setProjectManager(ProjectManager projectManager) {
                this.projectManager = projectManager;
        }

        @FXML
        public void handleAddAgent(ActionEvent event) {
                Project currentProject = projectManager.getCurrentProject();
                if (currentProject != null) {
                        // Dodaj agenta do aktualnego projektu
                        //currentProject.getListAgent().add(new Agent(/* parametry agenta */));
                        // Aktualizuj tabelę agentów
                        //updateAgentTable();
                }
        }

        public void updateAgentTable() {
                Project currentProject = projectManager.getCurrentProject();
                if (currentProject != null) {
                        // Aktualizuj tabelę agentów na podstawie aktualnego projektu
                        //agentTable.setItems(FXCollections.observableArrayList(currentProject.getListAgent()));
                }
        }

        // Pozostałe metody...
}