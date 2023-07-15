package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Scale;
import javafx.collections.FXCollections;
import javafx.scene.control.*;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Weight;

import java.util.ArrayList;
import java.util.List;

public class AVController {
    private AgentValueToWeight avWeights;
    private ProjectController projectController;
    private TableView<AgentValue> AVTable;
    
    private TableColumn<AgentValue, Integer> AVIdColumn;
    
    private TableColumn<AgentValue, String> AVAgentsColumn;
    
    private TableColumn<AgentValue, String> AVValuesColumn;
    
    private TableColumn<AgentValue, Double> AVWeightsColumn;
    
    private Button AVaddButton;
    
    private Button AVEditButton;
    
    private Button AVRandomButton;
    
    private Button changeScaleButton;
    
    private Slider minSlider;
    
    private Slider maxSlider;
    
    private ComboBox<Weight> weightsComboBox;

    public AVController(AgentValueToWeight avWeights, ProjectController projectController) {
        // TODO: więcej wstrzyknąć
        this.avWeights = avWeights;
        this.projectController = projectController;
    }

    private void addWeight() {
        // Retrieve selected AgentValue from the table view
        AgentValue selectedAV = AVTable.getSelectionModel().getSelectedItem();
        // Retrieve selected Weight from the combo box
        Weight selectedWeight = weightsComboBox.getValue();
        // If no item is selected in the table view or in the combo box, do nothing
        if (selectedAV == null || selectedWeight == null) {
            return;
        }
        // Update the weight in the model
        avWeights.editWeight(selectedAV, selectedWeight.getWeight());

    }

    private void editWeight() {
        // The logic here would be the same as addWeight, but might include some checks for if the weight has been changed or not
    }

    private void randomizeWeights() {
        // Randomize weights in the model
        for (AgentValue av : avWeights.keySet()) {
            double newWeight = Math.random() * (avWeights.getScale().getMax() - avWeights.getScale().getMin()) + avWeights.getScale().getMin();
            avWeights.editWeight(av, newWeight);
        }
    }

    private void changeScale() {
        // Get values from the sliders
        double min = minSlider.getValue();
        double max = maxSlider.getValue();
        // Create new scale
        Scale newScale = new Scale(min, max);
        // Update the scale in the model
        avWeights.setScale(newScale);
    }

    public void updateAVTable() {
        // Get the data from the model
        List<AgentValue> avList = new ArrayList<>(avWeights.keySet());
        // Update the table view
        AVTable.setItems(FXCollections.observableArrayList(avList));
    }
}
