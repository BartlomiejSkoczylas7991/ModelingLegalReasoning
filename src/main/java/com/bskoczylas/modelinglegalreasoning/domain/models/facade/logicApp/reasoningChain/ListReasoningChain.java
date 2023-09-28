package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.IncompPropObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PropositionObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.ListProposition;
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
import com.bskoczylas.modelinglegalreasoning.domain.models.projectObserver.ProjectObserver;

import java.util.*;
import java.util.stream.Collectors;

public class ListReasoningChain implements KBObserver, RCObservable, IncompPropObserver, PropositionObserver {
    private List<Agent> agents;
    private HashMap<Agent, ReasoningChain> listReasoningChain;
    private ListKnowledgeBase listKnowledgeBase;
    private List<Pair<Proposition, Proposition>> incompProp; // two inconsistent propositions;
    private IncompProp decisions;
    private List<RCObserver> observers = new ArrayList<>();
    private Map<Agent, Set<Rule>> allowedRules = new HashMap<>();
    private List<Proposition> propositions;


    public ListReasoningChain() {
        this.agents = new ArrayList<>();
        this.listReasoningChain = new HashMap<>();
        this.incompProp = new ArrayList<>();
    }

    public HashMap<Agent, ReasoningChain> getListReasoningChain() {
        return listReasoningChain;
    }

    // Check if the rule is valid for the given agent's reasoning chain.
    private boolean isValidRuleForAgent(Agent agent, Rule rule) {
        ReasoningChain rc = listReasoningChain.get(agent);
        Set<Proposition> currentPropositions = listKnowledgeBase.getKnowledgeBase(agent).getPi();
        List<Rule> currentRules = listKnowledgeBase.getKnowledgeBase(agent).getRj();

        // 0. Jeśli już jest w reasoningChain
        if (listReasoningChain.get(agent).getKnowledgeBase().getRj().contains(rule)){
            return false;
        }

        // 1. Tworzenie zbioru propozycji, które są true lub już zostały wnioskowane
        Set<Proposition> confirmedPropositions = currentPropositions.stream()
                .filter(Proposition::isTrue)
                .collect(Collectors.toSet());
        confirmedPropositions.addAll(listReasoningChain.get(agent).getKnowledgeBase().getRj().stream()
                .map(Rule::getConclusion)
                .collect(Collectors.toSet()));

        if (!confirmedPropositions.containsAll(rule.getPremises())) {
            return false;
        }

        // 2. Reguła nie może powodować konfliktu z IncompProp
        for (Pair<Proposition, Proposition> incomp : incompProp) {

            // Sprawdź, czy jakakolwiek z zasad w currentRules zawiera incomp.getKey() jako przesłankę
            boolean currentRulesContainKey = listReasoningChain.get(agent).getKnowledgeBase().getRj().stream()
                    .anyMatch(r -> r.getPremises().contains(incomp.getKey()));

            if (rule.getPremises().contains(incomp.getKey()) && currentRulesContainKey) {
                return false;
            }

            // Sprawdź, czy jakakolwiek z zasad w currentRules zawiera incomp.getValue() jako przesłankę
            boolean currentRulesContainValue = listReasoningChain.get(agent).getKnowledgeBase().getRj().stream()
                    .anyMatch(r -> r.getPremises().contains(incomp.getValue()));

            if (rule.getConclusion().equals(incomp.getValue()) && currentRulesContainKey) {
                return false;
            }

            if (rule.getConclusion().equals(incomp.getKey()) && currentRulesContainValue) {
                return false;
            }
        }

        // 3. Konkluzja reguły nie może już istnieć w obecnym ReasoningChain
        for (Rule ruleVar : this.listReasoningChain.get(agent).getKnowledgeBase().getRj()) {
            if(ruleVar.getPremises().contains(rule.getConclusion()) || ruleVar.getConclusion().equals(rule.getConclusion()) ) {
                return false;
            }
        }

        // 4. Konkluzja reguły nie może być sprzeczna z obecnym ReasoningChain
        for (Pair<Proposition, Proposition> incomp : incompProp) {
            for (Rule existingRule : listReasoningChain.get(agent).getKnowledgeBase().getRj()) {
                // Sprawdzanie w stosunku do konkluzji reguł
                if (rule.getConclusion().equals(incomp.getKey()) && existingRule.getConclusion().equals(incomp.getValue())) {
                    return false;
                }
                if (rule.getConclusion().equals(incomp.getValue()) && existingRule.getConclusion().equals(incomp.getKey())) {
                    return false;
                }

                // Sprawdzanie w stosunku do przesłanek obecnych reguł
                for (Proposition premise : existingRule.getPremises()) {
                    if (rule.getConclusion().equals(incomp.getKey()) && premise.equals(incomp.getValue())) {
                        return false;
                    }
                    if (rule.getConclusion().equals(incomp.getValue()) && premise.equals(incomp.getKey())) {
                        return false;
                    }
                }
            }
        }

        // 5. Reguły, które mają za przesłanki konkluzje innych reguł w ReasoningChain
        Set<Proposition> conclusionsInRC = listReasoningChain.get(agent).getKnowledgeBase().getRj().stream()
                .map(Rule::getConclusion)
                .collect(Collectors.toSet());
        conclusionsInRC.addAll(listReasoningChain.get(agent).getKnowledgeBase().getRj().stream()
                .flatMap(ruleAdd -> ruleAdd.getPremises().stream())
                .collect(Collectors.toSet()));
        if (conclusionsInRC.contains(rule.getConclusion())) {
            return false;
        }
        System.out.println("Przechodzi");
        return true;
    }

