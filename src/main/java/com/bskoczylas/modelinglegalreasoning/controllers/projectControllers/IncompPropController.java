package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable.IncompControllerObservable;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.IncompControllerObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.IncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AgentObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;

public class IncompPropController implements IncompControllerObservable {
    private TableView<IncompProp> incompPropTable;
    private ComboBox<Proposition> prop1comboBoxIncompProp;
    private ComboBox<Proposition> prop2comboBoxIncompProp;
    private RadioButton isDecisionRadioButton;
    private ProjectController projectController;
    private Project project;
    private final List<IncompControllerObserver> observers = new ArrayList<>();

    public IncompPropController(TableView<IncompProp> incompPropTable, ProjectController projectController, Project project,
                                RadioButton isDecisionRadioButton) {
        this.incompPropTable = incompPropTable;
        this.projectController = projectController;
        this.project = project;
        this.isDecisionRadioButton = isDecisionRadioButton;

        checkDecision(); // call this method to initialize the state of the button
    }

    public void handleAddButtonAction(ActionEvent event) {
        Proposition prop1 = prop1comboBoxIncompProp.getValue();
        Proposition prop2 = prop2comboBoxIncompProp.getValue();

        if(prop1 != null && prop2 != null && !prop1.equals(prop2)
                && !project.getListIncompProp().getIncompatiblePropositions().contains(new IncompProp(new Pair<>(prop1, prop2), false))) {
            ListIncompProp listIncompProp = project.getListIncompProp();
            IncompProp incompProp = new IncompProp(new Pair<>(prop1, prop2), isDecisionRadioButton.isSelected());
            listIncompProp.addIncompatiblePropositions(incompProp);
            updateIncompPropTable();

            checkDecision(); // call this method after adding a new IncompProp
        } else {
            // Show error to user, invalid propositions selected
        }
    }

    public void handleRemoveButtonAction(ActionEvent event) {
        IncompProp selectedIncompProp = incompPropTable.getSelectionModel().getSelectedItem();
        project.getListIncompProp().removeIncompProp(selectedIncompProp);
        updateIncompPropTable();

        checkDecision(); // call this method after removing an IncompProp
    }

    public void updateIncompPropTable() {
        incompPropTable.setItems(FXCollections.observableArrayList(project.getListIncompProp().getIncompatiblePropositions()));
    }

    public void checkDecision() {
        // If there is already a decision, disable the RadioButton.
        // Otherwise, enable it.
        if(project.getListIncompProp()
                .getIncompatiblePropositions()
                .stream()
                .anyMatch(IncompProp::isDecision)) {
            isDecisionRadioButton.setDisable(true);
        } else {
            isDecisionRadioButton.setDisable(false);
        }
    }

    @Override
    public void addIncompContrObserver(IncompControllerObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeIncompContrObserver(IncompControllerObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyIncompContrObservers() {
        for (IncompControllerObserver observer : this.observers) {
            observer.updateIncompController(this);
        }
    }
}