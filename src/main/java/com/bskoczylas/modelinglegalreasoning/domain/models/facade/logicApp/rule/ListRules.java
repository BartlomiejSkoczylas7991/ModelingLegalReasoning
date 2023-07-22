package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.IncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.RuleObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.IncompPropObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PropositionObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.RuleObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ListRules implements PropositionObserver, RuleObservable, IncompPropObserver {
    private List<Proposition> propositions = new ArrayList<>();
    private List<Rule> listRules = new LinkedList<>();
    private List<RuleObserver> observers = new ArrayList<>();
    private ListIncompProp listIncompProp = new ListIncompProp();

    public ListRules(List<Rule> listRules) {
        this.listRules = listRules;
    }

    public ListRules(){}

    public List<Rule> getListRules() {
        return listRules;
    }

    public void setListRules(List<Rule> listRules) {
        this.listRules = listRules;
        notifyObservers();
    }

    public List<Proposition> getPropositions() {
        return propositions;
    }

    public void setPropositions(List<Proposition> propositions) {
        this.propositions = propositions;
    }

    public List<RuleObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<RuleObserver> observers) {
        this.observers = observers;
    }

    public void setListIncompProp(ListIncompProp listIncompProp) {
        this.listIncompProp = listIncompProp;
    }

    public void addRule(Set<Proposition> premises, Proposition conclusion, String label) {
        // Check that none of the proposition pairs from premises are in incompProp
        List<IncompProp> incompProp = listIncompProp.getIncompatiblePropositions();
        for (Proposition premise1 : premises) {
            for (Proposition premise2 : premises) {
                Pair<Proposition, Proposition> pair1 = new Pair<>(premise1, premise2);
                Pair<Proposition, Proposition> pair2 = new Pair<>(premise2, premise1);
                if (incompProp.stream().anyMatch(ip -> ip.getPropositionsPair().equals(pair1) || ip.getPropositionsPair().equals(pair2))) {
                    throw new IllegalArgumentException("You cannot add a rule that contains incompatible propositions in premises.");
                }
            }
        }

        // Check if the conclusion is one of the decisions
        Pair<Proposition, Proposition> decisions = listIncompProp.getDecisions();
        if (!conclusion.equals(decisions.getFirst()) && !conclusion.equals(decisions.getSecond())) {
            throw new IllegalArgumentException("The conclusion must be one of the decisions.");
        }

        // We add a new rule only if no exception occurred
        Rule newRule = new Rule(premises, conclusion, label);
        listRules.add(newRule);
        notifyObservers();
    }

    public ListIncompProp getListIncompProp() {
        return listIncompProp;
    }

    public void removeRule(Rule rule) {
        this.listRules.remove(rule);
    }

    private void updateRules(Proposition removedProposition) {
        // Removing the policy that contains the removed proposal
        listRules.removeIf(rule -> rule.getPremises().contains(removedProposition) || rule.getConclusion().equals(removedProposition));
        notifyObservers();
    }

    @Override
    public void updateProposition(ListProposition listProposition) {
        // Create a copy of the local propositions list to avoid ConcurrentModificationException
        List<Proposition> localPropositionsCopy = new ArrayList<>(this.propositions);

        for (Proposition prop : listProposition.getListProposition()) {
            if (!this.propositions.contains(prop)) {
                this.propositions.add(prop);
            }
        }

        for (Proposition prop : localPropositionsCopy) {
            if (!listProposition.getListProposition().contains(prop)) {
                this.propositions.remove(prop);
                updateRules(prop);
            }
        }
    }

    @Override
    public void addObserver(RuleObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(RuleObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (RuleObserver observer : observers) {
            observer.updateRule(this);
        }
    }

    @Override
    public void updateIncomp(ListIncompProp listIncompProp) {
        this.listIncompProp = listIncompProp;
    }

    @Override
    public String toString() {
        return "Rules: " + listRules.stream()
                .map(Rule::toString)
                .collect(Collectors.joining("; "));
    }
}
