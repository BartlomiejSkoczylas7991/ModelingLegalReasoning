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
import javafx.scene.control.Alert;
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

    public IncompPropController(TableView<IncompProp> incompPropTable, ProjectController projectController, Project project
                                ,ComboBox<Proposition> prop1comboBoxIncompProp, ComboBox<Proposition> prop2comboBoxIncompProp,
                                RadioButton isDecisionRadioButton) {
        this.incompPropTable = incompPropTable;
        this.projectController = projectController;
        this.project = project;
        this.prop1comboBoxIncompProp = prop1comboBoxIncompProp;
        this.prop2comboBoxIncompProp = prop2comboBoxIncompProp;
        this.isDecisionRadioButton = isDecisionRadioButton;

        checkDecision();
        isDecisionRadioButton.setSelected(false);// call this method to initialize the state of the button
    }

    public void handleAddButtonAction(ActionEvent event) {
        Proposition prop1 = prop1comboBoxIncompProp.getValue();
        Proposition prop2 = prop2comboBoxIncompProp.getValue();

        if (prop1 != null && prop2 != null && !prop1.equals(prop2)) {
            Pair<Proposition, Proposition> newPair = new Pair<>(prop1, prop2);
            Pair<Proposition, Proposition> reversedPair = new Pair<>(prop2, prop1);
            boolean alreadyExists = project.getListIncompProp().getIncompatiblePropositions().stream()
                    .anyMatch(ip -> ip.getPropositionsPair().equals(newPair) || ip.getPropositionsPair().equals(reversedPair));

            boolean isPartOfDecision = project.getListIncompProp().getIncompatiblePropositions().stream()
                    .anyMatch(ip -> (ip.isDecision() || isDecisionRadioButton.isSelected()) &&
                            (ip.getPropositionsPair().getKey().equals(prop1) || ip.getPropositionsPair().getValue().equals(prop1) ||
                                    ip.getPropositionsPair().getKey().equals(prop2) || ip.getPropositionsPair().getValue().equals(prop2)));

            if (!alreadyExists && !isPartOfDecision) {
                ListIncompProp listIncompProp = project.getListIncompProp();
                IncompProp incompProp = new IncompProp(newPair, isDecisionRadioButton.isSelected());
                listIncompProp.addIncompatiblePropositions(incompProp);
                updateIncompPropTable();

                checkDecision(); // call this method after adding a new IncompProp
                isDecisionRadioButton.setSelected(false); // uncheck the RadioButton after adding a decision
                notifyIncompContrObservers();
            } else {
                // Show error to user, pair already exists or is part of decision
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Pair already exists or is part of decision");

                alert.showAndWait();
            }
        } else {
            // Show error to user, invalid propositions selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid propositions selected");

            alert.showAndWait();
        }
    }

    public Project getProject() {
        return project;
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

    public void removeIncompPropsIncludingProposition(Proposition proposition) {
        List<IncompProp> listIncompProp = project.getListIncompProp().getIncompatiblePropositions();
        List<IncompProp> toRemove = new ArrayList<>();

        for (IncompProp ip : listIncompProp) {
            if (ip.getPropositionsPair().getKey().equals(proposition) || ip.getPropositionsPair().getValue().equals(proposition)) {
                // Znaleziono IncompProp zawierające usuniętą propozycję
                toRemove.add(ip);
            }
        }

        // Usunięcie IncompProp zawierających usuniętą propozycję
        listIncompProp.removeAll(toRemove);
        updateIncompPropTable();
        notifyIncompContrObservers();
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