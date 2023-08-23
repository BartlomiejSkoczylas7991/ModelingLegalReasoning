package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.IncompPropObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.RCObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.KBObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.RCObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase.KnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase.ListKnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.IncompProp;

import java.util.*;
import java.util.stream.Collectors;

public class ListReasoningChain implements KBObserver, RCObservable, IncompPropObserver {
    private List<Agent> agents;
    private HashMap<Agent, ReasoningChain> listReasoningChain;
    private ListKnowledgeBase listKnowledgeBase;
    private final ReasoningChainStrategyFactory factory = new ReasoningChainStrategyFactory();
    private List<Pair<Proposition, Proposition>> incompProp; // two inconsistent propositions;
    private IncompProp decisions;
    private List<RCObserver> observers = new ArrayList<>();

    public ListReasoningChain() {
        this.listReasoningChain = new HashMap<>();
        this.incompProp = new ArrayList<>();
    }

    private ReasoningChain calculateWithVoting(Agent agent) {
        // Get the subjective knowledge base for a given agent
        KnowledgeBase subjectiveKB = listKnowledgeBase.getKnowledgeBase(agent);
        Set<Rule> setRules = subjectiveKB.getRj();

        int howManyDecisionsPBCContains = 0;
        for (Proposition prop : subjectiveKB.getPi()) {
            if (prop.isDecision()) {
                howManyDecisionsPBCContains++;
            }
        }

        ReasoningChainStrategy strategy = factory.createStrategy(subjectiveKB.getPi());
        return strategy.calculate(subjectiveKB);
    }

    public HashMap<Agent, ReasoningChain> getListReasoningChain() {
        return listReasoningChain;
    }

    private void calculateReasoningChain() {
        if(!agents.isEmpty()) {
            for (Agent agent : agents) {
                listReasoningChain.put(agent, calculateWithVoting(agent));
            }
            notifyObservers();
        }
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public List<Pair<Proposition, Proposition>> getIncompProp() {
        return incompProp;
    }

    public void setIncompProp(List<Pair<Proposition, Proposition>> incompProp) {
        this.incompProp = incompProp;
    }

    public IncompProp getDecisions() {
        return decisions;
    }

    public void setDecisions(IncompProp decisions) {
        this.decisions = decisions;
    }

    public List<RCObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<RCObserver> observers) {
        this.observers = observers;
    }

    @Override
    public void updateKB(ListKnowledgeBase knowledgeBase) {
        this.listKnowledgeBase = knowledgeBase;
        this.agents = knowledgeBase.getAgents();
        if (this.decisions != null) {
            calculateReasoningChain();
        }
    }

    @Override
    public void updateIncomp(ListIncompProp listIncompProp) {
        if (listIncompProp.decisionsExist()) {
            this.decisions = new IncompProp(listIncompProp.getDecisions(), true);
        }
        this.incompProp = listIncompProp.getIncompatiblePropositions_asPair();
        if (this.decisions != null && this.listKnowledgeBase != null && this.agents != null) {
            calculateReasoningChain();
        }
    }

    @Override
    public void addObserver(RCObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(RCObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(RCObserver observer : observers){
            observer.updateRC(this);
        }
    }

    public ReasoningChain getReasoningChainByAgent(Agent agent) {
        return listReasoningChain.get(agent);
    }

    @Override
    public String toString() {
        return listReasoningChain.entrySet().stream()
                .map(entry -> entry.getKey().getName() + " = " + entry.getValue().toString())
                .collect(Collectors.joining("\n"));
    }
}
