package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable.RuleControllerObservable;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.RuleControllerObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AgentObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.ListRules;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class RuleController implements RuleControllerObservable {
    private ProjectController projectController;
    private ListRules listRules;
    @FXML
    private TableView<Proposition> premisesTable;
    private List<Proposition> premises;
    @FXML
    private Label labelTextArea;
    private Proposition decision1;
    private Proposition decision2;
    @FXML
    private TableColumn<Proposition, String> premisesColumn;
    private final List<RuleControllerObserver> observers = new ArrayList<>();


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
        this.listRules = projectController.getListRules();  // Zakładając, że ProjectController posiada getter dla ListRules
        premises = projectController.getProject().getListProposition().getPropositionsNotDecisions();
    }

    @FXML
    public void initialize() {
        ObservableList<Proposition> premisesObservableList = FXCollections.observableArrayList(premises);

        // Inicjalizacja tabeli
        premisesColumn.setCellValueFactory(new PropertyValueFactory<>("statement"));
        premisesTable.setItems(premisesObservableList);

        // Inicjalizacja ComboBoxa
        premisesComboBox.setItems(premisesObservableList);

        premisesTable.setItems(premisesObservableList);
    }

    @FXML
    private void handleEnterKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            // Tutaj możesz wykonać dowolne działania po naciśnięciu Enter
            String text = labelTextArea.getText();
            System.out.println("Wprowadzony tekst: " + text);
        }
    }

    @FXML
    public void handleAddPremiseButtonAction(ActionEvent event) {
        // Pobierz wybraną propozycję
        Proposition selectedPremise = (Proposition) premisesComboBox.getSelectionModel().getSelectedItem();

        // Dodaj propozycję do listy przesłanek
        if (selectedPremise != null) {
            premises.add(selectedPremise);
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
