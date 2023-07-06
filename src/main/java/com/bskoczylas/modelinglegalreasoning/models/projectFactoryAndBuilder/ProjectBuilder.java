package com.bskoczylas.modelinglegalreasoning.models.projectFactoryAndBuilder;

import com.bskoczylas.modelinglegalreasoning.models.Project;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.IncompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Value.ListValue;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Weights.AV.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Weights.AVP.AgentValuePropWeight;

import java.time.LocalDateTime;

public class ProjectBuilder {
    private String name;

    // Optional parameters
    private ListAgent listAgent;
    private ListProposition listProposition;
    private ListValue listValue;
    private ListIncompProp listIncompProp;
    private ListRules listRules;
    private AgentValueToWeight agentValueToWeight;
    private AgentValuePropWeight agentValuePropWeight;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public ProjectBuilder(String name) {
        this.name = name;
    }

    public ProjectBuilder setListAgent(ListAgent listAgent) {
        this.listAgent = listAgent;
        return this;
    }

    public ProjectBuilder setListProposition(ListProposition listProposition) {
        this.listProposition = listProposition;
        return this;
    }

    public ProjectBuilder setListValue(ListValue listValue) {
        this.listValue = listValue;
        return this;
    }

    // Metody ustawiające pozostałe opcjonalne parametry

    public Project build() {
        Project project = new Project(name);

        // Inicjalizacja obiektu projektu na podstawie ustawionych parametrów
        project.setListAgent(listAgent);
        project.setListProposition(listProposition);
        project.setListValue(listValue);
        // ...

        return project;
    }
}
