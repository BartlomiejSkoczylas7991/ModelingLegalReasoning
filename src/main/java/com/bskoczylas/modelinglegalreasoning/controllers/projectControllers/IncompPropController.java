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
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IncompPropController implements IncompControllerObservable {
    private TableView<IncompProp> incompPropTable;
    private ComboBox<Proposition> prop1comboBoxIncompProp;
    private final ComboBox<Proposition> prop2comboBoxIncompProp;
    private RadioButton isDecisionRadioButton;
    private ProjectController projectController;
    private Project project;
    private ListRules listRules;
    private final List<IncompControllerObserver> observers = new ArrayList<>();

    public IncompPropController(TableView<IncompProp> incompPropTable, ProjectController projectController, Project project
                                ,ComboBox<Proposition> prop1comboBoxIncompProp, ComboBox<Proposition> prop2comboBoxIncompProp,
                                RadioButton isDecisionRadioButton) {
        this.incompPropTable = incompPropTable;
        this.projectController = projectController;
        this.project = project;
        this.listRules = project.getListRules();
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
            boolean conflictingRules = checkConflictingRules(prop1, prop2);

            if (!alreadyExists && !isPartOfDecision) {
                ListIncompProp listIncompProp = project.getListIncompProp();
                IncompProp incompProp = new IncompProp(newPair, isDecisionRadioButton.isSelected());
                listIncompProp.addIncompatiblePropositions(incompProp);
                updateIncompPropTable();

                checkDecision();
                isDecisionRadioButton.setSelected(false);
                notifyIncompContrObservers();
            } else {
                // Show error to user
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                if (alreadyExists || isPartOfDecision) {
                    alert.setContentText("Pair already exists or is part of decision");
                } else if (conflictingRules) {
                    alert.setContentText("Pair has conflicting rules");
                } else {
                    alert.setContentText("Invalid propositions selected");
                }
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
                toRemove.add(ip);
            }
        }

        listIncompProp.removeAll(toRemove);
        updateIncompPropTable();
        notifyIncompContrObservers();
    }

    private boolean containsPropositions(Rule rule, Proposition prop1, Proposition prop2) {
        return rule.getPremises().contains(prop1) && rule.getPremises().contains(prop2);
    }

    private boolean checkConflictingRules(Proposition prop1, Proposition prop2) {
        List<Rule> listRules = this.listRules.getListRules();

        for (Rule rule : listRules) {
            if (containsPropositions(rule, prop1, prop2)) {
                return true;
            }
        }

        Map<Proposition, List<Rule>> conclusionToRulesMap = listRules.stream()
                .collect(Collectors.groupingBy(Rule::getConclusion));

        for (Proposition conclusion : conclusionToRulesMap.keySet()) {
            List<Rule> rulesWithSameConclusion = conclusionToRulesMap.get(conclusion);

            for (Rule rule1 : rulesWithSameConclusion) {
                for (Rule rule2 : rulesWithSameConclusion) {
                    if (!rule1.equals(rule2)
                            && rule1.getPremises().contains(prop1)
                            && rule2.getPremises().contains(prop2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public ListRules getListRules() {
        return listRules;
    }

    public void setListRules(ListRules listRules) {
        this.listRules = listRules;
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