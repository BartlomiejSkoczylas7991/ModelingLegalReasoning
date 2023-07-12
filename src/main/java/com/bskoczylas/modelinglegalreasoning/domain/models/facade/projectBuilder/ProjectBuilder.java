package com.bskoczylas.modelinglegalreasoning.domain.models.facade.projectBuilder;

import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.ListValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AVP.AgentValuePropWeight;

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

    // Methods that set other optional parameters

    public Project build() {
        Project project = new Project(name);

        // Initialization of the project object based on the set parameters
        project.setListAgent(listAgent);
        project.setListProposition(listProposition);
        project.setListValue(listValue);
        project.setAgentValuePropWeight(agentValuePropWeight);
        project.setAgentValueToWeight(agentValueToWeight);
        project.setListIncompProp(listIncompProp);
        project.setListRules(listRules);
        project.setCreatedDate(createdDate);
        project.setLastModifiedDate(lastModifiedDate);

        return project;
    }
}
