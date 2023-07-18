package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.dataStructures.AVPPair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class AVPController {
    private AgentValuePropWeight avpWeights;
    private ProjectController projectController;
    private TableView<AVPPair> avpTable;
    private TableColumn<AVPPair, Integer> avpIdColumn;
    private TableColumn<AVPPair, String> avpAgentsColumn;
    private TableColumn<AVPPair, String> avpValuesColumn;
    private TableColumn<AVPPair, String> avpPropositionColumn;
    private TableColumn<AVPPair, Integer> avpWeightsColumn;
    private Slider minSlider;

    private Slider maxSlider;

    private ComboBox<Weight> weightsComboBox;

    public AVPController(AgentValuePropWeight avpWeights,
                        ProjectController projectController,
                        TableView<AVPPair> avpTable,
                        TableColumn<AVPPair, Integer> avpIdColumn,
                        TableColumn<AVPPair, String> avpAgentsColumn,
                        TableColumn<AVPPair, String> avpValuesColumn,
                         TableColumn<AVPPair, String> avpPropositionColumn,
                        TableColumn<AVPPair, Integer> avpWeightsColumn,
                        Slider minSlider,
                        Slider maxSlider,
                        ComboBox<Weight> weightsComboBox) {
        this.avpWeights = avpWeights;
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

    public void addWeight() {
        // Retrieve selected AgentValue from the table view
        AVPPair selectedAVP = avpTable.getSelectionModel().getSelectedItem();
        // Retrieve selected Weight from the combo box
        Weight selectedWeight = weightsComboBox.getValue();
        // If no item is selected in the table view or in the combo box, do nothing
        if (selectedAVP == null || selectedWeight == null) {
            return;
        }
        // Check the type of weight value
        Object weightValue = selectedWeight.getWeight();
        if (weightValue instanceof Integer) {
            avpWeights.editWeight(selectedAVP.getAgentValueProposition(), (Integer) weightValue);
        } else if (weightValue instanceof String && weightValue.equals("?")) {
            avpWeights.editWeight(selectedAVP.getAgentValueProposition(), null);
        }
    }

    public void randomizeWeights() {
        // Randomize weights in the model
        Random random = new Random();
        for (AgentValueProposition avp : avpWeights.keySet()) {
            Integer newWeight = avpWeights.getScale().getMin() +
                    (avpWeights.getScale().getMax() - avpWeights.getScale().getMin()) * random.nextInt();
            avpWeights.editWeight(avp, newWeight);
        }
    }

    public void changeScale() {
        // Get values from the sliders
        int min = (int) minSlider.getValue();
        int max = (int) maxSlider.getValue();
        // Create new scale
        Scale newScale = new Scale(min, max);
        // Update the scale in the model
        avpWeights.setScale(newScale);
    }

    public void updateAVPTable() {
        // Update the table view
        List<AVPPair> pairs = avpWeights.entrySet().stream()
                .map(entry -> new AVPPair(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
        avpTable.setItems(FXCollections.observableList(pairs));
    }
}
