package com.bskoczylas.modelinglegalreasoning.domain.models;

import com.bskoczylas.modelinglegalreasoning.adapters.ProjectData;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.consortium.ListConsortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.court.CourtOpinion;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.court.Report;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase.ListKnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.propBaseClean.ListPropBaseClean;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.ListValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.decision.Decision;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.projectObserver.ProjectObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.projectObserver.ProjectObserver;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Project implements ProjectObservable {
    private static int nextId = 0;
    private String id;
    private String name;
    private ListAgent listAgent = new ListAgent();
    private ListProposition listProposition = new ListProposition();
    private ListValue listValue = new ListValue();
    private ListIncompProp listIncompProp = new ListIncompProp();
    private AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
    private AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
    private Scale scale = new Scale(0,10);
    private ListRules listRules = new ListRules();
    private ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
    private ListKnowledgeBase listKnowledgeBase = new ListKnowledgeBase();
    private ListReasoningChain listReasoningChain = new ListReasoningChain();
    private Decision decision = new Decision();
    private ListConsortium listConsortium = new ListConsortium();
    private CourtOpinion courtOpinion = new CourtOpinion();
    private Report report = new Report();
    private List<ProjectObserver> observers = new ArrayList<>();
    private LocalDateTime createdDate = LocalDateTime.now();
    private LocalDateTime lastModifiedDate = LocalDateTime.now();

    public Project(ProjectData projectData) {
        if (projectData.getName() == null || projectData.getName().trim().isEmpty()) {
            name = "Project" + nextId;
        } else {
            name = projectData.getName();
        }

        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
        this.id = Integer.toString(nextId++);  // set id

        configureObservers();

        this.listAgent.setListAgent(projectData.getAgents());
        this.listProposition.setListProposition(projectData.getPropositions());
        this.listValue.setListValue(projectData.getValues());
        this.listIncompProp.setIncompPropList(projectData.getIncompProps());
        this.listIncompProp.setDecisions(projectData.getDecisions());
        this.listRules.setListRules(projectData.getRules());
        this.scale.setScale(projectData.getScale().getMin(), projectData.getScale().getMax());
        this.agentValueToWeight.setAgentValueWeights(projectData.getAgentValueWeightHashMap());
        this.agentValuePropWeight.setAgentValuePropWeights(projectData.getAgentValuePropositionWeightHashMap());
    }

    public Project() {
        String defaultName = "Project" + nextId;
        this.createdDate = LocalDateTime.now();
        this.lastModifiedDate = LocalDateTime.now();
        this.name = defaultName;
        this.id = Integer.toString(nextId++);  // set id

        // define
        configureObservers();
    }

    public Project(String name){
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

    public void fromProjectData(ProjectData projectData) {
        this.name = projectData.getName();
        this.listAgent.setListAgent(projectData.getAgents());
        this.listProposition.setListProposition(projectData.getPropositions());
        this.listValue.setListValue(projectData.getValues());
        this.listIncompProp.setIncompPropList(projectData.getIncompProps());
        this.listIncompProp.setDecisions(projectData.getDecisions());
        this.listRules.setListRules(projectData.getRules());
        this.scale.setScale(projectData.getScale().getMin(), projectData.getScale().getMax());
        this.agentValueToWeight.setAgentValueWeights(projectData.getAgentValueWeightHashMap());
        this.agentValuePropWeight.setAgentValuePropWeights(projectData.getAgentValuePropositionWeightHashMap());
    }

    public ProjectData toProjectData() {
        ProjectData projectData = new ProjectData();
        projectData.setName(this.name);
        projectData.setAgents(this.listAgent.getAgents());
        projectData.setValues(this.listValue.getValues());
        projectData.setPropositions(this.listProposition.getListProposition());
        projectData.setScale(this.scale);
        projectData.setIncompProps(this.listIncompProp.getIncompatiblePropositions());
        projectData.setAgentValueWeightHashMap(this.agentValueToWeight.getAgentValueWeights());
        projectData.setAgentValuePropositionWeightHashMap(this.agentValuePropWeight.getAgentValuePropWeights());
        projectData.setRules(this.listRules.getListRules());
        // ustawienie reszty pól...
        return projectData;
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
