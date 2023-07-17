package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class PropositionController {
    private TableView<Proposition> propositionTable;
    private ComboBox<Proposition> prop1comboBoxIncompProp;
    private ComboBox<Proposition> prop2comboBoxIncompProp;
    private ProjectController projectController;
    private Project project;
    private TextField propositionNameTextField;

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

    public void addProposition(Proposition proposition) {
        if (!project.getListProposition().getListProposition().stream().anyMatch(existingProposition -> existingProposition.getStatement().equals(proposition.getStatement()))) {
            project.getListProposition().addProposition(proposition);
            updatePropositionTable();
            updateComboBoxes();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Proposition with this name already exists!");

            alert.showAndWait();
        }
    }

    public void editProposition(Proposition oldProposition, Proposition newProposition) {
        int index = project.getListProposition().getListProposition().indexOf(oldProposition);
        if (index != -1) {
            project.getListProposition().getListProposition().set(index, newProposition);
            updatePropositionTable();
            updateComboBoxes();
        } else {
            // Show error to user, proposition does not exist
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Proposition doesn't exist!");

            alert.showAndWait();
        }
    }

    public void removeProposition(Proposition proposition) {
        if (project.getListProposition().getListProposition().remove(proposition)) {
            updatePropositionTable();
            updateComboBoxes();
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

    public void handleEditProposition() {
        Proposition selectedProposition = propositionTable.getSelectionModel().getSelectedItem();
        if (selectedProposition != null) {
            String propositionName = propositionNameTextField.getText();
            if (!propositionName.isEmpty()) {
                Proposition newProposition = new Proposition(propositionName);
                editProposition(selectedProposition, newProposition);
                propositionNameTextField.clear();
                updatePropositionTable(); // zaktualizuj tabelę po edycji Proposition
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No Proposition selected!");

            alert.showAndWait();
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
}
