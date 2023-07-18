package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.KnowledgeBase;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.PropBaseClean.ListPropBaseClean;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.KBObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PBCObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.RuleObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule.Rule;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.KBObservables;

import java.util.*;

public class ListKnowledgeBase implements PBCObserver, RuleObserver, KBObservables {
    private Set<Agent> agents = new HashSet<>();
    private ListPropBaseClean propBaseClean;
    private ListRules rules;
    private HashMap<Agent, KnowledgeBase> listKnowledgeBase;
    private List<KBObserver> observers;

    public ListKnowledgeBase(ListPropBaseClean propBaseClean, ListRules rules) {
        this.propBaseClean = propBaseClean;
        this.rules = rules;
        this.observers = new ArrayList<>();
    }

    public ListKnowledgeBase(){this.observers = new ArrayList<>();}

    public ListRules getRules() {
        return this.rules;
    }

    private KnowledgeBase calculate(Agent agent) {
        Set<Proposition> pi = propBaseClean.getAgentPropBaseClean(agent);
        Set<Rule> rj = new HashSet<>();
        for (Rule rule : rules.getListRules()) {
            if (pi != null && pi.containsAll(rule.getPremises())) {
                rj.add(rule);
            }
        }
        return new KnowledgeBase(pi, rj);
    }

    private void calculateKnowledgeBase() {
        for (Agent agent : agents) {
            listKnowledgeBase.put(agent, calculate(agent));
        }
    }

    @Override
    public void updatePBC(ListPropBaseClean propBaseClean) {
        this.agents = propBaseClean.getAgents();
        this.propBaseClean = propBaseClean;
        calculateKnowledgeBase();
    }

    @Override
    public void updateRule(ListRules listRules) {
        this.rules = listRules;
        calculateKnowledgeBase();
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public void setAgents(Set<Agent> agents) {
        this.agents = agents;
    }

    public ListPropBaseClean getPropBaseClean() {
        return this.propBaseClean;
    }

    public void setListKnowledgeBase(HashMap<Agent, KnowledgeBase> listKnowledgeBase) {
        this.listKnowledgeBase = listKnowledgeBase;
    }

    public KnowledgeBase getKnowledgeBase(Agent agent) {
        return listKnowledgeBase.getOrDefault(agent, new KnowledgeBase());
    }

    public void setPropBaseClean(ListPropBaseClean propBaseClean) {
        this.propBaseClean = propBaseClean;
    }

    public void setRules(ListRules rules) {
        this.rules = rules;
    }

    public List<KBObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<KBObserver> observers) {
        this.observers = observers;
    }

    @Override
    public void addObserver(KBObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(KBObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (KBObserver observer : observers){
            observer.updateKB(this);
        }
    }

    public HashMap<Agent, KnowledgeBase> getListKnowledgeBase() {
        return listKnowledgeBase;
    }
}