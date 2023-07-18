package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.dataStructures.AVPair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Scale_Weight.Scale;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Scale_Weight.Weight;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AVController {
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
        this.avWeights = avWeights;
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

    public void addWeight() {
        // Retrieve selected AgentValue from the table view
        AVPair selectedAV = avTable.getSelectionModel().getSelectedItem();
        // Retrieve selected Weight from the combo box
        Weight selectedWeight = weightsComboBox.getValue();
        // If no item is selected in the table view or in the combo box, do nothing
        if (selectedAV == null || selectedWeight == null) {
            return;
        }
        // Check the type of weight value
        Object weightValue = selectedWeight.getWeight();
        if (weightValue instanceof Integer) {
            avWeights.editWeight(selectedAV.getAgentValue(), (Integer) weightValue);
        } else if (weightValue instanceof String && weightValue.equals("?")) {
            avWeights.editWeight(selectedAV.getAgentValue(), null);
        }
    }

    public void randomizeWeights() {
        // Randomize weights in the model
        Random random = new Random();
        for (AgentValue av : avWeights.keySet()) {
            Integer newWeight = avWeights.getScale().getMin() +
                    (avWeights.getScale().getMax() - avWeights.getScale().getMin()) * random.nextInt();
            avWeights.editWeight(av, newWeight);
        }
    }

    public void changeScale() {
        // Get values from the sliders
        int min = (int) minSlider.getValue();
        int max = (int) maxSlider.getValue();
        // Create new scale
        Scale newScale = new Scale(min, max);
        // Update the scale in the model
        avWeights.setScale(newScale);
    }

    public void updateAVTable() {
        // Update the table view
        List<AVPair> pairs = avWeights.entrySet().stream()
                .map(entry -> new AVPair(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        avTable.setItems(FXCollections.observableList(pairs));
    }
}
