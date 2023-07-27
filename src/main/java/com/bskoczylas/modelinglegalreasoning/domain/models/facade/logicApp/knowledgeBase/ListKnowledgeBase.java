package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.propBaseClean.ListPropBaseClean;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.KBObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PBCObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.RuleObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.KBObservables;

import java.util.*;

public class ListKnowledgeBase implements PBCObserver, RuleObserver, KBObservables {
    private List<Agent> agents = new ArrayList<>();
    private ListPropBaseClean propBaseClean = new ListPropBaseClean();
    private ListRules rules = new ListRules();
    private Map<Agent, KnowledgeBase> listKnowledgeBase = new HashMap<Agent, KnowledgeBase>();
    private List<KBObserver> observers = new ArrayList<>();
    private boolean pbcUpdated = false;
    private boolean rulesUpdated = false;

    public ListKnowledgeBase(){}

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
        notifyObservers();
    }

    // it works
    @Override
    public void updatePBC(ListPropBaseClean propBaseClean) {
        this.setAgents(propBaseClean.getAgents());
        this.propBaseClean.setListPropBaseClean(propBaseClean.getListPropBaseClean());
        calculateKnowledgeBase();
    }

    @Override
    public void updateRule(ListRules newRules) {
        // Dodaj nowe zasady
        for (Rule rule : newRules.getListRules()) {
            if (!this.rules.getListRules().contains(rule)) {
                this.rules.getListRules().add(rule);
            }
        }

        // Usuń stare zasady
        this.rules.getListRules().removeIf(rule -> !newRules.getListRules().contains(rule));

        // Powiadom obserwatorów
        notifyObservers();
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public ListPropBaseClean getPropBaseClean() {
        return this.propBaseClean;
    }

    public KnowledgeBase getKnowledgeBase(Agent agent) {
        return listKnowledgeBase.getOrDefault(agent, new KnowledgeBase());
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
            // Przechodzenie przez klucze w mapie
            for (Agent agent : listKnowledgeBase.keySet()) {
                // Tutaj 'agent' będzie kolejnym kluczem w mapie
                // Możesz użyć 'agent' do otrzymania odpowiadającej mu wartości KnowledgeBase

                KnowledgeBase knowledgeBase = listKnowledgeBase.get(agent);

                // Teraz 'knowledgeBase' zawiera wartość KnowledgeBase dla danego 'agent'
                // Możesz wykonywać na nim operacje, np. wypisywanie informacji, przetwarzanie itp.
                // Przykładowo:
            }
        }
    }

    @Override
    public String toString() {
        return "ListKnowledgeBase{" +
                "agents=" + agents +
                ", propBaseClean=" + propBaseClean +
                ", rules=" + rules +
                ", listKnowledgeBase=" + listKnowledgeBase +
                ", observers=" + observers +
                '}';
    }
}