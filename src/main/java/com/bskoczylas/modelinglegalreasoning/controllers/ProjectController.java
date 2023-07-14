package com.bskoczylas.modelinglegalreasoning.controllers;

import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.*;
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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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

        // for propositions
        @FXML
        private TableView<Proposition> propositionTable;

        private PropositionController propositionController;

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
                this.agentController = new AgentController(agentTable, this);
                agentTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);


                // Na początku przycisk "Generate" jest nieaktywny
                generateButton.setDisable(true);

                // Nasłuchiwanie zmian w projekcie
                project.addProjectObserver(new ProjectObserver() {
                        @Override
                        public void updateProject(Project project) {
                                updateGenerateButtonState();
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
                        }
                } else {
                        // TODO: Show error to user, no agent selected
                }
        }

        @FXML
        private void handleRemoveAgent(ActionEvent event) {
                Agent selectedAgent = agentTable.getSelectionModel().getSelectedItem();
                if (selectedAgent != null) {
                        agentController.removeAgent(selectedAgent);
                } else {
                        // TODO: Show error to user, no agent selected
                }
        }

        public void updateAgentTable() {
                Project currentProject = projectManager.getCurrentProject();
                if (currentProject != null) {
                        // Aktualizuj tabelę agentów na podstawie aktualnego projektu
                        //agentTable.setItems(FXCollections.observableArrayList(currentProject.getListAgent()));
                }
        }

        //DODAWANIE PROPOZYCJI POPRZEZ PropositionController


        //DODAWANIE WARTOŚCI POPRZEZ ValueController


        //DODAWANIA WAG AGENT-VALUE WEIGHTS (I SKALI) POPRZEZ AVController

        //DODAWANIE WAG AGENT-VALUE-PROPOSITION WEIGHTS (I SKALI) POPRZEZ AVPController

        //DODAWANIE INCOMPPROP i 2 decyzji w IncompPropController

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