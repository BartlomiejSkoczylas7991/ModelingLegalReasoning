package com.bskoczylas.modelinglegalreasoning.domain.models;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Consortium.ListConsortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Court.CourtOpinion;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Court.Report;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.KnowledgeBase.ListKnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.PropBaseClean.ListPropBaseClean;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.ReasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule.Rule;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.ListValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Scale_Weight.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AVP.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Decision.Decision;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.projectObserver.ProjectObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.projectObserver.ProjectObserver;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Project implements ProjectObservable {
    private static int nextId = 0;
    @JsonProperty
    private String id; // zapisujemy/wczytujemy
    @JsonProperty
    private String name;
    @JsonProperty
    private ListAgent listAgent = new ListAgent();
    @JsonProperty
    private ListProposition listProposition = new ListProposition();
    @JsonProperty
    private ListValue listValue = new ListValue();
    @JsonProperty
    private ListIncompProp listIncompProp = new ListIncompProp();
    @JsonProperty
    private AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
    @JsonProperty
    private AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
    @JsonProperty
    private Scale scale = new Scale(0,10);
    @JsonProperty
    private ListRules listRules = new ListRules();
    @JsonProperty
    private ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
    @JsonProperty
    private ListKnowledgeBase listKnowledgeBase = new ListKnowledgeBase();
    @JsonProperty
    private ListReasoningChain listReasoningChain = new ListReasoningChain();
    @JsonProperty
    private Decision decision = new Decision();
    @JsonProperty
    private ListConsortium listConsortium = new ListConsortium();
    @JsonProperty
    private CourtOpinion courtOpinion = new CourtOpinion();
    @JsonProperty
    private Report report = new Report();
    @JsonProperty
    private List<ProjectObserver> observers = new ArrayList<>();
    @JsonProperty
    private LocalDateTime createdDate = LocalDateTime.now();
    @JsonProperty
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    public Project() {
        if (name == null || name.trim().isEmpty()) {
            name = "Project" + nextId;
        }
        // for new project
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
        this.name = name;
        this.id = Integer.toString(nextId++);  // set id
        nextId++;  // We increment nextId by 1, so the next Project will have a larger id

        // define
        configureObservers();
    }

    public Project(@JsonProperty("name") String name){
        if (name == null || name.trim().isEmpty()) {
            name = "Project" + nextId;
        }
        // for new project
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
        this.name = name;
        this.id = Integer.toString(nextId++);  // set id


        configureObservers();
    }

    // Injecting observers - dependencies
    public void configureObservers() {
        this.listAgent.addAgentObserver(this.agentValuePropWeight);
        this.listAgent.addAgentObserver(this.agentValueToWeight);
        this.listValue.addObserver(this.agentValuePropWeight);
        this.listValue.addObserver(this.agentValueToWeight);
        this.listProposition.addObserver(this.agentValuePropWeight);
        this.listProposition.addObserver(this.listIncompProp);
        this.scale.addObserver(this.agentValuePropWeight);
        this.scale.addObserver(this.agentValueToWeight);
        this.agentValuePropWeight.addObserver(this.listPropBaseClean);
        this.agentValueToWeight.addAVObserver(this.listPropBaseClean);
        this.listPropBaseClean.addObserver(this.listKnowledgeBase);
        this.listIncompProp.addObserver(this.listProposition);
        this.listRules.addObserver(this.listKnowledgeBase);
        this.listKnowledgeBase.addObserver(this.listReasoningChain);
        this.listIncompProp.addObserver(this.listRules);
        this.listReasoningChain.addObserver(this.decision);
        this.decision.addObserver(this.listConsortium);
        this.listConsortium.addObserver(this.courtOpinion);
        this.courtOpinion.addObserver(this.report);
    }

    @Override
    public void addProjectObserver(ProjectObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeProjectObserver(ProjectObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyProjectObservers(Project project) {
        for (ProjectObserver projectObserver : observers) {
            projectObserver.updateProject(project);
        }
    }
    // For ProjectController
    public boolean hasEnoughData() {
        boolean hasEnoughAgents = this.getListAgent().getAgents().size() >= 2;
        boolean hasEnoughValues = this.getListValue().getValues().size() >= 2;
        boolean hasEnoughPropositions = this.getListProposition().getListProposition().size() >= 4;
        boolean hasEnoughDecisions = this.getListIncompProp().decisionsExist(); // Zmienić zgodnie z rzeczywistą implementacją
        boolean hasEnoughRules = this.getListRules().getListRules().size() >= 2; // Zmienić zgodnie z rzeczywistą implementacją

        return hasEnoughAgents && hasEnoughValues && hasEnoughPropositions && hasEnoughDecisions && hasEnoughRules;
    }

    public List<Report.ReportSection> generateReport() {
        if (!hasEnoughData()) {
            throw new IllegalStateException("Not enough data to generate report");
        }
        return this.report.generateReport();
    }

    //GETTERS AND SETTERS FOR JACKSON
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void updateLastModifiedDate() {
        this.lastModifiedDate = LocalDateTime.now();
    }

    public String getName() {
        return name;
    }

    @JsonProperty
    public void setName(String name) {
        this.name = name;
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        Project.nextId = nextId;
    }

    public ListAgent getListAgent() {
        return listAgent;
    }

    public void setListAgent(ListAgent listAgent) {
        this.listAgent = listAgent;
    }

    public ListProposition getListProposition() {
        return listProposition;
    }

    public void setListProposition(ListProposition listProposition) {
        this.listProposition = listProposition;
    }

    public ListValue getListValue() {
        return listValue;
    }

    public void setListValue(ListValue listValue) {
        this.listValue = listValue;
    }

    public ListKnowledgeBase getListKnowledgeBase() {
        return listKnowledgeBase;
    }

    public void setListKnowledgeBase(ListKnowledgeBase listKnowledgeBase) {
        this.listKnowledgeBase = listKnowledgeBase;
    }

    public ListReasoningChain getListReasoningChain() {
        return listReasoningChain;
    }

    public void setListReasoningChain(ListReasoningChain listReasoningChain) {
        this.listReasoningChain = listReasoningChain;
    }

    public ListConsortium getListConsortium() {
        return listConsortium;
    }

    public void setListConsortium(ListConsortium listConsortium) {
        this.listConsortium = listConsortium;
    }

    public CourtOpinion getCourtOpinion() {
        return courtOpinion;
    }

    public void setCourtOpinion(CourtOpinion courtOpinion) {
        this.courtOpinion = courtOpinion;
    }

    public List<ProjectObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<ProjectObserver> observers) {
        this.observers = observers;
    }

    public ListIncompProp getListIncompProp() {
        return listIncompProp;
    }

    public void setListIncompProp(ListIncompProp listIncompProp) {
        this.listIncompProp = listIncompProp;
    }

    public AgentValueToWeight getAgentValueToWeight() {
        return agentValueToWeight;
    }

    public void setAgentValueToWeight(AgentValueToWeight agentValueToWeight) {
        this.agentValueToWeight = agentValueToWeight;
    }

    public AgentValuePropWeight getAgentValuePropWeight() {
        return agentValuePropWeight;
    }

    public void setAgentValuePropWeight(AgentValuePropWeight agentValuePropWeight) {
        this.agentValuePropWeight = agentValuePropWeight;
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public ListRules getListRules() {
        return listRules;
    }

    public void removeRule(Rule rule) {
        this.listRules.removeRule(rule);
    }

    public void setListRules(ListRules listRules) {
        this.listRules = listRules;
    }

    public ListPropBaseClean getListPropBaseClean() {
        return listPropBaseClean;
    }

    public void setListPropBaseClean(ListPropBaseClean listPropBaseClean) {
        this.listPropBaseClean = listPropBaseClean;
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
