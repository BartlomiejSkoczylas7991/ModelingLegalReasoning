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
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.ListValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AVP.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Decision.Decision;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.ListProposition;

import java.time.LocalDateTime;

public class Project {
    private static int nextId = 0;
    private String id;
    private String name;
    private ListAgent listAgent; //lista agentów
    private ListProposition listProposition; // lista propozycji
    private ListValue listValue; // lista wartości
    private ListIncompProp listIncompProp;
    private AgentValueToWeight agentValueToWeight; // lista wag AV
    private AgentValuePropWeight agentValuePropWeight; // lista wag AVP
    private Scale scale; // skala wag
    private ListRules listRules;
    private ListPropBaseClean listPropBaseClean;
    private ListKnowledgeBase listKnowledgeBase;
    private ListReasoningChain listReasoningChain;
    private Decision decision;
    private ListConsortium listConsortium;
    private CourtOpinion courtOpinion;
    private Report report;

    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;

    public Project(String name){
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
        this.listAgent = new ListAgent();
        this.listProposition = new ListProposition();
        this.listValue = new ListValue();
        this.listRules = new ListRules();
        this.agentValuePropWeight = new AgentValuePropWeight();
        this.agentValueToWeight = new AgentValueToWeight();
        this.listIncompProp = new ListIncompProp();
        this.listKnowledgeBase = new ListKnowledgeBase();
        this.listPropBaseClean = new ListPropBaseClean();
        this.scale = new Scale();
        this.listReasoningChain = new ListReasoningChain();
        this.listConsortium = new ListConsortium();
        this.courtOpinion = new CourtOpinion();
        this.report = new Report();

        configureObservers();

    }

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

    public String getId() {
        return id;
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

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        Project.nextId = nextId;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setListRules(ListRules listRules) {
        this.listRules = listRules;
    }

    public ListPropBaseClean getListPropBaseClean() {
        return listPropBaseClean;
    }

    public void setListPropBaseClean(ListPropBaseClean listPropBaseClean) {
        this.listPropBaseClean = listPropBaseClean;
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

    public CourtOpinion getCourt() {
        return courtOpinion;
    }

    public void setCourt(CourtOpinion court) {
        this.courtOpinion = court;
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public ListConsortium getConsortium() {
        return this.listConsortium;
    }

    public void setConsortium(ListConsortium listConsortium) {
        this.listConsortium = listConsortium;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public void setLastModifiedDate(LocalDateTime lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
}
