package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class HelpController {
    @FXML
    private TextArea helpTextArea;
    @FXML
    private TextFlow helpTextFlow;

    public void initialize() {

        Text titleIntroduction = new Text("Introduction\n");
        titleIntroduction.getStyleClass().add("title");
        Text contentIntroduction = new Text("Welcome to the Legal Reasoning Modeling program! This tool is based on the scientific work of two researchers: Wyner and Żurek. With it, you can analyze complex legal reasoning processes in a clear and efficient way.\n");
        contentIntroduction.getStyleClass().add("normal");

        Text titleProgramStructure = new Text("Program Structure\n");
        titleProgramStructure.getStyleClass().add("title");
        Text contentProgramStructure = new Text("Project\nAgents: Enter the agent's name.\nProposals: Enter the name and decide if it is the final decision.\n..."); // kontynuuj tekst sekcji
        contentProgramStructure.getStyleClass().add("normal");

        Text titleHowDoesItWork = new Text("How does it work?\n");
        titleHowDoesItWork.getStyleClass().add("title");
        Text contentHowDoesItWork = new Text("Defining PropBaseClean: Based on the entered weights, we create a PropBaseClean set for each agent.\n" +
        "Creating a Knowledge Base: Based on PropBaseClean and the created rules, we create knowledge bases for agents.\n" +
                "Reasoning Chain: For each agent, we create a reasoning chain using the newly created knowledge base.\n" +
                "Voting: After analyzing each agent's decisions, voting is conducted among all agents.\n" +
                "Creating Consortiums: Consortia of agents are created based on voting results.\n"); // kontynuuj tekst sekcji
        contentHowDoesItWork.getStyleClass().add("normal");

        Text titleRequirements = new Text("Requirements and Rules\n");
        titleRequirements.getStyleClass().add("title");
        Text contentRequirements = new Text("You can only generate a report when the following requirements are met:\n" +
                "\n" +
                "1. Minimum of 2 agents.\n" +
                "2. Minimum of 2 values.\n" +
                "3. Minimum of 4 proposals.\n" +
                "4. Minimum of 2 rules.\n" +
                "5. Two proposals being decisions defined in the IncompProp window.\n" +
                "\nAdditional Rules:\n" +
                "1. When setting weights for AgentValuePropWeight, remember that one weight greater than the corresponding value in AgentValueToWeight is enough for a proposal to pass through the filter.\n" +
                "2. Proposals decided as 'final decisions' cannot be used with premises in rules.\n" +
                "3. Proposals not used in setting rules (being in premises) will not be considered in values.\n" +
                "4. Avoid creating overly complicated reasoning chains that can make analysis unreadable and difficult to understand.\n" +
                "\n" +
                "Assign unique names to agents, values, and proposals.\n" +
                "You cannot add conflicting rules.\n" +
                "And many other rules described in the upper part of this text (from points 4 to 9).\n"); // kontynuuj tekst sekcji
        contentRequirements.getStyleClass().add("normal");

        Text titleTips = new Text("Tips and Tricks\n");
        titleTips.getStyleClass().add("title");
        Text contentTips = new Text("Use the 'Help' feature at any time if you have questions or doubts.\n" +
                "Start by defining basic elements such as agents, proposals, and values before moving on to more complicated tasks like creating rules or setting weights.\n");
        contentTips.getStyleClass().add("normal");

        helpTextFlow.getChildren().addAll(
                titleIntroduction, contentIntroduction,
                titleProgramStructure, contentProgramStructure,
                titleHowDoesItWork, contentHowDoesItWork,
                titleRequirements, contentRequirements,
                titleTips, contentTips
        );
        StringBuilder combinedText = new StringBuilder();

        for (Node node : helpTextFlow.getChildren()) {
            if (node instanceof Text) {
                combinedText.append(((Text) node).getText());
            }
        }

        helpTextArea.setText(combinedText.toString());
        helpTextFlow.getChildren().clear();
    }
}
