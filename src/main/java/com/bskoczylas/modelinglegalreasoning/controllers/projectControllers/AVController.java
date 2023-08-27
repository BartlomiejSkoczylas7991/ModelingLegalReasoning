package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable.AVObservableController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.AVObserverController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.dataStructures.AVPair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class AVController implements AVObservableController {
    private AgentValueToWeight avWeights;
    private ProjectController projectController;
    private TableView<AVPair> avTable;
    
    private TableColumn<AVPair, Integer> avIdColumn;
    
    private TableColumn<AVPair, String> avAgentsColumn;
    
    private TableColumn<AVPair, String> avValuesColumn;
    
    private TableColumn<AVPair, String> avWeightsColumn;
    
    private Slider minSlider;
    
    private Slider maxSlider;
    
    private ComboBox<Weight> weightsComboBox;
    private final List<AVObserverController> observers = new ArrayList<>();

    public AVController(AgentValueToWeight avWeights,
                        ProjectController projectController,
                        TableView<AVPair> avTable,
                        TableColumn<AVPair, Integer> avIdColumn,
                        TableColumn<AVPair, String> avAgentsColumn,
                        TableColumn<AVPair, String> avValuesColumn,
                        TableColumn<AVPair, String> avWeightsColumn,
                        Slider minSlider,
                        Slider maxSlider,
                        ComboBox<Weight> weightsComboBox) {
        this.avWeights = projectController.getProject().getAgentValueToWeight();
        this.projectController = projectController;
        this.avTable = avTable;
        this.avIdColumn = avIdColumn;
        this.avAgentsColumn = avAgentsColumn;
        this.avValuesColumn = avValuesColumn;
        this.avWeightsColumn = avWeightsColumn;
        this.minSlider = minSlider;
        this.maxSlider = maxSlider;
        this.weightsComboBox = weightsComboBox;
    }

    public AgentValueToWeight getAvWeights() {
        return avWeights;
    }

    public void addWeight() {
        AVPair selectedAV = avTable.getSelectionModel().getSelectedItem();
        Weight selectedWeight = weightsComboBox.getValue();
        if (selectedAV == null || selectedWeight == null) {
            showAlert();
            return;
        }
        if (selectedWeight.isIndeterminate()) {
            avWeights.editWeight(selectedAV.getAgentValue(), null);
        } else {
            avWeights.editWeight(selectedAV.getAgentValue(), selectedWeight.getNumberValue());
        }

        notifyAVObservers();
        avTable.refresh();
    }

    public void removeDeletedAgentEntriesFromTable(List<Agent> removedAgents) {
        List<AVPair> currentTableEntries = avTable.getItems();

        currentTableEntries.removeIf(avPair -> removedAgents.contains(avPair.getAgentValue().getAgent()));

        avTable.setItems(FXCollections.observableArrayList(currentTableEntries));

        updateAVTable();
    }

    public void removeDeletedValueEntriesFromTable(List<Value> removedValues) {
        List<AVPair> currentTableEntries = avTable.getItems();

        currentTableEntries.removeIf(avPair -> removedValues.contains(avPair.getAgentValue().getValue()));

        avTable.setItems(FXCollections.observableArrayList(currentTableEntries));

        updateAVTable();
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
        int min = avWeights.getScale().getMin();
        int max = avWeights.getScale().getMax();

        if (min < max) {
            for (int i = min; i <= max; i++) {
                weights.add(Weight.of(i));

            }
        }
        weightsComboBox.setItems(FXCollections.observableArrayList(weights));
    }

    public void randomizeWeights() {
        Random random = new Random();
        for (AgentValue av : avWeights.keySet()) {
            int min = avWeights.getScale().getMin();
            int max = avWeights.getScale().getMax();
            Integer newWeight = min + random.nextInt(max - min + 1);
            avWeights.editWeight(av, newWeight);
        }
        avTable.refresh();
        notifyAVObservers();
    }

    public void changeScale() {
        int min = (int) minSlider.getValue();
        int max = (int) maxSlider.getValue();
        Scale newScale = new Scale(min, max);
        avWeights.updateScale(newScale);

        updateWeightsComboBox();

        projectController.getProject().notifyProjectObservers(projectController.getProject());

        Platform.runLater(() -> {
            updateAVTable();
        });
        avTable.refresh();
        notifyAVObservers();
    }

    public void setAvWeights(AgentValueToWeight avWeights) {
        this.avWeights = avWeights;
    }

    public void updateAVTable() {
        Map.Entry<AgentValue, Weight>[] entries = avWeights.getAgentValueWeights().entrySet().toArray(new Map.Entry[0]);
        List<AVPair> pairs = IntStream.range(0, entries.length)
                .mapToObj(i -> {
                    AVPair pair = new AVPair(entries[i].getKey(), entries[i].getValue());
                    pair.setId(i+1);
                    return pair;
                })
                .collect(Collectors.toList());
        avTable.setItems(FXCollections.observableList(pairs));
    }

    public void changeScaleFromAVP(Scale newScale) {
        this.avWeights.setScale(newScale);
        setScaleSlider(newScale);
        updateWeightsComboBox();
        updateAVTable();
        notifyAVObservers();
    }

    public void setScaleSlider(Scale scale) {
        this.minSlider.setValue(scale.getMin());
        this.maxSlider.setValue(scale.getMax());
    }

    @Override
    public void addAVObserver(AVObserverController observer) {
        observers.add(observer);
    }

    @Override
    public void removeAVObserver(AVObserverController observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAVObservers() {
        for (AVObserverController observer : this.observers) {
            observer.updateAV(this);
        }
    }


}
