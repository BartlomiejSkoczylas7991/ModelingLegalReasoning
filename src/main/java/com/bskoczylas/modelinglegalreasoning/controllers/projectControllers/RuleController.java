package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable.RuleControllerObservable;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.RuleControllerObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.IncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AgentObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RuleController implements RuleControllerObservable {
    private ProjectController projectController;
    private ListRules listRules;
    @FXML
    private TableView<Proposition> premisesTable;
    private Set<Proposition> premises = new HashSet<>();
    @FXML
    private Label labelTextArea;
    private List<Proposition> availablePropositions = new ArrayList<>();
    private Proposition decision1;
    private Proposition decision2;
    private List<IncompProp> incompPropList;
    @FXML
    private TableColumn<Proposition, String> premisesColumn;
    private final List<RuleControllerObserver> observers = new ArrayList<>();
    private ToggleGroup group;
    private ObservableList<Proposition> allPropositionsObservableList = FXCollections.observableArrayList();
    private ObservableList<Proposition> selectedPropositionsObservableList = FXCollections.observableArrayList();

    @FXML
    private Button addRuleButton;
    @FXML
    private RadioButton decisionRadioButton1;
    @FXML
    private RadioButton decisionRadioButton2;
    @FXML
    private ComboBox premisesComboBox;


    public RuleController(ProjectController projectController) {
        this.projectController = projectController;
        this.listRules = projectController.getProject().getListRules();
        this.incompPropList = projectController.getProject().getListIncompProp().getIncompatiblePropositions();
        availablePropositions = projectController.getProject().getListProposition().getPropositionsNotDecisions();
    }

    public ListRules getListRules() {
        return listRules;
    }

    public void setIncompPropList(List<IncompProp> incompPropList) {
        this.incompPropList = incompPropList;
    }

    @FXML
    public void initialize() {
        allPropositionsObservableList.addAll(availablePropositions);

        premisesColumn.setCellValueFactory(new PropertyValueFactory<>("statement"));
        premisesTable.setItems(selectedPropositionsObservableList);

        premisesComboBox.setItems(allPropositionsObservableList);

        group = new ToggleGroup();
        decisionRadioButton1.setToggleGroup(group);
        decisionRadioButton2.setToggleGroup(group);
        this.group = group;

        projectController.getProject().getListIncompProp().getIncompatiblePropositions();
    }

    @FXML
    public void handleAddRuleButtonAction(ActionEvent actionEvent) {
        RadioButton selectedDecisionButton = (RadioButton) group.getSelectedToggle();

        if (selectedDecisionButton == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You must select a conclusion!");

            alert.showAndWait();
            return;
        }

        Proposition selectedDecision = selectedDecisionButton == decisionRadioButton1 ? decision1 : decision2;

        if (premises.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("You must select at least one premise to create a rule!");

            alert.showAndWait();
            return;
        }

        boolean ruleExists = false;

        for (Rule existingRule : listRules.getListRules()) {
            for (Proposition premise : premises) {
                if (existingRule.getPremises().contains(premise)) {
                    ruleExists = true;
                    break;
                }
            }
        }

        if (!ruleExists) {
            Rule newRule = new Rule(premises, selectedDecision);
            listRules.addRule(newRule);

            Toggle selectedToggle = group.getSelectedToggle();
            if (selectedToggle != null) {
                selectedToggle.setSelected(false);
            }

            notifyRuleContrObservers();
            ((Node)(actionEvent.getSource())).getScene().getWindow().hide();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("The rule with the same premises already exists!");
            alert.showAndWait();
        }

    }

    @FXML
    public void handleRemovePremiseButtonAction(ActionEvent event) {
        Proposition selectedPremise = premisesTable.getSelectionModel().getSelectedItem();

        if (selectedPremise != null && premises.contains(selectedPremise)) {
            premises.remove(selectedPremise);
            selectedPropositionsObservableList.remove(selectedPremise);
            availablePropositions.add(selectedPremise);
        }
    }

    @FXML
    public void handleAddPremiseButtonAction(ActionEvent event) {
        Proposition selectedPremise = (Proposition) premisesComboBox.getSelectionModel().getSelectedItem();
        boolean isCompatible = true;
        for (IncompProp incompProp : projectController.getProject().getListIncompProp().getIncompatiblePropositions()) {
            Pair<Proposition, Proposition> pair = incompProp.getPropositionsPair();
            if (pair.getKey().equals(selectedPremise) && selectedPropositionsObservableList.contains(pair.getValue()) ||
                    pair.getValue().equals(selectedPremise) && selectedPropositionsObservableList.contains(pair.getKey())) {
                isCompatible = false;
                break;
            }
        }

        if (!isCompatible) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("The chosen proposition is incompatible with an already chosen one!");

            alert.showAndWait();
        } else {
            if (selectedPremise != null && availablePropositions.contains(selectedPremise)) {
                premises.add(selectedPremise);
                selectedPropositionsObservableList.add(selectedPremise);
                availablePropositions.remove(selectedPremise);
                premisesTable.refresh();
            }
        }
    }

    public void setDecisions(Proposition decision1, Proposition decision2) {
        this.decision1 = decision1;
        this.decision2 = decision2;

        decisionRadioButton1.setText(decision1.getStatement());
        decisionRadioButton2.setText(decision2.getStatement());
    }

    @Override
    public void addRuleContrObserver(RuleControllerObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeRuleContrObserver(RuleControllerObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyRuleContrObservers() {
        for (RuleControllerObserver ruleControllerObserver : observers) {
            ruleControllerObserver.updateRuleController(this);
        }
    }
}
