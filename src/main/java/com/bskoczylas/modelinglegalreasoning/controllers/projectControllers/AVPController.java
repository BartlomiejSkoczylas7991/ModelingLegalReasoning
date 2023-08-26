package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable.AVPObservableController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.AVPObserverController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.dataStructures.AVPPair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AVPController implements AVPObservableController {
    private AgentValuePropWeight avpWeights;
    private ProjectController projectController;
    private TableView<AVPPair> avpTable;
    private TableColumn<AVPPair, Integer> avpIdColumn;
    private TableColumn<AVPPair, String> avpAgentsColumn;
    private TableColumn<AVPPair, String> avpValuesColumn;
    private TableColumn<AVPPair, String> avpPropositionColumn;
    private TableColumn<AVPPair, String> avpWeightsColumn;
    private Slider minSlider;

    private Slider maxSlider;

    private ComboBox<Weight> weightsComboBox;
    private final List<AVPObserverController> observers = new ArrayList<>();

    public AVPController(AgentValuePropWeight avpWeights,
                        ProjectController projectController,
                        TableView<AVPPair> avpTable,
                        TableColumn<AVPPair, Integer> avpIdColumn,
                        TableColumn<AVPPair, String> avpAgentsColumn,
                        TableColumn<AVPPair, String> avpValuesColumn,
                         TableColumn<AVPPair, String> avpPropositionColumn,
                        TableColumn<AVPPair, String> avpWeightsColumn,
                        Slider minSlider,
                        Slider maxSlider,
                        ComboBox<Weight> weightsComboBox) {
        this.avpWeights = projectController.getProject().getAgentValuePropWeight();
        this.projectController = projectController;
        this.avpTable = avpTable;
        this.avpIdColumn = avpIdColumn;
        this.avpAgentsColumn = avpAgentsColumn;
        this.avpValuesColumn = avpValuesColumn;
        this.avpPropositionColumn = avpPropositionColumn;
        this.avpWeightsColumn = avpWeightsColumn;
        this.minSlider = minSlider;
        this.maxSlider = maxSlider;
        this.weightsComboBox = weightsComboBox;
    }

    public AgentValuePropWeight getAvpWeights() {
        return this.avpWeights;
    }

    public void addWeight() {
        AVPPair selectedAVP = avpTable.getSelectionModel().getSelectedItem();
        Weight selectedWeight = weightsComboBox.getValue();
        if (selectedAVP == null || selectedWeight == null) {
            showAlert();
            return;
        }
        Object weightValue = selectedWeight.getWeight();
        if (weightValue instanceof Integer) {
            avpWeights.editWeight(selectedAVP.getAgentValueProposition(), (Integer) weightValue);
        } else if (weightValue instanceof String && weightValue.equals("?")) {
            avpWeights.editWeight(selectedAVP.getAgentValueProposition(), null);
        }
        notifyAVPObservers();

        avpTable.refresh();
    }

    public void removeDeletedAgentEntriesFromTable(List<Agent> removedAgents) {
        List<AVPPair> currentTableEntries = avpTable.getItems();

        currentTableEntries.removeIf(avPPair -> removedAgents.contains(avPPair.getAgentValueProposition().getAgent()));

        avpTable.setItems(FXCollections.observableArrayList(currentTableEntries));

        updateAVPTable();
    }

    public void removeDeletedValueEntriesFromTable(List<Value> removedValues) {
        List<AVPPair> currentTableEntries = avpTable.getItems();

        currentTableEntries.removeIf(avPPair -> removedValues.contains(avPPair.getAgentValueProposition().getValue()));

        avpTable.setItems(FXCollections.observableArrayList(currentTableEntries));

        updateAVPTable();
    }

    public void removeDeletedPropositionEntryFromTable(Proposition removedProposition) {
        List<AVPPair> currentTableEntries = new ArrayList<>(avpTable.getItems());

        currentTableEntries.removeIf(avPPair -> avPPair.getAgentValueProposition().getProposition().equals(removedProposition));

        avpTable.setItems(FXCollections.observableArrayList(currentTableEntries));

        updateAVPTable();
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Selection");
        alert.setHeaderText(null);
        alert.setContentText("Please select an item in the table and a weight.");
        alert.showAndWait();
    }

    public void updateWeightsComboBox() {
        List<Weight> weights = new ArrayList<>();
        int min = avpWeights.getScale().getMin();
        int max = avpWeights.getScale().getMax();

        if (min <= max) {
            for (int i = min; i <= max; i++) {
                weights.add(Weight.of(i));
            }
        }

        weightsComboBox.setItems(FXCollections.observableArrayList(weights));
    }

    public void randomizeWeights() {
        Random random = new Random();
        for (AgentValueProposition avp : avpWeights.keySet()) {
            int min = avpWeights.getScale().getMin();
            int max = avpWeights.getScale().getMax();
            Integer newWeight = min + random.nextInt(max - min + 1);
            this.avpWeights.editWeight(avp, newWeight);
        }
        avpTable.refresh();
        notifyAVPObservers();
    }

    public void changeScale() {
        int min = (int) minSlider.getValue();
        int max = (int) maxSlider.getValue();
        Scale newScale = new Scale(min, max);
        avpWeights.setScale(newScale);

        updateWeightsComboBox();

        Platform.runLater(() -> {
            updateAVPTable();
        });
        avpTable.refresh();
        notifyAVPObservers();
    }

    public void setAvpWeights(AgentValuePropWeight avpWeights) {
        this.avpWeights = avpWeights;
    }

    public void updateAVPTable() {
        Map.Entry<AgentValueProposition, Weight>[] entries = avpWeights.getAgentValuePropWeights().entrySet().toArray(new Map.Entry[0]);
        List<AVPPair> pairs = IntStream.range(0, entries.length)
                .mapToObj(i -> {
                    AVPPair pair = new AVPPair(entries[i].getKey(), entries[i].getValue());
                    pair.setId(i+1);
                    return pair;
                })
                .collect(Collectors.toList());
        avpTable.setItems(FXCollections.observableList(pairs));
    }

    public void changeScale(Scale newScale) {
        this.avpWeights.setScale(newScale);
        updateWeightsComboBox();
        updateAVPTable();
        notifyAVPObservers();
    }

    @Override
    public void addObserver(AVPObserverController observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(AVPObserverController observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAVPObservers() {
        for (AVPObserverController observer : this.observers) {
            observer.updateAVP(this);
        }
    }
}
