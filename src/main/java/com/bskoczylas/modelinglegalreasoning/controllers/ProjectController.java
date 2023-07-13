package com.bskoczylas.modelinglegalreasoning.controllers;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Court.Report;
import com.bskoczylas.modelinglegalreasoning.services.ProjectManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;

import java.util.List;


public class ProjectController {
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

        private Report report = new Report();

        @FXML
        private TableView<Agent> agentTable;

        @FXML
        private Button addAgentButton;

        public void setProjectManager(ProjectManager projectManager) {
                this.projectManager = projectManager;
        }
        public void setProject(Project project) {
                this.project = project;
        }

        public void initialize() {
                // Na początku przycisk "Generate" jest nieaktywny
                generateButton.setDisable(true);

                // Nasłuchiwanie zmian w projekcie
                project.addObserver((observable, o) -> updateGenerateButtonState());
        }

        @FXML
        public void handleAddAgent(ActionEvent event) {
                Project currentProject = projectManager.getCurrentProject();
                if (currentProject != null) {
                        // Dodaj agenta do aktualnego projektu
                        //currentProject.getListAgent().add(new Agent(/* parametry agenta */));
                        // Aktualizuj tabelę agentów
                        //updateAgentTable();
                }
        }

        public void updateAgentTable() {
                Project currentProject = projectManager.getCurrentProject();
                if (currentProject != null) {
                        // Aktualizuj tabelę agentów na podstawie aktualnego projektu
                        //agentTable.setItems(FXCollections.observableArrayList(currentProject.getListAgent()));
                }
        }

        @FXML
        private void handleGeneratePDF() {
                // Tutaj można zaimplementować generowanie PDFa z raportem
        }

        @FXML
        private void handleNew() {
                // Reset projektu
                project.reset();
                reportTextArea.clear();
        }

        @FXML
        private void handleSaveAs() {
                // Zapisz projekt jako nowy plik
                projectManager.saveProjectAs(project);
        }

        @FXML
        private void handleSave() {
                // Zapisz zmiany w projekcie
                projectManager.saveProject(project);
        }

        @FXML
        private void handleMenu() {
                // Otwórz okno startowe
                projectManager.openStartWindow();
        }

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
}