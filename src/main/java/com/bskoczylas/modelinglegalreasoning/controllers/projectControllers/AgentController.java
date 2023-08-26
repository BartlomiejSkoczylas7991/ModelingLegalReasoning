package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable.AgContrObservable;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.AgContrObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.AgentObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AgentObserver;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class AgentController implements AgContrObservable {
    private TableView<Agent> agentTable;
    private Project project;
    private TextField agentNameTextField;
    private final List<AgContrObserver> observers = new ArrayList<>();

    public AgentController(ProjectController projectController) {
        this.agentTable = projectController.getAgentTable();
        this.agentNameTextField = projectController.getAgentNameTextField();
        this.project = projectController.getProject();
    }

    public Project getProject() {
        return project;
    }

    private void addAgent(Agent agent) {
        if (!project.getListAgent().getAgents().stream().anyMatch(existingAgent -> existingAgent.getName().equals(agent.getName()))) {
            project.getListAgent().addAgent(agent);
            updateAgentTable();
            notifyAgentContrObservers();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Agent with this name already exists!");

            alert.showAndWait();
        }
    }

    public void removeAgent(Agent agent) {
        if (project.getListAgent().removeAgent(agent)) {
            updateAgentTable();
            notifyAgentContrObservers();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Agent doesn't exist!");

            alert.showAndWait();
        }
    }

    public void handleAddAgent() {
        String agentName = agentNameTextField.getText().trim(); // pobierz nazwę agenta z pola tekstowego

        if (!agentName.isEmpty()) {
            Agent newAgent = new Agent(agentName); // utwórz nową instancję agenta
            addAgent(newAgent); // dodaj nowego agenta do projektu
            agentNameTextField.clear(); // wyczyść pole tekstowe
            updateAgentTable(); // zaktualizuj tabelę po dodaniu agenta
        }
    }
    @FXML
    public void handleRemoveAgent() {
        Agent selectedAgent = agentTable.getSelectionModel().getSelectedItem();
        if (selectedAgent != null) {
            removeAgent(selectedAgent);
            notifyAgentContrObservers();
            updateAgentTable(); // zaktualizuj tabelę po usunięciu agenta
        } else {
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


    @Override
    public void addAgentContrObserver(AgContrObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeAgentContrObserver(AgContrObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAgentContrObservers() {
        for (AgContrObserver observer : this.observers) {
            observer.updateAgentController(this);
        }
    }
}
