package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import javafx.beans.property.SimpleStringProperty;

public class AgentTableEntry {
    private final SimpleStringProperty agentName;
    private final SimpleStringProperty reasoningChain;

    public AgentTableEntry(String agentName, String reasoningChain) {
        this.agentName = new SimpleStringProperty(agentName);
        this.reasoningChain = new SimpleStringProperty(reasoningChain);
    }

    public String getAgentName() {
        return agentName.get();
    }

    public String getReasoningChain() {
        return reasoningChain.get();
    }
}
