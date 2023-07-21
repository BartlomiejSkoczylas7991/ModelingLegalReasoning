package com.bskoczylas.modelinglegalreasoning.controllers;

import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.*;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.*;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.dataStructures.AVPair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.court.Report;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.IncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AgentObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;
import com.bskoczylas.modelinglegalreasoning.domain.models.projectObserver.ProjectObserver;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.dataStructures.AVPPair;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

// celem klasy jest edytowanie currentProject, zapisywanie currentProject do bazy
// (nadpisywania już istniejącego). Każda klasa pełni wzorzec - implementujemy metody modyfikujące
// dane pole, po czym utwardzamy tą zmianę ze wszystkimi tego konsekwencjami.
//
// 1 defitions
// 2 Constructor
// 3 getters and setters
// 4 initializer
// 5 handlers
// 6 observers
public class ProjectController implements ProjectObserver, AVObserverController, AVPObserverController, AgContrObserver, PropositionControllerObserver, ValueControllerObserver, IncompControllerObserver, RuleControllerObserver {
        private Project project;

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
        private TableColumn<AVPair, Integer> avWeightsColumn;
        @FXML
        private Slider avMinScale;
        @FXML
        private Slider avMaxScale;
        @FXML
        private ComboBox<Weight> avWeightsComboBox;

        // for AVP
        private AVPController avpController;
        private AgentValuePropWeight avpWeights;
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
        private TableColumn<AVPPair, Integer> avpWeightsColumn;
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
        private TableColumn<IncompProp, LocalDateTime> incompCreatedColumn;
        @FXML
        private TableColumn<IncompProp, Boolean> incompIsDecisionColumn;
        @FXML
        private TableColumn<IncompProp, String> incompNameColumn;
        @FXML
        private TableColumn<IncompProp, String> incompPropNameColumn;

        @FXML
        private ComboBox<Proposition> prop1comboBoxIncompProp = new ComboBox<>();

        @FXML
        private ComboBox<Proposition> prop2comboBoxIncompProp = new ComboBox<>();

        @FXML
        private RadioButton isDecisionRadioButton;

        private IncompPropController incompPropController;

        // for Rules
        @FXML
        private TableView<Rule> rulesTable;
        private RuleController ruleController;
        @FXML
        private TableColumn<Rule, String> rulesIdColumn;
        @FXML
        private TableColumn<Rule, String> rulesCreatedColumn;
        @FXML
        private TableColumn<Rule, String> rulesPremisesColumn;
        @FXML
        private TableColumn<Rule, String> rulesConclusionsColumn;
        @FXML
        private TableColumn<Rule, String> rulesNameColumn;

        // buttons
        private Report report = new Report();

        @FXML
        private Button generateButton;

        @FXML
        private Button generatePDFButton;

        public ProjectController() {
                this.project = new Project();
                this.avPairs = FXCollections.observableArrayList();
        }

        public void setProject(Project project) {
                this.project = project;
                this.project.addProjectObserver(this);

                incompPropController = new IncompPropController(incompPropTable, this, this.project, isDecisionRadioButton);
        }

        public void initialize() {
                // Inicjalizacja kontrolerów
                // Agent section
                this.agentController = new AgentController(this.project, agentTable,
                        agentNameTextField, this);
                this.agentController.addAgentContrObserver(this);

                // Ustawienie kolumn w tabeli
                agentIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                agentCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("created"));
                agentNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

                agentTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // Value section
                this.valueController = new ValueController(valueTable,
                        this.valueNameTextField, this);
                this.valueController.addValueContrObserver(this);

                valueIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                valueCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("created"));
                valueNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));


                valueTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // Proposition section
                this.propositionController = new PropositionController(propositionTable,
                        prop1comboBoxIncompProp, prop2comboBoxIncompProp,
                        this.propositionNameTextField, this);
                this.propositionController.addPropositionContrObserver(this);

                propositionIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                propositionCreatedColumn.setCellValueFactory(new PropertyValueFactory<>("created"));
                propositionNameColumn.setCellValueFactory(new PropertyValueFactory<>("statement"));

                propositionTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

                // IncompProp section
                this.incompPropController = new IncompPropController(incompPropTable, this, project, isDecisionRadioButton);
                this.incompPropController.addIncompContrObserver(this);

                // Set up the columns in the IncompProp table
                incompIdColumn.setCellValueFactory(new PropertyValueFactory<IncompProp, Integer>("id"));
                incompCreatedColumn.setCellValueFactory(new PropertyValueFactory<IncompProp, LocalDateTime>("created"));
                incompIsDecisionColumn.setCellValueFactory(new PropertyValueFactory<IncompProp, Boolean>("isDecision"));
                incompNameColumn.setCellValueFactory(new PropertyValueFactory<IncompProp, String>("name"));
                incompPropNameColumn.setCellValueFactory(new PropertyValueFactory<IncompProp, String>("propName"));

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

                // Bind the table view to the data
                Map.Entry<AgentValue, Weight>[] entries = avWeights.entrySet().stream()
                        .toArray(Map.Entry[]::new);

                List<AVPair> avPairs = IntStream.range(0, entries.length)
                        .mapToObj(i -> {
                                AVPair pair = new AVPair(entries[i].getKey(), entries[i].getValue());
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

                avTable.setItems(FXCollections.observableArrayList(avPairs));

                // Initialize the table columns
                avIdColumn.setCellValueFactory(data -> new SimpleIntegerProperty(data.getValue().getId()).asObject());
                avAgentsColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAgentValue().getAgent().getName()));
                avValuesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getAgentValue().getValue().getName()));
                avWeightsColumn.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getWeight()));

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

                // Bind the table view to the data
                Map.Entry<AgentValueProposition, Weight>[] entriesAVP = avpWeights.entrySet().stream()
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
                avpWeightsColumn.setCellValueFactory(data -> new SimpleObjectProperty(data.getValue().getWeight()));

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
                // rules


                // Na początku przycisk "Generate" jest nieaktywny
                generateButton.setDisable(true);
                generatePDFButton.setDisable(true);

                // Nasłuchiwanie zmian w projekcie
                project.addProjectObserver(new ProjectObserver() {
                        @Override
                        public void updateProject(Project project) {
                                updateGenerateButtonState();
                                avController.updateAVTable();
                                avpController.updateAVPTable();
                        }
                });
        }

        //DODAWANIE PRZYCISKÓW GÓRNYCH ("GENERATE PDF", "NEW", "HELP", "EXIT")
        // GENEROWANIE PDF i "GENERATE" (W Javafx) dopier po spełnieniu warunków:
        // minimum 2 agentów, 2 wartości, 4 propozycje, 1 para decyzji (zwykłe incompProp niekonieczne), 2 zasady
        //@FXML
        //private void handleGenerate() {
        //        if (project.hasEnoughData()) {
        //                // Generuj raport
        //                List<Report.ReportSection> reportSections = report.generateReport();
        //                String formattedReport = report.formatForJavaFX(reportSections);
        //                reportTextArea.setText(formattedReport);
        //

        //WYJŚCIE Z PROGRAMU

        @FXML
        public void handleGenerate() {
                // Generuj raport
                //List<Report.ReportSection> reportSections = report.generateReport();
                //String formattedReport = report.formatForJavaFX(reportSections);
                //reportTextArea.setText(formattedReport);
        }

        private void updateGenerateButtonState() {
                // Sprawdź, czy wprowadzono wystarczająco dużo danych, aby można było wygenerować raport
                boolean canGenerate = project.hasEnoughData();
                generateButton.setDisable(!canGenerate);
                generatePDFButton.setDisable(!canGenerate);
        }


        //DODAWANIE AGENTÓW POPRZEZ AgentController
        @FXML
        public void handleAddAgent(ActionEvent event) {
                this.agentController.handleAddAgent();
                agentController.updateAgentTable();
                System.out.println("To jest lista agentów: " + this.project.getListAgent().getAgents()); // przy dodawaniu agentów lista jest pusta
        }

        @FXML
        public void handleRemoveAgent(ActionEvent event) {
                // Pobieramy zaznaczony agent z tabeli
                Agent selectedAgent = this.agentTable.getSelectionModel().getSelectedItem();
                if (selectedAgent != null) {
                        // Wywołujemy metodę usuwania agenta z AgentController
                        this.agentController.handleRemoveAgent();
                        // Aktualizujemy tabelę agentów
                        agentController.updateAgentTable();
                        // Teraz musimy zaktualizować AVController o usunięciu agenta
                        // Przyjmujemy, że mamy listę usuniętych agentów (w tym przypadku tylko jeden)
                        List<Agent> removedAgents = new ArrayList<>();
                        removedAgents.add(selectedAgent);
                        avController.removeDeletedAgentEntriesFromTable(removedAgents);
                }
        }

        //DODAWANIE PROPOZYCJI POPRZEZ PropositionController
        @FXML
        public void handleAddProposition(ActionEvent event) {
                this.propositionController.handleAddProposition();
                propositionController.updatePropositionTable();
        }

        @FXML
        public void handleRemoveProposition(ActionEvent event) {
                this.propositionController.handleRemoveProposition();
                propositionController.updatePropositionTable();
        }

        //DODAWANIE WARTOŚCI POPRZEZ ValueController
        @FXML
        public void handleAddValue(ActionEvent event) {
                this.valueController.handleAddValue();
                valueController.updateValueTable();
        }

        @FXML
        public void handleRemoveValue(ActionEvent event) {
                Value selectedValue = this.valueTable.getSelectionModel().getSelectedItem();
                if (selectedValue != null) {
                        // Wywołujemy metodę usuwania wartości z ValueController
                        this.valueController.handleRemoveValue();
                        // Aktualizujemy tabelę agentów
                        valueController.updateValueTable();
                        // Teraz musimy zaktualizować AVController o usunięciu wartosci
                        // Przyjmujemy, że mamy listę usuniętych wartości (w tym przypadku tylko jeden)
                        List<Value> removedValues = new ArrayList<>();
                        removedValues.add(selectedValue);
                        avController.removeDeletedValueEntriesFromTable(removedValues);
                }
        }

        //DODAWANIA WAG AGENT-VALUE WEIGHTS (I SKALI) POPRZEZ AVController
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

       // @FXML
       // public void handleRemoveAgent() {
       //         // Załóżmy, że mamy jakąś logikę do wybrania i usunięcia agenta
       //         Agent selectedAgent = this.agentTable.getSelectionModel().getSelectedItem();
       //         if (selectedAgent != null) {
       //                 // Usuwamy agenta
       //                 project.getListAgent().removeAgent(selectedAgent);
       //                 // Teraz musimy zaktualizować AVController o usunięciu agenta
       //                 // Przyjmujemy, że mamy listę usuniętych agentów (w tym przypadku tylko jeden)
       //                 List<Agent> removedAgents = new ArrayList<>();
       //                 removedAgents.add(selectedAgent);
       //                 avController.removeDeletedEntriesFromTable(removedAgents);
       //         }
       // }

        @Override
        public void updateAV(AVController avController) {
                this.avWeights = avController.getAvWeights();
                this.project.getAgentValueToWeight().setAgentValueWeights(this.avWeights.getAgentValueWeights());
                // Update the ObservableList
                avPairs.clear();
                avPairs.addAll(avWeights.entrySet().stream()
                        .map(entry -> new AVPair(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList()));
                this.avController.setAvWeights(this.avWeights);
                avTable.setItems(avPairs);  // Bind the ObservableList to the TableView

                // Here is the added call to update the table
                this.avController.updateAVTable();
                System.out.println(project.getAgentValueToWeight().getAgentValueWeights()); // pusta lista
        }

        //DODAWANIE WAG AGENT-VALUE-PROPOSITION WEIGHTS (I SKALI) POPRZEZ AVPController
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
        public void updateAVP(AgentValuePropWeight agentValuePropWeight) {
                this.avpWeights = agentValuePropWeight;
                List<AVPPair> avPairList = avpWeights.entrySet().stream()
                        .map(entry -> new AVPPair(entry.getKey(), entry.getValue()))
                        .collect(Collectors.toList());
                this.avpController.setAvpWeights(this.avpWeights);
                avpTable.setItems(FXCollections.observableArrayList(avPairList));

                this.avpController.updateAVPTable();
        }

        //DODAWANIE INCOMPPROP i 2 decyzji w IncompPropController
        @FXML
        public void handleAddIncompButtonAction(ActionEvent actionEvent) {
                incompPropController.handleAddButtonAction(actionEvent);
        }

        @FXML
        public void handleRemoveIncompButtonAction(ActionEvent actionEvent) {
                incompPropController.handleRemoveButtonAction(actionEvent);
        }

        //DODAWANIE Zasad w RuleController, gdzie także dodajemy kolejne okienko do dodawania zasad.
        @FXML
        public void handleAddRuleButton(ActionEvent actionEvent) {
                Proposition decision1 = project.getListIncompProp().getDecisions().getFirst();
                Proposition decision2 = project.getListIncompProp().getDecisions().getSecond();
                if (decision1 == null || decision2 == null) {
                        // Pokaż komunikat o błędzie
                        return;
                }

                // Tworzymy nową instancję RuleController i dostarczamy jej zależności
                RuleController ruleController = new RuleController(this);
                ruleController.setDecisions(decision1, decision2);

                try {
                        // Używamy FXMLLoader do załadowania pliku fxml
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createRule.fxml"));
                        loader.setController(ruleController);  // Ustawiamy nasz RuleController jako kontroler dla załadowanego pliku fxml
                        Parent root = loader.load();  // Ładujemy plik fxml

                        // Tworzymy nowe okno i wyświetlamy załadowany interfejs użytkownika
                        Stage stage = new Stage();
                        stage.setScene(new Scene(root));
                        stage.show();
                } catch (IOException e) {
                        e.printStackTrace();
                }

                // Aktualizujemy tabelę po dodaniu zasady
                updateRulesTable();
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
        }

        @FXML
        public void handleHelpButton(ActionEvent actionEvent) {
        }

        @FXML
        public void handleNewButton(ActionEvent actionEvent) {
        }

        @FXML
        public void handleMenuButton(ActionEvent actionEvent) {
        }

        @FXML
        public void handleExitButton(ActionEvent actionEvent) {
        }


        @Override
        public void updateRuleController(RuleController ruleController) {

        }

        @Override
        public void updateValueController(ValueController valueController) {
                List<Value> oldList = project.getListValue().getValues();
                List<Value> newList = valueController.getProject().getListValue().getValues();

                if (newList.size() > oldList.size()) {
                        // Dodano nowego agenta
                        for (Value newValue : newList) {
                                if (!oldList.contains(newValue)) {
                                        // Znaleźliśmy dodanego agenta
                                        project.getListValue().addValue(newValue);
                                }
                        }
                } else if (newList.size() < oldList.size()) {
                        // Usunięto agenta
                        for (Value oldValue : oldList) {
                                if (!newList.contains(oldValue)) {
                                        // Znaleźliśmy usuniętego agenta
                                        project.getListValue().removeValue(oldValue);
                                }
                        }
                }
        }

        @Override
        public void updateAgentController(AgentController agentController) {
                List<Agent> oldList = project.getListAgent().getAgents();
                List<Agent> newList = agentController.getProject().getListAgent().getAgents();

                if (newList.size() > oldList.size()) {
                        // Dodano nowego agenta
                        for (Agent newAgent : newList) {
                                if (!oldList.contains(newAgent)) {
                                        // Znaleźliśmy dodanego agenta
                                        project.getListAgent().addAgent(newAgent);
                                }
                        }
                } else if (newList.size() < oldList.size()) {
                        // Usunięto agenta
                        for (Agent oldAgent : oldList) {
                                if (!newList.contains(oldAgent)) {
                                        // Znaleźliśmy usuniętego agenta
                                        project.getListAgent().removeAgent(oldAgent);
                                }
                        }
                }
        }

        @Override
        public void updatePropositionController(PropositionController propositionController) {
                List<Proposition> oldList = project.getListProposition().getListProposition();
                List<Proposition> newList = propositionController.getProject().getListProposition().getListProposition();

                if (newList.size() > oldList.size()) {
                        // Dodano nowego agenta
                        for (Proposition newProposition : newList) {
                                if (!oldList.contains(newProposition)) {
                                        // Znaleźliśmy dodanego agenta
                                        project.getListProposition().addProposition(newProposition);
                                }
                        }
                } else if (newList.size() < oldList.size()) {
                        // Usunięto agenta
                        for (Proposition oldProposition : oldList) {
                                if (!newList.contains(oldProposition)) {
                                        // Znaleźliśmy usuniętego agenta
                                        project.getListProposition().removeProposition(oldProposition);
                                }
                        }
                }
        }

        @Override
        public void updateIncompController(IncompPropController incompPropController) {

        }
}