package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.IncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;

public class IncompPropController {
    @FXML
    private TableView<IncompProp> incompPropTable;

    @FXML
    private ComboBox<Proposition> comboBox1;

    @FXML
    private ComboBox<Proposition> comboBox2;

    @FXML
    private RadioButton isDecisionButton;

    private ProjectController projectController;
    private Project project;

    public IncompPropController(TableView<IncompProp> incompPropTable, ProjectController projectController) {
        this.incompPropTable = incompPropTable;
        this.projectController = projectController;
        this.project = projectController.getProject();

        checkDecision(); // call this method to initialize the state of the button
    }

    @FXML
    public void handleAddButtonAction(ActionEvent event) {
        Proposition prop1 = comboBox1.getValue();
        Proposition prop2 = comboBox2.getValue();

        if(prop1 != null && prop2 != null && !prop1.equals(prop2)) {
            ListIncompProp listIncompProp = project.getListIncompProp();
            IncompProp incompProp = new IncompProp(new Pair<>(prop1, prop2), isDecisionButton.isSelected());
            listIncompProp.addIncompatiblePropositions(incompProp);
            updateIncompPropTable();

            checkDecision(); // call this method after adding a new IncompProp
        } else {
            // Show error to user, invalid propositions selected
        }
    }

    @FXML
    public void handleEditButtonAction(ActionEvent event) {
        IncompProp selectedIncompProp = incompPropTable.getSelectionModel().getSelectedItem();
        // Update the selectedIncompProp as necessary
    }

    @FXML
    public void handleRemoveButtonAction(ActionEvent event) {
        IncompProp selectedIncompProp = incompPropTable.getSelectionModel().getSelectedItem();
        project.getListIncompProp().removeIncompProp(selectedIncompProp);
        updateIncompPropTable();

        checkDecision(); // call this method after removing an IncompProp
    }

    public void updateIncompPropTable() {
        incompPropTable.setItems(FXCollections.observableArrayList(project.getListIncompProp().getIncompatiblePropositions()));
    }

    private void checkDecision() {
        // If there is already a decision, disable the RadioButton.
        // Otherwise, enable it.
        if(project.getListIncompProp()
                .getIncompatiblePropositions()
                .stream()
                .anyMatch(IncompProp::isDecision)) {
            isDecisionButton.setDisable(true);
        } else {
            isDecisionButton.setDisable(false);
        }
    }
}