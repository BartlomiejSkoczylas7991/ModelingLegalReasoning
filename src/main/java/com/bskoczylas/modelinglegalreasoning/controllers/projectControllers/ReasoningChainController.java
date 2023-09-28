package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.App;
import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.court.Report;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ReasoningChainController {
    private Stage primaryStage;
    private App app;
    @FXML
    private TabPane mainTabPane;
    @FXML
    private Button backButton;
    @FXML
    private TableView<Proposition> propositionsTableView;
    @FXML
    private TableColumn<Proposition, String> propositionNameColumn;
    @FXML
    private TableColumn<Proposition, Boolean> isTrueColumn;
    private Report report = new Report();
    @FXML
    private Button switchButton;
    @FXML
    private TextArea reportTextArea;

    private ObservableMap<Agent, Set<Rule>> observableAllowedRules = FXCollections.observableHashMap();
    private ObservableList<Proposition> observablePropositions = FXCollections.observableArrayList();
    private Map<Agent, TableView<Rule>> allowedRulesTableViews = new HashMap<>();
    private ObservableMap<Agent, List<Rule>> observableReasoningChains = FXCollections.observableHashMap();
    private Map<Agent, TableView<Rule>> reasoningChainTableViews = new HashMap<>();


    private Project project;

    private ProjectController parentController;

    public ReasoningChainController() {
        project = new Project();
    }

    public ReasoningChainController(Project project) {
        this.project = project;
    }

    public void setProject(Project project) {
        this.project = project;
        // Możesz również zaktualizować widok w zależności od przekazanego projektu, np. wypełnić tabelę
        updateView();
    }

    private void updateView() {
        // Aktualizuj widok w oparciu o dane w projekcie
    }

    @FXML
    public void initialize() {
        // Uzupełniamy observableAllowedRules na podstawie danych z projektu
        observableAllowedRules.putAll(project.getListReasoningChain().getAllowedRules());

        // Inicjalizacja observableReasoningChains na podstawie danych z projektu
        for (Map.Entry<Agent, ReasoningChain> entry : project.getListReasoningChain().getListReasoningChain().entrySet()) {
            Agent agent = entry.getKey();
            ReasoningChain reasoningChain = entry.getValue();
            List<Rule> rj = reasoningChain.getKnowledgeBase().getRj();
            observableReasoningChains.put(agent, rj);
        }

        // Ustawienie początkowego stanu dla komponentów UI (jeśli jest potrzebne)
        // Na przykład, możemy chcieć zdezaktywować pewne przyciski, dopóki pewne warunki nie zostaną spełnione
        for (Agent agent : this.project.getListReasoningChain().getListReasoningChain().keySet()) {
            Tab agentTab = new Tab(agent.getName() + " ReasoningChain");

            // Create tables and buttons for ReasoningChain and AllowedRules
            TableView<Rule> reasoningChainTableView = createReasoningChainTableView(agent);
            reasoningChainTableViews.put(agent, reasoningChainTableView);
            TableView<Rule> allowedRulesTableView = createAllowedRulesTableView(agent);

            Button removeButton = new Button("Remove");
            removeButton.setOnAction(e -> {
                Rule selectedRule = reasoningChainTableView.getSelectionModel().getSelectedItem();
                if (selectedRule != null) {
                    project.getListReasoningChain().removeRuleFromReasoningChain(agent, selectedRule);
                    reasoningChainTableView.getItems().remove(selectedRule);

                    // Aktualizacja widoku w oparciu o model
                    allowedRulesTableView.getItems().setAll(project.getListReasoningChain().getAllowedRules().get(agent));
                    refreshReasoningChainView(agent);
                    refreshAllowedRulesView(agent);
                }
            });

            Button addButton = new Button("Add");
            addButton.setOnAction(e -> {
                Rule selectedRule = allowedRulesTableView.getSelectionModel().getSelectedItem();
                if (selectedRule != null) {  // Aby uniknąć null pointer exception
                    project.getListReasoningChain().addRuleToReasoningChain(agent, selectedRule);
                    refreshAllowedRulesView(agent);
                    refreshReasoningChainView(agent);
                }
            });

            // Przywrócenie definicji dla reasoningChainBox i allowedRulesBox:
            VBox reasoningChainBox = new VBox(10, reasoningChainTableView, removeButton);
            VBox allowedRulesBox = new VBox(10, allowedRulesTableView, addButton);

            HBox combinedLayout = new HBox(10);  // dodajemy odstępy między elementami w HBox
            combinedLayout.setAlignment(Pos.CENTER);
            combinedLayout.getChildren().addAll(reasoningChainBox, allowedRulesBox);

            GridPane gridPane = new GridPane();
            gridPane.add(combinedLayout, 0, 0);  // Dodajemy HBox do środka GridPane
            gridPane.setAlignment(Pos.CENTER);

            // Ustawienie Hgrow i Vgrow dla komponentów wewnątrz GridPane
            GridPane.setHgrow(reasoningChainBox, Priority.ALWAYS);
            GridPane.setVgrow(reasoningChainBox, Priority.ALWAYS);
            GridPane.setHgrow(allowedRulesBox, Priority.ALWAYS);
            GridPane.setVgrow(allowedRulesBox, Priority.ALWAYS);

            agentTab.setContent(gridPane);

            mainTabPane.getTabs().add(agentTab);
        }
        propositionsTableView.setPlaceholder(new Label("No propositions available"));  // Wprowadź odpowiednią wiadomość
        loadTableData();
        propositionNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatement()));
        isTrueColumn.setCellValueFactory(cellData -> new SimpleBooleanProperty(cellData.getValue().isTrue()));

        observablePropositions.setAll(project.getListProposition().getListProposition());
        propositionsTableView.setItems(observablePropositions);

        observableAllowedRules.addListener((MapChangeListener<Agent, Set<Rule>>) change -> {
            if (change.wasAdded()) {
                // Tu możemy wprowadzić dodatkowe operacje jeśli są potrzebne.
            }
        });

        observableReasoningChains.addListener((MapChangeListener<Agent, List<Rule>>) change -> {
            if (change.wasAdded()) {
            }
        });
        setupListeners();
    }

    private void refreshAllowedRulesView(Agent agent) {
        Set<Rule> updatedAllowedRules = project.getListReasoningChain().getAllowedRules().get(agent);
        observableAllowedRules.put(agent, FXCollections.observableSet(updatedAllowedRules));

        TableView<Rule> tableView = allowedRulesTableViews.get(agent);
        if (tableView != null) {
            tableView.setItems(FXCollections.observableArrayList(updatedAllowedRules));
        }
    }

    private void refreshReasoningChainView(Agent agent) {
        List<Rule> updatedReasoningChain = project.getListReasoningChain().getListReasoningChain().get(agent).getKnowledgeBase().getRj();
        observableReasoningChains.put(agent, updatedReasoningChain);

        TableView<Rule> tableView = reasoningChainTableViews.get(agent);
        if (tableView != null) {
            tableView.setItems(FXCollections.observableArrayList(updatedReasoningChain));
        }
    }

    private void setupListeners() {
        backButton.setOnAction(event -> handleBackButton());
    }

    private void loadTableData() {
        ObservableList<AgentTableEntry> data = FXCollections.observableArrayList();

        HashMap<Agent, ReasoningChain> map = project.getListReasoningChain().getListReasoningChain();
        for (Map.Entry<Agent, ReasoningChain> entry : map.entrySet()) {
            String agentName = entry.getKey().getName();
            String reasoningChain = entry.getValue().toString(); // Zakładając, że masz metodę toString() w ReasoningChain
            data.add(new AgentTableEntry(agentName, reasoningChain));
        }
    }

    @FXML
    public void handleBackButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/project.fxml"));
            ProjectController projectController = new ProjectController(this.project);

            loader.setController(projectController);
            Parent root = loader.load();

            projectController.setPrimaryStage(primaryStage); // Ustawiamy primaryStage po wczytaniu FXML

            backButton.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    private void handleGenerate() {
        List<Report.ReportSection> reportSections = project.getReport().generateReport();

        String formattedReport = report.formatForJavaFX(reportSections);

        reportTextArea.setText(formattedReport);
    }


    @FXML
    private void handleSwitchButton() {
        Proposition selectedProposition = propositionsTableView.getSelectionModel().getSelectedItem();
        if (selectedProposition != null) {
            project.getListProposition().switchPropositionTrue(selectedProposition);
            observablePropositions.setAll(project.getListProposition().getListProposition()); // Aktualizacja listy

            for (Agent agent : this.project.getListReasoningChain().getListReasoningChain().keySet()) {
                refreshAllowedRulesView(agent);
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No choice");
            alert.setHeaderText(null);
            alert.setContentText("Please choose a proposal from the table!");
            alert.showAndWait();
        }

    }

    public Project getProject() {
        return project;
    }

    public void setParentController(ProjectController parentController) {
        this.parentController = parentController;
    }

    private TableView<Rule> createReasoningChainTableView(Agent agent) {
        TableView<Rule> tableViewRC = new TableView<>();
        ObservableList<Rule> data = FXCollections.observableArrayList(project.getListReasoningChain().getListReasoningChain().get(agent).getKnowledgeBase().getRj());
        tableViewRC.setItems(data);

        // Set up columns
        TableColumn<Rule, String> premisesColumn = new TableColumn<>("Premises");
        premisesColumn.setCellValueFactory(new PropertyValueFactory<>("premisesAsString"));

        TableColumn<Rule, String> conclusionColumn = new TableColumn<>("Conclusion");
        conclusionColumn.setCellValueFactory(new PropertyValueFactory<>("conclusionAsString"));

        tableViewRC.getColumns().addAll(premisesColumn, conclusionColumn);

        // Responsiveness settings
        tableViewRC.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        premisesColumn.setMaxWidth(1f * Integer.MAX_VALUE * 50); // 50% width
        conclusionColumn.setMaxWidth(1f * Integer.MAX_VALUE * 50); // 50% width

        return tableViewRC;
    }


    private TableView<Rule> createAllowedRulesTableView(Agent agent) {
        TableView<Rule> tableViewAl = new TableView<>();
        ObservableList<Rule> data = FXCollections.observableArrayList(observableAllowedRules.get(agent));
        tableViewAl.setItems(data);

        // Set up columns
        TableColumn<Rule, String> premisesColumn = new TableColumn<>("Premises");
        premisesColumn.setCellValueFactory(new PropertyValueFactory<>("premisesAsString"));

        TableColumn<Rule, String> conclusionColumn = new TableColumn<>("Conclusion");
        conclusionColumn.setCellValueFactory(new PropertyValueFactory<>("conclusionAsString"));

        tableViewAl.getColumns().addAll(premisesColumn, conclusionColumn);

        // Responsiveness settings
        tableViewAl.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        premisesColumn.setMaxWidth(1f * Integer.MAX_VALUE * 50); // 50% width
        conclusionColumn.setMaxWidth(1f * Integer.MAX_VALUE * 50); // 50% width

        allowedRulesTableViews.put(agent, tableViewAl);
        return tableViewAl;
    }

    @FXML
    public void handleGeneratePDFButton(ActionEvent actionEvent) {
        List<Report.ReportSection> reportSections = project.getReport().generateReport();

        String destinationPath = "src/main/resources/pdf/project.pdf";

        getProject().getReport().generateReportPDF(reportSections, destinationPath);

        try {
            File pdfFile = new File(destinationPath);
            if (pdfFile.exists()) {
                if (Desktop.isDesktopSupported()) {
                    new Thread(() -> {
                        try {
                            Desktop.getDesktop().open(pdfFile);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }).start();
                } else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Awt Desktop is not supported!");

                    alert.showAndWait();
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("File does not exist!");

                alert.showAndWait();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    public void handleNewButton(ActionEvent actionEvent) {
        app.showProjectWindow();
    }

    @FXML
    public void handleSaveButton(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save project data");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("ProjectFile", "*.mlrp")
        );
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            this.project.save(file.getAbsolutePath());
        }
    }

    @FXML
    public void handleExitButton(ActionEvent actionEvent) {
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.close();
        Platform.exit();
    }

}
