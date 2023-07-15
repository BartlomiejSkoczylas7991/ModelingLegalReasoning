package com.bskoczylas.modelinglegalreasoning.controllers;

import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.*;
import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Court.Report;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.IncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule.Rule;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AVP.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.projectObserver.ProjectObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.projectObserver.ProjectObserver;
import com.bskoczylas.modelinglegalreasoning.services.ProjectManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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

        private AgentController agentController;

        @FXML
        private Button addAgentButton;

        @FXML
        private Button editAgentButton;

        @FXML
        private Button removeAgentButton;

        @FXML
        private TextField agentNameTextField;

        // for values
        @FXML
        private TableView<Value> valueTable;

        private ValueController valueController;

        @FXML
        private Button addValueButton;

        @FXML
        private Button editValueButton;

        @FXML
        private Button removeValueButton;

        @FXML
        private TextField valueNameTextField;

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
        @FXML
        private TableView<AgentValueToWeight> AVTable;

        private AVController avController;

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
                this.agentController = new AgentController(agentTable, this);
                agentTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // Dodanie funkcji obsługującej dla przycisków
                addAgentButton.setOnAction(event -> agentController.handleAddAgent(agentNameTextField));
                editAgentButton.setOnAction(event -> agentController.handleEditAgent(agentNameTextField));
                removeAgentButton.setOnAction(event -> agentController.handleRemoveAgent());


                // Value section
                this.valueController = new ValueController(valueTable, this);
                valueTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // Proposition section
                this.propositionController = new PropositionController(propositionTable, prop1comboBoxIncompProp, prop2comboBoxIncompProp, this);
                propositionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // IncompProp section
                this.incompPropController = new IncompPropController(incompPropTable, this);

                // AgentValue Weights section
                this.avController = new AVController(AVTable, this);

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


        // DODAWANIE AGENTÓW POPRZEZ AGENTCONTROLLER
        @FXML
        public void handleAddAgent(ActionEvent event) {
                String agentName = agentNameTextField.getText(); // pobierz nazwę agenta z pola tekstowego

                if (!agentName.isEmpty()) {
                        Agent newAgent = new Agent(agentName); // utwórz nową instancję agenta
                        agentController.addAgent(newAgent); // dodaj nowego agenta do projektu
                        agentNameTextField.clear(); // wyczyść pole tekstowe
                        agentController.updateAgentTable(); // zaktualizuj tabelę po dodaniu agenta
                }
        }

        @FXML
        private void handleEditAgent(ActionEvent event) {
                Agent selectedAgent = agentTable.getSelectionModel().getSelectedItem();
                if (selectedAgent != null) {
                        String agentName = agentNameTextField.getText();
                        if (!agentName.isEmpty()) {
                                Agent newAgent = new Agent(agentName);
                                agentController.editAgent(selectedAgent, newAgent);
                                agentNameTextField.clear();
                                agentController.updateAgentTable(); // zaktualizuj tabelę po edycji agenta
                        }
                } else {
                        // TODO: Show error to user, no agent selected
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("No agent selected!");

                        alert.showAndWait();
                }
        }

        @FXML
        private void handleRemoveAgent(ActionEvent event) {
                Agent selectedAgent = agentTable.getSelectionModel().getSelectedItem();
                if (selectedAgent != null) {
                        agentController.removeAgent(selectedAgent);
                        agentController.updateAgentTable(); // zaktualizuj tabelę po usunięciu agenta
                } else {
                        // TODO: Show error to user, no agent selected
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("No agent selected!");

                        alert.showAndWait();
                }
        }

        public void updateAgentTable() {
                Project currentProject = projectManager.getCurrentProject();
                if (currentProject != null) {
                        // Aktualizuj tabelę agentów na podstawie aktualnego projektu
                        agentTable.setItems(FXCollections.observableArrayList(currentProject.getListAgent().getAgents()));
                }
        }

        //DODAWANIE PROPOZYCJI POPRZEZ PropositionController
        @FXML
        public void handleAddProposition(ActionEvent event) {
                String propositionName = propositionNameTextField.getText(); // pobierz nazwę proposition z pola tekstowego

                if (!propositionName.isEmpty()) {
                        Proposition newProposition = new Proposition(propositionName); // utwórz nową instancję proposition
                        propositionController.addProposition(newProposition); // dodaj nowego proposition do projektu
                        propositionNameTextField.clear(); // wyczyść pole tekstowe
                        propositionController.updatePropositionTable(); // zaktualizuj tabelę po dodaniu proposition
                }
        }

        @FXML
        private void handleEditProposition(ActionEvent event) {
                Proposition selectedProposition = propositionTable.getSelectionModel().getSelectedItem();
                if (selectedProposition != null) {
                        String propositionName = propositionNameTextField.getText();
                        if (!propositionName.isEmpty()) {
                                Proposition newProposition = new Proposition(propositionName);
                                propositionController.editProposition(selectedProposition, newProposition);
                                propositionNameTextField.clear();
                                propositionController.updatePropositionTable(); // zaktualizuj tabelę po edycji proposition
                        }
                } else {
                        // TODO: Show error to user, no proposition selected
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("No proposition selected!");

                        alert.showAndWait();
                }
        }

        @FXML
        private void handleRemoveProposition(ActionEvent event) {
                Proposition selectedProposition = propositionTable.getSelectionModel().getSelectedItem();
                if (selectedProposition != null) {
                        propositionController.removeProposition(selectedProposition);
                        propositionController.updatePropositionTable(); // zaktualizuj tabelę po usunięciu proposition
                } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("No proposition selected!");

                        alert.showAndWait();
                }
        }

        public void updatePropositionTable() {
                Project currentProject = projectManager.getCurrentProject();
                if (currentProject != null) {
                        // Aktualizuj tabelę propositionów na podstawie aktualnego projektu
                        propositionTable.setItems(FXCollections.observableArrayList(currentProject.getListProposition().getListProposition()));
                }
        }

        //DODAWANIE WARTOŚCI POPRZEZ ValueController
        @FXML
        public void handleAddValue(ActionEvent event) {
                String valueName = valueNameTextField.getText(); // pobierz nazwę proposition z pola tekstowego

                if (!valueName.isEmpty()) {
                        Value newValue = new Value(valueName); // utwórz nową instancję Value
                        valueController.addValue(newValue); // dodaj nowego Value do projektu
                        valueNameTextField.clear(); // wyczyść pole tekstowe
                        valueController.updateValueTable(); // zaktualizuj tabelę po dodaniu Value
                }
        }

        @FXML
        private void handleEditValue(ActionEvent event) {
                Value selectedValue = valueTable.getSelectionModel().getSelectedItem();
                if (selectedValue != null) {
                        String valueName = valueNameTextField.getText();
                        if (!valueName.isEmpty()) {
                                Value newValue = new Value(valueName);
                                valueController.editValue(selectedValue, newValue);
                                valueNameTextField.clear();
                                valueController.updateValueTable(); // zaktualizuj tabelę po edycji proposition
                        }
                } else {
                        // TODO: Show error to user, no value selected
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("No value selected!");

                        alert.showAndWait();
                }
        }

        @FXML
        private void handleRemoveValue(ActionEvent event) {
                Value selectedValue = valueTable.getSelectionModel().getSelectedItem();
                if (selectedValue != null) {
                        valueController.removeValue(selectedValue);
                        valueController.updateValueTable(); // zaktualizuj tabelę po usunięciu value
                } else {
                        // TODO: Show error to user, no proposition selected
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("No value selected!");

                        alert.showAndWait();
                }
        }

        public void updateValueTable() {
                Project currentProject = projectManager.getCurrentProject();
                if (currentProject != null) {
                        // Aktualizuj tabelę propositionów na podstawie aktualnego projektu
                        valueTable.setItems(FXCollections.observableArrayList(currentProject.getListValue().getValues()));
                }
        }

        //DODAWANIA WAG AGENT-VALUE WEIGHTS (I SKALI) POPRZEZ AVController

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