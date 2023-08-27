package com.bskoczylas.modelinglegalreasoning.domain.models;

import com.bskoczylas.modelinglegalreasoning.domain.models.DTO.ProjectData;
import com.bskoczylas.modelinglegalreasoning.domain.models.DTO.ProjectRepository;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.consortium.ListConsortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.court.CourtOpinion;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.court.Report;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.IncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase.ListKnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.propBaseClean.ListPropBaseClean;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.ListValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.decision.DecisionVoting;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;
import com.bskoczylas.modelinglegalreasoning.domain.models.projectObserver.ProjectObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.projectObserver.ProjectObserver;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


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
    private Scale scale = new Scale(0, 15);
    private ListRules listRules = new ListRules();
    private ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
    private ListKnowledgeBase listKnowledgeBase = new ListKnowledgeBase();
    private ListReasoningChain listReasoningChain = new ListReasoningChain();
    private DecisionVoting decision = new DecisionVoting();
    private ListConsortium listConsortium = new ListConsortium();
    private CourtOpinion courtOpinion = new CourtOpinion();
    private Report report = new Report();
    private List<ProjectObserver> observers = new ArrayList<>();

    public Project() {
        String defaultName = "Project" + nextId;
        this.name = defaultName;
        this.id = Integer.toString(nextId++);  // set id

        // define
        configureObservers();
    }

    public Project(String name) {
        if (name == null || name.trim().isEmpty()) {
            name = "Project" + nextId;
        }
        // for new project
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
        this.listIncompProp.addObserver(this.listProposition);
        this.scale.addObserver(this.agentValuePropWeight);
        this.scale.addObserver(this.agentValueToWeight);
        this.agentValuePropWeight.addObserver(this.listPropBaseClean);
        this.agentValueToWeight.addAVObserver(this.listPropBaseClean);
        this.agentValueToWeight.setScale(this.scale);
        this.agentValuePropWeight.setScale(this.scale);
        this.listPropBaseClean.addObserver(this.listKnowledgeBase);
        this.listIncompProp.addObserver(this.report);
        this.listRules.addObserver(this.listKnowledgeBase);
        this.listKnowledgeBase.addObserver(this.listReasoningChain);
        this.listIncompProp.addObserver(this.listReasoningChain);
        this.listIncompProp.addObserver(this.decision);
        this.listIncompProp.addObserver(this.listRules);
        this.listReasoningChain.addObserver(this.decision);
        this.decision.addObserver(this.listConsortium);
        this.listConsortium.addObserver(this.courtOpinion);
        this.courtOpinion.addObserver(this.report);
        this.listAgent.addAgentObserver(this.report);
        this.listRules.addObserver(this.report);
        this.listValue.addObserver(this.report);
        this.listPropBaseClean.addObserver(this.report);
    }

    @Override
    public void addProjectObserver(ProjectObserver observer) {
        if (!observers.contains(observer)) {
            observers.add(observer);
        }
    }

    @Override
    public void removeProjectObserver(ProjectObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyProjectObservers(Project project) {

    }

    // For ProjectController
    public boolean hasEnoughData() {
        boolean hasEnoughAgents = this.getListAgent().getAgents().size() >= 2;
        boolean hasEnoughValues = this.getListValue().getValues().size() >= 2;
        boolean hasEnoughPropositions = this.getListProposition().getListProposition().size() >= 4;
        boolean hasEnoughDecisions = this.getListIncompProp().decisionsExist();
        boolean hasEnoughRules = this.getListRules().getListRules().size() >= 2;

        return hasEnoughAgents && hasEnoughValues && hasEnoughPropositions && hasEnoughDecisions && hasEnoughRules;
    }

    public List<Report.ReportSection> generateReport() {
        if (!hasEnoughData()) {
            throw new IllegalStateException("Not enough data to generate report");
        }
        return this.report.generateReport();
    }

    public void setData(ProjectData projectData) {
        // Dla agentów
        List<Agent> copiedAgents = projectData.getAgents().stream()
                .map(Agent::new)
                .collect(Collectors.toList());
        this.listAgent.setListAgent(copiedAgents);

        // Dla wartości
        List<Value> copiedValues = projectData.getValues().stream()
                .map(Value::new)
                .collect(Collectors.toList());
        this.listValue.setListValue(copiedValues);

        // Dla propozycji
        List<Proposition> copiedPropositions = projectData.getPropositions().stream()
                .map(Proposition::new)
                .collect(Collectors.toList());
        this.listProposition.setListProposition(copiedPropositions);

        // Dla wagi agenta i wartości
        Map<AgentValue, Weight> copiedAgentValueToWeight = projectData.getAgentValueToWeight().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> new AgentValue(entry.getKey()),
                        entry -> new Weight(entry.getValue())
                ));
        this.agentValueToWeight.setAgentValueWeights(copiedAgentValueToWeight);

        // Dla wagi agenta, wartości i propozycji
        Map<AgentValueProposition, Weight> copiedAgentValuePropWeight = projectData.getAgentValuePropWeight().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> new AgentValueProposition(entry.getKey()),
                        entry -> new Weight(entry.getValue())
                ));
        this.agentValuePropWeight.setAgentValuePropWeights(copiedAgentValuePropWeight);

        // Zakładając, że metoda getScale zwraca kopię obiektu, inaczej możemy potrzebować głębokiej kopii
        this.scale.setScale(projectData.getScale().getMin(), projectData.getScale().getMax());

        // Dla niekompatybilnych propozycji
        List<IncompProp> copiedIncompProps = projectData.getIncompPropList().stream()
                .map(IncompProp::new)
                .collect(Collectors.toList());
        this.listIncompProp.setIncompPropList(copiedIncompProps);

        // Zakładam, że obiekt Rule ma konstruktor tworzący głęboką kopię
        List<Rule> copiedRules = projectData.getListRule().stream()
                .map(Rule::new)
                .collect(Collectors.toList());
        this.listRules.setListRules(copiedRules);
    }


    public ProjectData getData() {
        ProjectData projectData = new ProjectData();

        List<Agent> copiedAgents = listAgent.getAgents().stream()
                .map(Agent::new)
                .collect(Collectors.toList());
        projectData.setAgents(copiedAgents);

        List<Value> copiedValues = listValue.getValues().stream()
                .map(Value::new)
                .collect(Collectors.toList());
        projectData.setValues(copiedValues);

        List<Proposition> copiedPropositions = listProposition.getListProposition().stream()
                .map(Proposition::new)
                .collect(Collectors.toList());
        projectData.setPropositions(copiedPropositions);

        Map<AgentValue, Weight> copiedAgentValueToWeight = agentValueToWeight.getAgentValueWeights().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> new AgentValue(entry.getKey()),
                        entry -> new Weight(entry.getValue())
                ));
        projectData.setAgentValueToWeight(copiedAgentValueToWeight);

        Map<AgentValueProposition, Weight> copiedAgentValuePropWeight = agentValuePropWeight.getSerializableCopyOfMap().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> new AgentValueProposition(entry.getKey()),
                        entry -> new Weight(entry.getValue())
                ));
        projectData.setAgentValuePropWeight(copiedAgentValuePropWeight);

        projectData.setScale(agentValueToWeight.getScale());

        List<IncompProp> copiedIncompProps = listIncompProp.getIncompatiblePropositions().stream()
                .map(IncompProp::new)
                .collect(Collectors.toList());
        projectData.setIncompPropList(copiedIncompProps);

        List<Rule> copiedRules = listRules.getListRules().stream()
                .map(Rule::new)
                .collect(Collectors.toList());
        projectData.setListRule(copiedRules);
        projectData.setListRule(listRules.getListRules());

        return projectData;
    }

    public void save(String path) {
        ProjectRepository repository = new ProjectRepository();
        repository.save(getData(), path);
    }

    public ProjectData load(String path) {
        ProjectRepository repository = new ProjectRepository();
        return repository.load(path);
    }

    public String getId() {
        return id;
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

    public void setListReasoningChain(ListReasoningChain listReasoningChain) {
        this.listReasoningChain = listReasoningChain;
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

    public void addListRules(ListRules listRules) {
        this.listRules = listRules;
        this.listRules.notifyObservers();
    }

    public ListPropBaseClean getListPropBaseClean() {
        return listPropBaseClean;
    }

    public void setListPropBaseClean(ListPropBaseClean listPropBaseClean) {
        this.listPropBaseClean = listPropBaseClean;
    }

    public DecisionVoting getDecision() {
        return decision;
    }

    public void setDecision(DecisionVoting decision) {
        this.decision = decision;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }
}
