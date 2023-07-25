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
    
    private TableColumn<AVPair, Integer> avWeightsColumn;
    
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
                        TableColumn<AVPair, Integer> avWeightsColumn,
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
        // Retrieve selected AgentValue from the table view
        AVPair selectedAV = avTable.getSelectionModel().getSelectedItem();
        // Retrieve selected Weight from the combo box
        Weight selectedWeight = weightsComboBox.getValue();
        // If no item is selected in the table view or in the combo box, show a dialog
        if (selectedAV == null || selectedWeight == null) {
            showAlert();
            return;
        }
        // Check the type of weight value
        Object weightValue = selectedWeight.getWeight();
        if (weightValue instanceof Integer) {
            avWeights.editWeight(selectedAV.getAgentValue(), (Integer) weightValue);
        } else if (weightValue instanceof String && weightValue.equals("?")) {
            avWeights.editWeight(selectedAV.getAgentValue(), null);
        }

        // Notify the project about the change (TO MOŻE BYĆ ŹLE - może być notify() zwykłe jednak
        notifyAVObservers();
        // Force the table to refresh
        avTable.refresh();
    }

    public void removeDeletedAgentEntriesFromTable(List<Agent> removedAgents) {
        // Pobierz aktualne wpisy w tabeli
        List<AVPair> currentTableEntries = avTable.getItems();

        // Usuń wpisy z tabeli, które są związane z usuniętymi agentami
        currentTableEntries.removeIf(avPair -> removedAgents.contains(avPair.getAgentValue().getAgent()));

        // Ustaw zaktualizowaną listę jako nowe elementy tabeli
        avTable.setItems(FXCollections.observableArrayList(currentTableEntries));

        // Aktualizuj tabelę
        updateAVTable();
    }

    public void removeDeletedValueEntriesFromTable(List<Value> removedValues) {
        // Pobierz aktualne wpisy w tabeli
        List<AVPair> currentTableEntries = avTable.getItems();

        // Usuń wpisy z tabeli, które są związane z usuniętymi agentami
        currentTableEntries.removeIf(avPair -> removedValues.contains(avPair.getAgentValue().getValue()));

        // Ustaw zaktualizowaną listę jako nowe elementy tabeli
        avTable.setItems(FXCollections.observableArrayList(currentTableEntries));

        // Aktualizuj tabelę
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

        // Check if min is less than or equal to max
        if (min <= max) {
            for (int i = min; i <= max; i++) {
                // assume Weight class has a constructor that accepts an int
                weights.add(new Weight(this.projectController.getProject().getScale(), i));
            }
        }

        weightsComboBox.setItems(FXCollections.observableArrayList(weights));
    }

    public void randomizeWeights() {
        // Randomize weights in the model
        Random random = new Random();
        for (AgentValue av : avWeights.keySet()) {
            int min = avWeights.getScale().getMin();
            int max = avWeights.getScale().getMax();
            Integer newWeight = min + random.nextInt(max - min + 1);
            avWeights.editWeight(av, newWeight);
        }
        // Force the table to refresh
        avTable.refresh();
        // Notify the project about the change
        notifyAVObservers();
    }

    public void changeScale() {
        // Get values from the sliders
        int min = (int) minSlider.getValue();
        int max = (int) maxSlider.getValue();
        // Create new scale
        Scale newScale = new Scale(min, max);
        // Update the scale in the model
        avWeights.updateScale(newScale);

        // Create new weights with the new scale
        updateWeightsComboBox();

        // Notify the project about the change
        projectController.getProject().notifyProjectObservers(projectController.getProject());

        // Update the table view after all other tasks have completed
        Platform.runLater(() -> {
            updateAVTable();
        });
        // Update the table view
        avTable.refresh();
        notifyAVObservers();
    }

    public void setAvWeights(AgentValueToWeight avWeights) {
        this.avWeights = avWeights;
    }

    public void updateAVTable() {
        // Update the table view
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