    /**
     * Re-calculates the set of allowed rules for a given agent.
     * This method should be called after every change that might affect the allowed rules.
     */
    private void calculateAllowedRulesForAgent(Agent agent) {
        Set<Rule> agentAllowedRules = new HashSet<>();
        List<Rule> rulesForAgent = new ArrayList<>(listReasoningChain.get(agent).getKnowledgeBase().getRj());
        boolean isLast = false;
        if (!rulesForAgent.isEmpty()) {
            Rule lastRule = rulesForAgent.get(rulesForAgent.size() - 1);
            System.out.println("1");
            if (lastRule.getConclusion().isDecision()) {
                isLast = true;
                System.out.println("2");
                listReasoningChain.get(agent).setDecision(lastRule.getConclusion());
                listReasoningChain.get(agent).pruneRules(lastRule);
                allowedRules.put(agent, new HashSet<>());
                return;
            } else {
                System.out.println("3");
                listReasoningChain.get(agent).setDecision(null);
            }
        }
        if (!isLast) {
            System.out.println("4");
            for (Rule rule : listKnowledgeBase.getKnowledgeBase(agent).getRj()) {
                System.out.println("5");
                if (isValidRuleForAgent(agent, rule)) {
                    System.out.println("6");
                    agentAllowedRules.add(rule);
                }
            }

            allowedRules.put(agent, agentAllowedRules);
        }
    }

    /**
     * Re-calculates the set of allowed rules for all agents.
     */
    public void actualizeAllowedRules() {
        for (Agent agent : agents) {
            calculateAllowedRulesForAgent(agent);
        }
    }

    public void addRuleToReasoningChain(Agent agent, Rule rule) {
        if (allowedRules.get(agent).contains(rule)) {
            ReasoningChain rc = listReasoningChain.get(agent);
            rc.addRule(rule);
            calculateAllowedRulesForAgent(agent);
            notifyObservers();
        }
    }

    public void removeRuleFromReasoningChain(Agent agent, Rule rule) {
        listReasoningChain.get(agent).removeRuleAndAllAfter(rule);
        calculateAllowedRulesForAgent(agent);
        notifyObservers();
    }

    public List<Agent> getAgents() {
        return this.agents;
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
        List<Agent> newAgents = knowledgeBase.getAgents();

        Iterator<Map.Entry<Agent, ReasoningChain>> it = listReasoningChain.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Agent, ReasoningChain> entry = it.next();
            if (!newAgents.contains(entry.getKey())) {
                it.remove();
            }
        }

        for (Agent agent : newAgents) {
            if (!listReasoningChain.containsKey(agent)) {
                listReasoningChain.put(agent, new ReasoningChain());
            }
        }
        this.agents = newAgents;

        actualizeAllowedRules();
    }

    public void setListReasoningChain(HashMap<Agent, ReasoningChain> listReasoningChain) {
        this.listReasoningChain = listReasoningChain;
    }

    public Map<Agent, Set<Rule>> getAllowedRules() {
        return allowedRules;
    }

    public void setAllowedRules(Map<Agent, Set<Rule>> allowedRules) {
        this.allowedRules = allowedRules;
    }

    @Override
    public void updateProposition(ListProposition listProposition) {
        this.propositions = listProposition.getListProposition();
        if(this.agents != null) {
            actualizeAllowedRules();
        }
    }

    @Override
    public void updateIncomp(ListIncompProp listIncompProp) {
        if (listIncompProp.decisionsExist()) {
            this.decisions = new IncompProp(listIncompProp.getDecisions(), true);
        }
        this.incompProp = listIncompProp.getIncompatiblePropositions_asPair();
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
