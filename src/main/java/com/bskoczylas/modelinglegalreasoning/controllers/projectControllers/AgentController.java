package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Agent with this name already exists!");

            alert.showAndWait();
        }
    }

    public void editAgent(Agent oldAgent, Agent newAgent) {
        int index = project.getListAgent().getAgents().indexOf(oldAgent);
        if (index != -1) {
            project.getListAgent().getAgents().set(index, newAgent);
            updateAgentTable();
        } else {
            // Show error to user, agent does not exist
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Agent doesn't exist!");

            alert.showAndWait();
        }
    }

    public void removeAgent(Agent agent) {
        if (project.getListAgent().getAgents().remove(agent)) {
            updateAgentTable();
        } else {
            // TODO: Show error to user, agent does not exist
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Agent doesn't exist!");

            alert.showAndWait();
        }
    }

    public void handleAddAgent(TextField agentNameTextField) {
        String agentName = agentNameTextField.getText(); // pobierz nazwę agenta z pola tekstowego

        if (!agentName.isEmpty()) {
            Agent newAgent = new Agent(agentName); // utwórz nową instancję agenta
            addAgent(newAgent); // dodaj nowego agenta do projektu
            agentNameTextField.clear(); // wyczyść pole tekstowe
            updateAgentTable(); // zaktualizuj tabelę po dodaniu agenta
        }
    }

    public void handleEditAgent(TextField agentNameTextField) {
        Agent selectedAgent = agentTable.getSelectionModel().getSelectedItem();
        if (selectedAgent != null) {
            String agentName = agentNameTextField.getText();
            if (!agentName.isEmpty()) {
                Agent newAgent = new Agent(agentName);
                editAgent(selectedAgent, newAgent);
                agentNameTextField.clear();
                updateAgentTable(); // zaktualizuj tabelę po edycji agenta
            }
        } else {
            // TODO: Show error to user, no agent selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No agent selected!");

            alert.showAndWait();
        }
    }

    public void handleRemoveAgent() {
        Agent selectedAgent = agentTable.getSelectionModel().getSelectedItem();
        if (selectedAgent != null) {
            removeAgent(selectedAgent);
            updateAgentTable(); // zaktualizuj tabelę po usunięciu agenta
        } else {
            // TODO: Show error to user, no agent selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No agent selected!");

            alert.showAndWait();
        }
    }

    public void updateAgentTable() {
        agentTable.setItems(FXCollections.observableArrayList(project.getListAgent().getAgents()));
    }
}
