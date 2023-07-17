package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule.ListRules;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.List;

public class RuleController {
    private ProjectController projectController;
    private ListRules listRules;
    private List<Proposition> premises;
    @FXML
    private Label labelTextArea;
    @FXML
    private Button addRuleButton;
    @FXML
    private RadioButton decisionRadioButton1;
    @FXML
    private RadioButton decisionRadioButton2;

    public RuleController(ProjectController projectController) {
        this.projectController = projectController;
        this.listRules = projectController.getListRules();  // Zakładając, że ProjectController posiada getter dla ListRules
    }

    @FXML
    private void handleEnterKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            // Tutaj możesz wykonać dowolne działania po naciśnięciu Enter
            String text = labelTextArea.getText();
            System.out.println("Wprowadzony tekst: " + text);
        }
    }

}
