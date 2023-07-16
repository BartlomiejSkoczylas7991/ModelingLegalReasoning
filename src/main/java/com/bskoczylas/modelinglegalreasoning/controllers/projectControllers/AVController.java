package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.dataStructures.AVPair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Scale;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Weight;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AVController {
    private AgentValueToWeight avWeights;
    private ProjectController projectController;
    private TableView<AVPair> aVTable;
    
    private TableColumn<AVPair, Integer> aIdColumn;
    
    private TableColumn<AVPair, String> aVAgentsColumn;
    
    private TableColumn<AVPair, String> aVValuesColumn;
    
    private TableColumn<AVPair, Integer> aVWeightsColumn;
    
    private Button aVaddButton;
    
    private Button aVEditButton;
    
    private Button aVRandomButton;
    
    private Button changeScaleButton;
    
    private Slider minSlider;
    
    private Slider maxSlider;
    
    private ComboBox<Weight> weightsComboBox;

    public AVController(AgentValueToWeight avWeights,
                        ProjectController projectController,
                        TableView<AVPair> aVTable,
                        TableColumn<AVPair, Integer> aIdColumn,
                        TableColumn<AVPair, String> aVAgentsColumn,
                        TableColumn<AVPair, String> aVValuesColumn,
                        TableColumn<AVPair, Integer> aVWeightsColumn,
                        Button aVaddButton,
                        Button aVEditButton,
                        Button aVRandomButton,
                        Button changeScaleButton,
                        Slider minSlider,
                        Slider maxSlider,
                        ComboBox<Weight> weightsComboBox) {
        this.avWeights = avWeights;
        this.projectController = projectController;
        this.aVTable = aVTable;
        this.aIdColumn = aIdColumn;
        this.aVAgentsColumn = aVAgentsColumn;
        this.aVValuesColumn = aVValuesColumn;
        this.aVWeightsColumn = aVWeightsColumn;
        this.aVaddButton = aVaddButton;
        this.aVEditButton = aVEditButton;
        this.aVRandomButton = aVRandomButton;
        this.changeScaleButton = changeScaleButton;
        this.minSlider = minSlider;
        this.maxSlider = maxSlider;
        this.weightsComboBox = weightsComboBox;
    }

    public void addWeight() {
        // Retrieve selected AgentValue from the table view
        AVPair selectedAV = aVTable.getSelectionModel().getSelectedItem();
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

    public void editWeight() {
        // The logic here would be the same as addWeight, but might include some checks for if the weight has been changed or not
    }

    public void randomizeWeights() {
        // Randomize weights in the model
        Random random = new Random();
        for (AgentValue av : avWeights.keySet()) {
            Double newWeight = avWeights.getScale().getMin() +
                    (avWeights.getScale().getMax() - avWeights.getScale().getMin()) * random.nextDouble();
            avWeights.editWeight(av,Double.valueOf(newWeight).intValue());
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
        aVTable.setItems(FXCollections.observableList(pairs));
    }
}
