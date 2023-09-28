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
    private List<Rule> listRules = new ArrayList<>();
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

    public boolean addRule(Rule newRule) {
        if (!isRuleAlreadyPresent(newRule)) {
            listRules.add(newRule);
            notifyObservers();
            return true;
        } else {
            return false;
        }
    }

    private boolean isRuleAlreadyPresent(Rule newRule) {
        for (Rule existingRule : listRules) {
            if (existingRule.getPremises().equals(newRule.getPremises())
                    && existingRule.getConclusion().equals(newRule.getConclusion())) {
                return true;
            }
        }
        return false;
    }

    public ListIncompProp getListIncompProp() {
        return listIncompProp;
    }

    public void removeRule(Rule rule) {
        this.listRules.remove(rule);
    }

    private void removeRulesByProp(Proposition removedProposition) {
        listRules.removeIf(rule -> rule.getPremises().contains(removedProposition) || rule.getConclusion().equals(removedProposition));
        notifyObservers();
    }

    @Override
    public void updateProposition(ListProposition listProposition) {
        List<Proposition> localPropositionsCopy = this.propositions;

        for (Proposition prop : listProposition.getListProposition()) {
            if (!this.propositions.contains(prop)) {
                this.propositions.add(prop);
            }
        }

        for (Proposition prop : localPropositionsCopy) {
            if (!listProposition.getListProposition().contains(prop)) {
                this.propositions.remove(prop);
                removeRulesByProp(prop);
            }
        }
    }

    public boolean containsSamePremises(Rule rule) {
        return listRules.stream().anyMatch(r -> r.getPremises().equals(rule.getPremises()));
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
