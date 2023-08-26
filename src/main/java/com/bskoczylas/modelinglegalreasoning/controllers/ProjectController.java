package com.bskoczylas.modelinglegalreasoning.controllers;

import com.bskoczylas.modelinglegalreasoning.App;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.*;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.*;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.dataStructures.AVPair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.court.Report;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.IncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;
import com.bskoczylas.modelinglegalreasoning.domain.models.projectObserver.ProjectObserver;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.dataStructures.AVPPair;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class ProjectController implements Initializable, ProjectObserver, AVObserverController, AVPObserverController, AgContrObserver, PropositionControllerObserver, ValueControllerObserver, IncompControllerObserver, RuleControllerObserver {
        private Project project;

        @FXML
        private SplitPane splitPane;

        private App app;
        private Stage helpStage;
        private Stage ruleStage;

        // for agent
        @FXML
        private TableView<Agent> agentTable;

        @FXML
        private TextField agentNameTextField;

        @FXML
        private TableColumn<Agent, Integer> agentIdColumn;

        @FXML
        private TableColumn<Agent, String> agentCreatedColumn;

        @FXML
        private TableColumn<Agent, String> agentNameColumn;

        private AgentController agentController;

        // for values
        @FXML
        private TableView<Value> valueTable;
        @FXML
        private TextField valueNameTextField;
        @FXML
        private TableColumn<Value, Integer> valueIdColumn;
        @FXML
        private TableColumn<Value, String> valueCreatedColumn;
        @FXML
        private TableColumn<Value, String> valueNameColumn;

        private ValueController valueController;

        // for propositions
        @FXML
        private TableView<Proposition> propositionTable;
        private PropositionController propositionController;
        @FXML
        private TextField propositionNameTextField;
        @FXML
        private TableColumn<Proposition, Integer> propositionIdColumn;
        @FXML
        private TableColumn<Proposition, String> propositionCreatedColumn;
        @FXML
        private TableColumn<Proposition, String> propositionNameColumn;

        // for AV
        private AVController avController;
        private AgentValueToWeight avWeights;
        private ObservableList<AVPair> avPairs;
        @FXML
        private TableView<AVPair> avTable;
        @FXML
        private TableColumn<AVPair, Integer> avIdColumn;
        @FXML
        private TableColumn<AVPair, String> avAgentsColumn;
        @FXML
        private TableColumn<AVPair, String> avValuesColumn;
        @FXML
        private TableColumn<AVPair, String> avWeightsColumn;
        @FXML
        private Slider avMinScale;
        @FXML
        private Slider avMaxScale;
        @FXML
        private ComboBox<Weight> avWeightsComboBox;

        // for AVP
        private AVPController avpController;
        private AgentValuePropWeight avpWeights;
        private ObservableList<AVPPair> avpPairs;
        @FXML
        private TableView<AVPPair> avpTable;
        @FXML
        private TableColumn<AVPPair, Integer> avpIdColumn;
        @FXML
        private TableColumn<AVPPair, String> avpAgentsColumn;
        @FXML
        private TableColumn<AVPPair, String> avpValuesColumn;
        @FXML
        private TableColumn<AVPPair, String> avpPropositionsColumn;
        @FXML
        private TableColumn<AVPPair, String> avpWeightsColumn;
        @FXML
        private Slider avpMinScale;
        @FXML
        private Slider avpMaxScale;
        @FXML
        private ComboBox<Weight> avpWeightsComboBox;

        // for IncompProp
        @FXML
        private TableView<IncompProp> incompPropTable;
        @FXML
        private TableColumn<IncompProp, Integer> incompIdColumn;
        @FXML
        private TableColumn<IncompProp, String> incompCreatedColumn;
        @FXML
        private TableColumn<IncompProp, Boolean> incompIsDecisionColumn;
        @FXML
        private TableColumn<IncompProp, String> incompProp1NameColumn;
        @FXML
        private TableColumn<IncompProp, String> incompProp2NameColumn;

        @FXML
        private ComboBox<Proposition> prop1comboBoxIncompProp = new ComboBox<>();

        @FXML
        private ComboBox<Proposition> prop2comboBoxIncompProp = new ComboBox<>();

        @FXML
        private RadioButton isDecisionRadioButton;

        private IncompPropController incompPropController;

        // for Rules
        private RuleController ruleController;
        @FXML
        private TableView<Rule> rulesTable;
        @FXML
        private TableColumn<Rule, String> rulesIdColumn;
        @FXML
        private TableColumn<Rule, String> rulesCreatedColumn;
        @FXML
        private TableColumn<Rule, String> rulesPremisesColumn;
        @FXML
        private TableColumn<Rule, String> rulesConclusionsColumn;
        private ObservableList<Rule> rulesObservableList = FXCollections.observableArrayList();


        // buttons
        private Report report = new Report();
        @FXML
        private TextArea reportTextArea;

        @FXML
        private Button generateButton;

        @FXML
        private Button generatePDFButton;

        public ProjectController() {
                this.project = new Project();
                this.avPairs = FXCollections.observableArrayList();
                this.avpPairs = FXCollections.observableArrayList();
                this.project.addProjectObserver(this);
        }

        @Override
        public void initialize(URL url, ResourceBundle rb) {
                splitPane.lookupAll(".split-pane-divider").forEach(div ->  div.setMouseTransparent(true));
                // Inicjalizacja kontrolerów
                // Agent section
                this.agentController = new AgentController(this);
                this.agentController.addAgentContrObserver(this);

                // Ustawienie kolumn w tabeli
                agentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                agentCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("formattedCreated"));
                agentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

                agentTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // Value section
                this.valueController = new ValueController(this);
                this.valueController.addValueContrObserver(this);

                valueIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                valueCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("formattedCreated"));
                valueNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));


                valueTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // Proposition section
                this.propositionController = new PropositionController(this);
                this.propositionController.addPropositionContrObserver(this);

                propositionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                propositionCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("formattedCreated"));
                propositionNameColumn.setCellValueFactory(new PropertyValueFactory<>("statement"));

                propositionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // IncompProp section
                this.incompPropController = new IncompPropController(incompPropTable, this, project, prop1comboBoxIncompProp,
                        prop2comboBoxIncompProp, isDecisionRadioButton);
                this.incompPropController.addIncompContrObserver(this);

                // Set up the columns in the IncompProp table
                incompIdColumn.setCellValueFactory(new PropertyValueFactory<IncompProp, Integer>("id"));
                incompCreatedColumn.setCellValueFactory(new PropertyValueFactory<IncompProp, String>("formattedCreated"));
                incompIsDecisionColumn.setCellValueFactory(new PropertyValueFactory<IncompProp, Boolean>("decision"));
                incompProp1NameColumn.setCellValueFactory(new PropertyValueFactory<IncompProp, String>("prop1Name"));
                incompProp2NameColumn.setCellValueFactory(new PropertyValueFactory<IncompProp, String>("prop2Name"));

                incompPropTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // AgentValue Weights section
                this.avWeights = this.project.getAgentValueToWeight();

                this.avController = new AVController(
                        avWeights,
                        this,
                        avTable,
                        avIdColumn,
                        avAgentsColumn,
                        avValuesColumn,
                        avWeightsColumn,
                        avMinScale,
                        avMaxScale,
                        avWeightsComboBox
                );
                this.avController.addAVObserver(this);

                // Bind the table view to the data
                Map.Entry<AgentValue, Weight>[] entries = avWeights.getAgentValueWeights().entrySet().stream()
                        .toArray(Map.Entry[]::new);

                List<AVPair> avPairs = IntStream.range(0, entries.length)
                        .mapToObj(i -> {
                                AVPair pair = new AVPair(entries[i].getKey(), entries[i].getValue());
                                pair.setId(i+1); // Assume that setId exists in AVPair.
                                return pair;
                        })
                        .collect(Collectors.toList());

                // Inicjalizacja cell factory dla ComboBoxa.
                avWeightsComboBox.setCellFactory(new Callback<ListView<Weight>, ListCell<Weight>>() {
                        @Override
                        public ListCell<Weight> call(ListView<Weight> param) {
                                return new ListCell<Weight>() {
                                        @Override
                                        protected void updateItem(Weight item, boolean empty) {
                                                super.updateItem(item, empty);
                                                if (item == null || empty) {
                                                        setText(null);
                                                } else {
                                                        setText(item.toString()); // replace with your own method to convert Weight to a string
                                                }
                                        }
                                };
                        }
                });

                avTable.setItems(FXCollections.observableArrayList(avPairs));

                // Initialize the table columns
                avIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
                avAgentsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAgentValue().getAgent().getName()));
                avValuesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAgentValue().getValue().getName()));
                avWeightsColumn.setCellValueFactory(cellData -> {
                        Weight weight = cellData.getValue().getWeight();
                        if (weight.isIndeterminate()) {
                                return new SimpleStringProperty("?");
                        } else {
                                return new SimpleStringProperty(String.valueOf(weight.getNumberValue()));
                        }
                });

                avTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // Initialize the sliders
                avMinScale.valueProperty().addListener((obs, oldVal, newVal) -> {
                        if (newVal.intValue() > avMaxScale.getValue() - 2) {
                                Platform.runLater(() -> avMinScale.setValue(oldVal.intValue()));
                        }
                });

                avMaxScale.valueProperty().addListener((obs, oldVal, newVal) -> {
                        if (newVal.intValue() < avMinScale.getValue() + 2) {
                                Platform.runLater(() -> avMaxScale.setValue(oldVal.intValue()));
                        }
                });
                avController.updateWeightsComboBox();

                // AgentValueProposition Weights section
                this.avpWeights = this.project.getAgentValuePropWeight();

                this.avpController = new AVPController(
                        avpWeights,
                        this,
                        avpTable,
                        avpIdColumn,
                        avpAgentsColumn,
                        avpValuesColumn,
                        avpPropositionsColumn,
                        avpWeightsColumn,
                        avpMinScale,
                        avpMaxScale,
                        avpWeightsComboBox
                );
                this.avpController.addObserver(this);

                // Bind the table view to the data
                Map.Entry<AgentValueProposition, Weight>[] entriesAVP = avpWeights.getAgentValuePropWeights().entrySet().stream()
                        .toArray(Map.Entry[]::new);

                List<AVPPair> avpPairs = IntStream.range(0, entriesAVP.length)
                        .mapToObj(i -> {
                                AVPPair pair = new AVPPair(entriesAVP[i].getKey(), entriesAVP[i].getValue());
                                pair.setId(i+1); // Assume that setId exists in AVPair.
                                return pair;
                        })
                        .collect(Collectors.toList());


                // Inicjalizacja cell factory dla ComboBoxa.
                avpWeightsComboBox.setCellFactory(new Callback<ListView<Weight>, ListCell<Weight>>() {
                        @Override
                        public ListCell<Weight> call(ListView<Weight> param) {
                                return new ListCell<Weight>() {
                                        @Override
                                        protected void updateItem(Weight item, boolean empty) {
                                                super.updateItem(item, empty);
                                                if (item == null || empty) {
                                                        setText(null);
                                                } else {
                                                        setText(item.toString()); // replace with your own method to convert Weight to a string
                                                }
                                        }
                                };
                        }
                });
                avpTable.setItems(FXCollections.observableArrayList(avpPairs));

                // Initialize the table columns
                avpIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
                avpAgentsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAgentValueProposition().getAgent().getName()));
                avpValuesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAgentValueProposition().getValue().getName()));
                avpPropositionsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAgentValueProposition().getProposition().getStatement()));
                avpWeightsColumn.setCellValueFactory(cellData -> {
                        Weight weight = cellData.getValue().getWeight();
                        if (weight.isIndeterminate()) {
                                return new SimpleStringProperty("?");
                        } else {
                                return new SimpleStringProperty(String.valueOf(weight.getNumberValue()));
                        }
                });

                avpTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // Initialize the sliders
                avpMinScale.valueProperty().addListener((obs, oldVal, newVal) -> {
                        if (newVal.intValue() > avpMaxScale.getValue() - 2) {
                                Platform.runLater(() -> avMinScale.setValue(oldVal.intValue()));
                        }
                });

                avpMaxScale.valueProperty().addListener((obs, oldVal, newVal) -> {
                        if (newVal.intValue() < avpMinScale.getValue() + 2) {
                                Platform.runLater(() -> avMaxScale.setValue(oldVal.intValue()));
                        }
                });
                avpController.updateWeightsComboBox();

                // Ustawienie kolumn w tabeli
                rulesIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                rulesCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("created"));
                rulesPremisesColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Rule, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<Rule, String> param) {
                                String premisesString = param.getValue().getPremises().stream()
                                        .map(Proposition::getStatement)
                                        .collect(Collectors.joining(", "));
                                return new SimpleStringProperty(premisesString);
                        }
                });
                rulesConclusionsColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Rule, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<Rule, String> param) {
                                return new SimpleStringProperty(param.getValue().getConclusion().getStatement());
                        }
                });


                rulesTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                generateButton.setDisable(true);
                generatePDFButton.setDisable(true);

                project.addProjectObserver(new ProjectObserver() {
                        @Override
                        public void updateProject(Project project) {
                                updateGenerateButtonState();
                                avController.updateAVTable();
                                avpController.updateAVPTable();
                        }
                });
        }

        @FXML
        private void handleGenerate() {
                List<Report.ReportSection> reportSections = project.getReport().generateReport();
                String formattedReport = report.formatForJavaFX(reportSections);

                reportTextArea.setText(formattedReport);
        }

        private void updateGenerateButtonState() {
                boolean canGenerate = project.hasEnoughData();
                generateButton.setDisable(!canGenerate);
                generatePDFButton.setDisable(!canGenerate);
        }


        @FXML
        public void handleAddAgent(ActionEvent event) {
                this.agentController.handleAddAgent();
                agentController.updateAgentTable();
                avController.updateAVTable();
                avpController.updateAVPTable();
        }

        @FXML
        public void handleRemoveAgent(ActionEvent event) {
                Agent selectedAgent = this.agentTable.getSelectionModel().getSelectedItem();
                if (selectedAgent != null) {
                        this.agentController.handleRemoveAgent();
                        agentController.updateAgentTable();
                        List<Agent> removedAgents = new ArrayList<>();
                        removedAgents.add(selectedAgent);
                        avController.removeDeletedAgentEntriesFromTable(removedAgents);
                        avpController.removeDeletedAgentEntriesFromTable(removedAgents);
                }
        }

        @FXML
        public void handleAddProposition(ActionEvent event) {
                this.propositionController.handleAddProposition();
                propositionController.updatePropositionTable();
                avpController.updateAVPTable();
        }

        @FXML
        public void handleRemoveProposition() {
                Proposition selectedProposition = propositionTable.getSelectionModel().getSelectedItem();
                if (selectedProposition != null) {
                        propositionController.removeProposition(selectedProposition);
                        propositionController.updatePropositionTable();
                        avpController.removeDeletedPropositionEntryFromTable(selectedProposition);
                        incompPropController.removeIncompPropsIncludingProposition(propositionController.getRemovedProposition());
                } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("No Proposition selected!");

                        alert.showAndWait();
                }
        }

        @FXML
        public void handleAddValue(ActionEvent event) {
                this.valueController.handleAddValue();
                valueController.updateValueTable();
                avController.updateAVTable();
                avpController.updateAVPTable();
                avTable.refresh();
        }

        @FXML
        public void handleRemoveValue(ActionEvent event) {
                Value selectedValue = this.valueTable.getSelectionModel().getSelectedItem();
                if (selectedValue != null) {
                        this.valueController.handleRemoveValue();
                        valueController.updateValueTable();
                        List<Value> removedValues = new ArrayList<>();
                        removedValues.add(selectedValue);
                        avController.removeDeletedValueEntriesFromTable(removedValues);
                        avpController.removeDeletedValueEntriesFromTable(removedValues);
                }
        }

        @FXML
        public void handleAVAddScaleButton() {
                avController.changeScale();
        }

        @FXML
        public void handleAVRandomButton() {
                avController.randomizeWeights();
                avController.updateAVTable();
        }

        @FXML
        public void handleAVAddButton() {
                avController.addWeight();
                avController.updateAVTable();
        }

        @Override
        public void updateAV(AVController avController) {
                this.avWeights = avController.getAvWeights();
                this.project.getAgentValueToWeight().setAgentValueWeights(this.avWeights.getAgentValueWeights());
                Scale newScale = avController.getAvWeights().getScale();
                if (!this.avWeights.getScale().equals(newScale)) {
                        this.project.getAgentValuePropWeight().setScale(this.avWeights.getScale());
                }
                avPairs.clear();
                avPairs.addAll(avWeights.getAgentValueWeights().entrySet().stream()
                        .map(entry -> new AVPair(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()));
                this.avController.setAvWeights(this.avWeights);
                avTable.setItems(avPairs);
                this.avController.updateAVTable();
        }

        @FXML
        public void handleAVPScaleButton() {
                avpController.changeScale();
                avpController.updateAVPTable();
        }

        @FXML
        public void handleAVPRandomButton() {
                avpController.randomizeWeights();
                avpController.updateAVPTable();
        }

        @FXML
        public void handleAVPAddButton() {
                avpController.addWeight();
                avpController.updateAVPTable();
        }

        @Override
        public void updateAVP(AVPController avpController) {
                this.avpWeights = avpController.getAvpWeights();
                this.project.getAgentValuePropWeight().setAgentValuePropWeights(this.avpWeights.getAgentValuePropWeights());

                Scale newScale = avpController.getAvpWeights().getScale();
                if (!this.avpWeights.getScale().equals(newScale)) {
                        this.project.getAgentValueToWeight().setScale(this.avpWeights.getScale());
                }
                avpPairs.clear();
                avpPairs.addAll(avpWeights.getAgentValuePropWeights().entrySet().stream()
                        .map(entry -> new AVPPair(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()));
                this.avpController.setAvpWeights(this.avpWeights);
                avpTable.setItems(avpPairs);
                this.avpController.updateAVPTable();
        }

        @FXML
        public void handleAddIncompButtonAction(ActionEvent actionEvent) {
                incompPropController.handleAddButtonAction(actionEvent);
        }

        @FXML
        public void handleRemoveIncompButtonAction(ActionEvent actionEvent) {
                incompPropController.handleRemoveButtonAction(actionEvent);
        }

        @FXML
        public void handleAddRuleButton(ActionEvent actionEvent) {
                if(!(project.getListIncompProp().getDecisions() == null)) {
                        Proposition decision1 = project.getListIncompProp().getDecisions().getFirst();
                        Proposition decision2 = project.getListIncompProp().getDecisions().getSecond();

                        try {
                                if (ruleStage != null && ruleStage.isShowing()) {
                                        return;
                                }

                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createRule.fxml"));

                                ruleController = new RuleController(this);
                                ruleController.addRuleContrObserver(this);

                                loader.setController(ruleController);

                                Parent root = loader.load();

                                ruleController.setDecisions(decision1, decision2);

                                ruleStage = new Stage();
                                ruleStage.setScene(new Scene(root));
                                ruleStage.show();

                        } catch (IOException e) {
                                e.printStackTrace();
                        }

                        updateRulesTable();
                } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setHeaderText(null);
                        alert.setContentText("There is no decisions in project! You have to define them in IncompProp section.");

                        alert.showAndWait();
                }
        }

        @FXML
        public void handleRemoveRuleButton(ActionEvent actionEvent) {
                Rule selectedRule = rulesTable.getSelectionModel().getSelectedItem();

                if (selectedRule != null) {
                        project.getListRules().removeRule(selectedRule);

                        updateRulesTable();
                }
        }

        public void updateRulesTable() {
                ObservableList<Rule> rulesObservableList = FXCollections.observableArrayList(project.getListRules().getListRules());
                rulesTable.setItems(rulesObservableList);
        }

        @Override
        public void updateRuleController(RuleController ruleController) {
                project.getListRules().setListRules(ruleController.getListRules().getListRules());

                updateRulesTable();
                updateGenerateButtonState();
        }

        public ListRules getListRules() {
                return project.getListRules();
        }

        @Override
        public void updateProject(Project project) {
                this.project = project;
        }

        public Project getProject() {
                return this.project;
        }

        @FXML
        public void handleGeneratePDFButton(ActionEvent actionEvent) {
                List<Report.ReportSection> reportSections = project.getReport().generateReport();

                String destinationPath = "src/main/resources/pdf/project.pdf";

                report.generateReportPDF(reportSections, destinationPath);

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
        public void handleHelpButton(ActionEvent actionEvent) {
                try {
                        if (helpStage != null && helpStage.isShowing()) {
                                return;
                        }

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/help.fxml"));
                        Parent root = loader.load();

                        Scene scene = new Scene(root);
                        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

                        helpStage = new Stage();
                        helpStage.setScene(scene);
                        helpStage.setTitle("Help");
                        helpStage.setResizable(false);
                        helpStage.show();
                } catch (IOException ex) {
                        ex.printStackTrace();
                }
        }

        @FXML
        public void handleNewButton(ActionEvent actionEvent) {
                app.showProjectWindow();
        }

        @FXML
        public void handleExitButton(ActionEvent actionEvent) {
        }

        @Override
        public void updateValueController(ValueController valueController) {
                List<Value> oldList = project.getListValue().getValues();
                List<Value> newList = valueController.getProject().getListValue().getValues();

                if (newList.size() > oldList.size()) {
                        for (Value newValue : newList) {
                                if (!oldList.contains(newValue)) {
                                        project.getListValue().addValue(newValue);
                                }
                        }
                } else if (newList.size() < oldList.size()) {
                        List<Value> toRemove = new ArrayList<>();

                        for (Value oldValue : oldList) {
                                if (!newList.contains(oldValue)) {
                                        toRemove.add(oldValue);
                                }
                        }
                        avController.removeDeletedValueEntriesFromTable(toRemove);
                        avpController.removeDeletedValueEntriesFromTable(toRemove);
                        for (Value value : toRemove) {
                                project.getListValue().removeValue(value);
                        }
                }
                updateGenerateButtonState();
        }

        @Override
        public void updateAgentController(AgentController agentController) {
                List<Agent> oldList = project.getListAgent().getAgents();
                List<Agent> newList = agentController.getProject().getListAgent().getAgents();

                if (newList.size() > oldList.size()) {
                        for (Agent newAgent : newList) {
                                if (!oldList.contains(newAgent)) {
                                        project.getListAgent().addAgent(newAgent);
                                }
                        }
                } else if (newList.size() < oldList.size()) {
                        List<Agent> toRemove = new ArrayList<>();

                        for (Agent oldAgent : oldList) {
                                if (!newList.contains(oldAgent)) {
                                        toRemove.add(oldAgent);
                                }
                        }
                        avController.removeDeletedAgentEntriesFromTable(toRemove);
                        avpController.removeDeletedAgentEntriesFromTable(toRemove);
                        for (Agent agent : toRemove) {
                                project.getListAgent().removeAgent(agent);
                        }
                }
                updateGenerateButtonState();
        }

        @Override
        public void updatePropositionController(PropositionController propositionController) {
                List<Proposition> oldList = new ArrayList<>(project.getListProposition().getListProposition());
                List<Proposition> newList = propositionController.getProject().getListProposition().getListProposition();

                if (newList.size() > oldList.size()) {
                        for (Proposition newProposition : newList) {
                                if (!oldList.contains(newProposition)) {
                                        project.getListProposition().addProposition(newProposition);
                                }
                        }
                } else if (newList.size() < oldList.size()) {
                        Iterator<Proposition> iterator = oldList.iterator();
                        while (iterator.hasNext()) {
                                Proposition oldProposition = iterator.next();
                                if (!newList.contains(oldProposition)) {
                                        project.getListProposition().removeProposition(oldProposition);
                                        incompPropController.removeIncompPropsIncludingProposition(oldProposition);
                                        avpController.removeDeletedPropositionEntryFromTable(oldProposition);
                                        iterator.remove();
                                }
                        }
                }
                updateGenerateButtonState();
        }

        @Override
        public void updateIncompController(IncompPropController incompPropController) {
                project.getListIncompProp().setIncompPropList(incompPropController.getProject()
                        .getListIncompProp().getIncompatiblePropositions());
                updateGenerateButtonState();
        }

        public TableView<Agent> getAgentTable() {
                return agentTable;
        }

        public TextField getAgentNameTextField() {
                return agentNameTextField;
        }

        public TableColumn<Agent, Integer> getAgentIdColumn() {
                return agentIdColumn;
        }

        public TableColumn<Agent, String> getAgentCreatedColumn() {
                return agentCreatedColumn;
        }

        public TableColumn<Agent, String> getAgentNameColumn() {
                return agentNameColumn;
        }

        public TableView<Value> getValueTable() {
                return valueTable;
        }

        public TextField getValueNameTextField() {
                return valueNameTextField;
        }

        public TableColumn<Value, Integer> getValueIdColumn() {
                return valueIdColumn;
        }

        public TableColumn<Value, String> getValueCreatedColumn() {
                return valueCreatedColumn;
        }

        public TableColumn<Value, String> getValueNameColumn() {
                return valueNameColumn;
        }

        public TableView<Proposition> getPropositionTable() {
                return propositionTable;
        }

        public TextField getPropositionNameTextField() {
                return propositionNameTextField;
        }

        public TableColumn<Proposition, Integer> getPropositionIdColumn() {
                return propositionIdColumn;
        }

        public TableColumn<Proposition, String> getPropositionCreatedColumn() {
                return propositionCreatedColumn;
        }

        public TableColumn<Proposition, String> getPropositionNameColumn() {
                return propositionNameColumn;
        }

        public AgentValueToWeight getAvWeights() {
                return avWeights;
        }

        public ObservableList<AVPair> getAvPairs() {
                return avPairs;
        }

        public TableView<AVPair> getAvTable() {
                return avTable;
        }

        public TableColumn<AVPair, Integer> getAvIdColumn() {
                return avIdColumn;
        }

        public TableColumn<AVPair, String> getAvAgentsColumn() {
                return avAgentsColumn;
        }

        public TableColumn<AVPair, String> getAvValuesColumn() {
                return avValuesColumn;
        }

        public TableColumn<AVPair, String> getAvWeightsColumn() {
                return avWeightsColumn;
        }

        public Slider getAvMinScale() {
                return avMinScale;
        }

        public Slider getAvMaxScale() {
                return avMaxScale;
        }

        public ComboBox<Weight> getAvWeightsComboBox() {
                return avWeightsComboBox;
        }

        public AgentValuePropWeight getAvpWeights() {
                return avpWeights;
        }

        public ObservableList<AVPPair> getAvpPairs() {
                return avpPairs;
        }

        public TableView<AVPPair> getAvpTable() {
                return avpTable;
        }

        public TableColumn<AVPPair, Integer> getAvpIdColumn() {
                return avpIdColumn;
        }

        public TableColumn<AVPPair, String> getAvpAgentsColumn() {
                return avpAgentsColumn;
        }

        public TableColumn<AVPPair, String> getAvpValuesColumn() {
                return avpValuesColumn;
        }

        public TableColumn<AVPPair, String> getAvpPropositionsColumn() {
                return avpPropositionsColumn;
        }

        public TableColumn<AVPPair, String> getAvpWeightsColumn() {
                return avpWeightsColumn;
        }

        public Slider getAvpMinScale() {
                return avpMinScale;
        }

        public Slider getAvpMaxScale() {
                return avpMaxScale;
        }

        public ComboBox<Weight> getAvpWeightsComboBox() {
                return avpWeightsComboBox;
        }

        public TableView<IncompProp> getIncompPropTable() {
                return incompPropTable;
        }

        public TableColumn<IncompProp, Integer> getIncompIdColumn() {
                return incompIdColumn;
        }

        public TableColumn<IncompProp, String> getIncompCreatedColumn() {
                return incompCreatedColumn;
        }

        public TableColumn<IncompProp, Boolean> getIncompIsDecisionColumn() {
                return incompIsDecisionColumn;
        }

        public TableColumn<IncompProp, String> getIncompProp1NameColumn() {
                return incompProp1NameColumn;
        }

        public TableColumn<IncompProp, String> getIncompProp2NameColumn() {
                return incompProp2NameColumn;
        }

        public ComboBox<Proposition> getProp1comboBoxIncompProp() {
                return prop1comboBoxIncompProp;
        }

        public ComboBox<Proposition> getProp2comboBoxIncompProp() {
                return prop2comboBoxIncompProp;
        }

        public RadioButton getIsDecisionRadioButton() {
                return isDecisionRadioButton;
        }

        public TableView<Rule> getRulesTable() {
                return rulesTable;
        }

        public TableColumn<Rule, String> getRulesIdColumn() {
                return rulesIdColumn;
        }

        public TableColumn<Rule, String> getRulesCreatedColumn() {
                return rulesCreatedColumn;
        }

        public TableColumn<Rule, String> getRulesPremisesColumn() {
                return rulesPremisesColumn;
        }

        public TableColumn<Rule, String> getRulesConclusionsColumn() {
                return rulesConclusionsColumn;
        }

        public void setApp(App app) {
                this.app = app;
        }
}