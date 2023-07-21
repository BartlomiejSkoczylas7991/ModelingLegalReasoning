package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable.PropositionControllerObservable;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.IncompControllerObserver;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.PropositionControllerObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;

public class PropositionController implements PropositionControllerObservable {
    private TableView<Proposition> propositionTable;
    private ComboBox<Proposition> prop1comboBoxIncompProp;
    private ComboBox<Proposition> prop2comboBoxIncompProp;
    private ProjectController projectController;
    private Project project;
    private TextField propositionNameTextField;
    private final List<PropositionControllerObserver> observers = new ArrayList<>();


    public PropositionController(TableView<Proposition> propositionTable, ComboBox<Proposition> prop1comboBoxIncompProp,
                                 ComboBox<Proposition> prop2comboBoxIncompProp, TextField propositionNameTextField,
                                ProjectController projectController) {
        this.propositionTable = propositionTable;
        this.prop1comboBoxIncompProp = prop1comboBoxIncompProp;
        this.prop2comboBoxIncompProp = prop2comboBoxIncompProp;
        this.propositionNameTextField = propositionNameTextField;
        this.projectController = projectController;
        this.project = projectController.getProject();
    }

    public Project getProject() {
        return project;
    }

    public void addProposition(Proposition proposition) {
        if (!project.getListProposition().getListProposition().stream().anyMatch(existingProposition -> existingProposition.getStatement().equals(proposition.getStatement()))) {
            project.getListProposition().addProposition(proposition);
            updatePropositionTable();
            updateComboBoxes();
            notifyPropositionContrObservers();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Proposition with this name already exists!");

            alert.showAndWait();
        }
    }

    public void removeProposition(Proposition proposition) {
        if (project.getListProposition().removeProposition(proposition)) {
            updatePropositionTable();
            updateComboBoxes();
            notifyPropositionContrObservers();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Proposition doesn't exist!");

            alert.showAndWait();
        }
    }

    public void updatePropositionTable() {
        propositionTable.setItems(FXCollections.observableArrayList(project.getListProposition().getListProposition()));
    }

    public void handleAddProposition() {
        String propositionName = propositionNameTextField.getText(); // pobierz nazwę Propositiona z pola tekstowego

        if (!propositionName.isEmpty()) {
            Proposition newProposition = new Proposition(propositionName); // utwórz nową instancję Proposition
            addProposition(newProposition); // dodaj nowego Propositiona do projektu
            propositionNameTextField.clear(); // wyczyść pole tekstowe
            updatePropositionTable(); // zaktualizuj tabelę po dodaniu Proposition
        }
    }
    @FXML
    public void handleRemoveProposition() {
        Proposition selectedProposition = propositionTable.getSelectionModel().getSelectedItem();
        if (selectedProposition != null) {
            removeProposition(selectedProposition);
            updatePropositionTable(); // zaktualizuj tabelę po usunięciu Proposition
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No Proposition selected!");

            alert.showAndWait();
        }
    }

    private void updateComboBoxes() {
        ObservableList<Proposition> propositions = FXCollections.observableArrayList(project.getListProposition().getListProposition());
        prop1comboBoxIncompProp.setItems(propositions);
        prop2comboBoxIncompProp.setItems(propositions);
    }

    @Override
    public void addPropositionContrObserver(PropositionControllerObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removePropositionContrObserver(PropositionControllerObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyPropositionContrObservers() {
        for (PropositionControllerObserver propositionControllerObserver : observers) {
            propositionControllerObserver.updatePropositionController(this);
        }
    }
}
