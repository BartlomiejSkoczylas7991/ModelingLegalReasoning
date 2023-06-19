package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.observables.KB_Observables;
import com.bskoczylas.modelinglegalreasoning.models.observers.KB_Observer;
import com.bskoczylas.modelinglegalreasoning.models.observers.PBC_Observer;
import com.bskoczylas.modelinglegalreasoning.models.observers.RuleObserver;
import java.util.*;

public class ListKnowledgeBase implements PBC_Observer, RuleObserver, KB_Observables {
    private Set<Agent> agents= new HashSet<>();
    private ListPropBaseClean propBaseClean;
    private List<Rule> rules;
    private HashMap<Agent, KnowledgeBase> listKnowledgeBase;
    private List<KB_Observer> observers;

    public ListKnowledgeBase(ListPropBaseClean propBaseClean, List<Rule> rules) {
        this.propBaseClean = propBaseClean;
        this.rules = rules;
    }

    public List<Rule> getRules() {
        return this.rules;
    }

    private KnowledgeBase calculate(Agent agent) {
        Set<Proposition> pi = propBaseClean.getAgentPropBaseClean(agent);
        Set<Rule> rj = new HashSet<>();
        for (Rule rule : getRules()) {
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
        this.rules = listRules.getListRules();
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

    public void setPropBaseClean(ListPropBaseClean propBaseClean) {
        this.propBaseClean = propBaseClean;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public void setListKnowledgeBase(HashMap<Agent, KnowledgeBase> listKnowledgeBase) {
        this.listKnowledgeBase = listKnowledgeBase;
    }

    public KnowledgeBase getKnowledgeBase(Agent agent) {
        return listKnowledgeBase.getOrDefault(agent, new KnowledgeBase());
    }

    @Override
    public void addObserver(KB_Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(KB_Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (KB_Observer observer : observers){
            observer.updateKB(this);
        }
    }



    public HashMap<Agent, KnowledgeBase> getListKnowledgeBase() {
        return listKnowledgeBase;
    }
}