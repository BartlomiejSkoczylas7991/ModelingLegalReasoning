package com.bskoczylas.modelinglegalreasoning.controllers;

import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.*;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.dataStructures.AVPair;
import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Court.Report;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.IncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule.Rule;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AVP.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Weight;
import com.bskoczylas.modelinglegalreasoning.domain.models.projectObserver.ProjectObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.projectObserver.ProjectObserver;
import com.bskoczylas.modelinglegalreasoning.services.ProjectManager;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class ProjectController implements ProjectObserver {
        private ProjectManager projectManager;
        private Project project;

        @FXML
        private Button generatePDFButton;
        @FXML
        private Button newButton;
        @FXML
        private Button saveAsButton;
        @FXML
        private Button saveButton;
        @FXML
        private Button menuButton;
        @FXML
        private Button exitButton;
        @FXML
        private Button generateButton;

        // for agent
        @FXML
        private TableView<Agent> agentTable;
        @FXML
        private Button addAgentButton;
        @FXML
        private Button editAgentButton;
        @FXML
        private Button removeAgentButton;
        @FXML
        private TextField agentNameTextField;

        private AgentController agentController;

        // for values
        @FXML
        private TableView<Value> valueTable;
        @FXML
        private Button addValueButton;
        @FXML
        private Button editValueButton;
        @FXML
        private Button removeValueButton;
        @FXML
        private TextField valueNameTextField;

        private ValueController valueController;

        // for propositions
        @FXML
        private TableView<Proposition> propositionTable;

        private PropositionController propositionController;

        @FXML
        private Button addPropositionButton;

        @FXML
        private Button editPropositionButton;

        @FXML
        private Button removePropositionButton;

        @FXML
        private TextField propositionNameTextField;

        // for AV
        private AVController avController;
        private AgentValueToWeight avWeights;
        @FXML
        private TableView<AVPair> AVTable;
        @FXML
        private TableColumn<AVPair, Integer> AVIdColumn;
        @FXML
        private TableColumn<AVPair, String> AVAgentsColumn;
        @FXML
        private TableColumn<AVPair, String> AVValuesColumn;
        @FXML
        private TableColumn<AVPair, Integer> AVWeightsColumn;
        @FXML
        private Button aVAddButton;
        @FXML
        private Button aVEditButton;
        @FXML
        private Button aVRandomButton;
        @FXML
        private Button aVAddScaleButton;
        @FXML
        private Slider aVMinScale;
        @FXML
        private Slider aVMaxScale;
        @FXML
        private ComboBox<Weight> aVWeightsComboBox;

        // for AVP
        @FXML
        private TableView<AgentValuePropWeight> AVPTable;

        private AVPController avpController;

        // for IncompProp
        @FXML
        private TableView<IncompProp> incompPropTable;

        private IncompPropController incompPropController;

        @FXML
        private Button addIncompPropButton;

        @FXML
        private Button removeIncompPropButton;

        @FXML
        private ComboBox<Proposition> prop1comboBoxIncompProp;

        @FXML
        private ComboBox<Proposition> prop2comboBoxIncompProp;

        @FXML
        private RadioButton isDecisionRadioButton;

        // for Rules
        @FXML
        private TableView<Rule> rulesTable;

        private RuleController ruleController;

        private Report report = new Report();

        public void setProjectManager(ProjectManager projectManager) {
                this.projectManager = projectManager;
        }


        public void initialize() {
                // Inicjalizacja kontrolerów
                // Agent section
                this.agentController = new AgentController(this.agentTable, this.agentNameTextField,
                        this.addAgentButton, this.editAgentButton, this.removeAgentButton, this);
                agentTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // Value section
                this.valueController = new ValueController(valueTable, this.valueNameTextField,
                        this.addValueButton, this.editValueButton, this.removeValueButton,this);
                valueTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // Proposition section
                this.propositionController = new PropositionController(propositionTable,
                        prop1comboBoxIncompProp, prop2comboBoxIncompProp, this.propositionNameTextField,
                        this.addPropositionButton, this.editPropositionButton, this.removePropositionButton,this);
                propositionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // IncompProp section
                this.incompPropController = new IncompPropController(incompPropTable, this);

                // AgentValue Weights section
                this.avController = new AVController(
                        avWeights,
                        this,
                        AVTable,
                        AVIdColumn,
                        AVAgentsColumn,
                        AVValuesColumn,
                        AVWeightsColumn,
                        aVAddButton,
                        aVEditButton,
                        aVRandomButton,
                        aVAddScaleButton,
                        aVMinScale,
                        aVMaxScale,
                        aVWeightsComboBox
                );


                // Bind the table view to the data
                AVTable.setItems(FXCollections.observableArrayList(avWeights.keySet()));
                
                // Initialize the table columns


                AVIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getAgentValue().getId()).asObject());
                AVAgentsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAgentValue().getAgent().getName()));
                AVValuesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAgentValue().getValue().getName()));
                AVWeightsColumn.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getWeight()));


                // Initialize the sliders
                aVMinScale.valueProperty().addListener((obs, oldVal, newVal) -> {
                        if (newVal.doubleValue() > aVMaxScale.getValue() - 1) {
                                aVMinScale.setValue(oldVal.doubleValue());
                        }
                });

                aVMaxScale.valueProperty().addListener((obs, oldVal, newVal) -> {
                        if (newVal.doubleValue() < aVMinScale.getValue() + 1) {
                                aVMaxScale.setValue(oldVal.doubleValue());
                        }
                });

                // Na początku przycisk "Generate" jest nieaktywny
                generateButton.setDisable(true);

                // Nasłuchiwanie zmian w projekcie
                project.addProjectObserver(new ProjectObserver() {
                        @Override
                        public void updateProject(Project project) {
                                updateGenerateButtonState();
                                avController.updateAV(project.getAgentValueToWeight());
                        }
                });
        }

        //DODAWANIE PRZYCISKÓW GÓRNYCH ("GENERATE PDF", "NEW", "HELP", "SAVE", "MENU", "EXIT")

        //DODAWANIE AGENTÓW POPRZEZ PropositionController
        @FXML
        public void handleAddAgent(ActionEvent event) {
                this.agentController.handleAddAgent();
        }

        @FXML
        public void handleEditAgent(ActionEvent event) {
                this.agentController.handleEditAgent();
        }

        @FXML
        public void handleRemoveAgent(ActionEvent event) {
                this.agentController.handleRemoveAgent();
        }

        //DODAWANIE PROPOZYCJI POPRZEZ PropositionController
        @FXML
        public void handleAddProposition(ActionEvent event) {
                this.propositionController.handleAddProposition();
        }

        @FXML
        private void handleEditProposition(ActionEvent event) {
                this.propositionController.handleEditProposition();
        }

        @FXML
        private void handleRemoveProposition(ActionEvent event) {
                this.propositionController.handleRemoveProposition();
        }

        //DODAWANIE WARTOŚCI POPRZEZ ValueController
        @FXML
        public void handleAddValue(ActionEvent event) {
                this.valueController.handleAddValue();
        }

        @FXML
        private void handleEditValue(ActionEvent event) {
                this.valueController.handleEditValue();
        }

        @FXML
        private void handleRemoveValue(ActionEvent event) {
                this.valueController.handleRemoveValue();
        }

        //DODAWANIA WAG AGENT-VALUE WEIGHTS (I SKALI) POPRZEZ AVController
        @FXML
        private void handleaVAddScaleButton() {
                avController.changeScale();
                avController.updateAVTable();
        }

        @FXML
        private void handleaVRandomButton() {
                avController.randomizeWeights();
                avController.updateAVTable();
        }

        @FXML
        private void handleaVEditButton() {
                avController.editWeight();
                avController.updateAVTable();
        }

        @FXML
        private void handleaVAddButton() {
                avController.addWeight();
                avController.updateAVTable();
        }

        //DODAWANIE WAG AGENT-VALUE-PROPOSITION WEIGHTS (I SKALI) POPRZEZ AVPController

        //DODAWANIE INCOMPPROP i 2 decyzji w IncompPropController
        public void handleAddButtonAction(ActionEvent actionEvent) {
                // Pobierz wybrane wartości z obu ComboBoxów
                Proposition prop1 = prop1comboBoxIncompProp.getValue();
                Proposition prop2 = prop2comboBoxIncompProp.getValue();

                // Sprawdź, czy obie propozycje są różne i czy już nie istnieje para propozycji
                if(prop1 != null && prop2 != null && !prop1.equals(prop2)
                        && !project.getListIncompProp().getIncompPropList().contains(new IncompProp(new Pair<>(prop1, prop2), false))) { //nie ma contain
                        // Dodaj nową parę niekompatybilnych propozycji do listy
                        IncompProp incompProp = new IncompProp(new Pair<>(prop1, prop2), isDecisionRadioButton.isSelected());
                        project.getListIncompProp().addIncompatiblePropositions(incompProp);

                        // Zaktualizuj tabelę
                        incompPropController.updateIncompPropTable();
                        // Sprawdź stan przycisku decyzji
                        incompPropController.checkDecision();
                }
        }


        public void handleRemoveButtonAction(ActionEvent actionEvent) {
                // Pobierz wybrany element z tabeli
                IncompProp selectedIncompProp = incompPropTable.getSelectionModel().getSelectedItem();

                // Usuń wybrany element z listy
                if(selectedIncompProp != null) {
                        project.getListIncompProp().removeIncompProp(selectedIncompProp);

                        // Zaktualizuj tabelę
                        incompPropController.updateIncompPropTable();
                        // Sprawdź stan przycisku decyzji
                        incompPropController.checkDecision();
                }
        }


        //DODAWANIE Zasad w RuleController, gdzie także dodajemy kolejne okienko do dodawania zasad.






        // GENEROWANIE PDF i "GENERATE" (W Javafx) dopier po spełnieniu warunków:
        // minimum 2 agentów, 2 wartości, 4 propozycje, 1 para decyzji (zwykłe incompProp niekonieczne), 2 zasady
        @FXML
        private void handleGenerate() {
                if (project.hasEnoughData()) {
                        // Generuj raport
                        List<Report.ReportSection> reportSections = report.generateReport();
                        String formattedReport = report.formatForJavaFX(reportSections);
                        reportTextArea.setText(formattedReport);
                }
        }

        @FXML
        private void handleGeneratePDF() {
                if (project.hasEnoughData()) {
                        // Tutaj implementacja generowania PDFa z raportem
                }
        }

        @FXML
        private void handleNew() {
                // Reset projektu
                project.reset();
                reportTextArea.clear();
        }

        //POMOC
        @FXML
        private void handleHelp() {
                // Wyświetl okno pomocy
                // Tutaj może być wywołanie metody, która otwiera nowe okno z instrukcjami korzystania z programu
        }

        //ZAPIS
        @FXML
        private void handleSave() {
                try {
                        // Zapisz zmiany w projekcie
                        projectManager.saveProject(project);
                } catch (IOException e) {
                        // Tutaj obsłuż wyjątek - na przykład wyświetl dialog z informacją o błędzie
                        e.printStackTrace();
                }
        }

        //POWRÓT DO MENU
        @FXML
        private void handleMenu() {
                // Otwórz okno startowe
                projectManager.openStartWindow();
        }

        //WYJŚCIE Z PROGRAMU
        @FXML
        private void handleExit() {
                // Zakończ program
                System.exit(0);
        }

        @FXML
        private void handleGenerate() {
                // Generuj raport
                List<Report.ReportSection> reportSections = report.generateReport();
                String formattedReport = report.formatForJavaFX(reportSections);
                reportTextArea.setText(formattedReport);
        }

        private void updateGenerateButtonState() {
                // Sprawdź, czy wprowadzono wystarczająco dużo danych, aby można było wygenerować raport
                boolean canGenerate = project.hasEnoughData();
                generateButton.setDisable(!canGenerate);
        }

        public void setProject(Project project) {
                this.project = project;
                this.project.addProjectObserver(this);
        }

        @Override
        public void updateProject(Project project) {

        }

        public Project getProject() {
                return this.project;
        }


}