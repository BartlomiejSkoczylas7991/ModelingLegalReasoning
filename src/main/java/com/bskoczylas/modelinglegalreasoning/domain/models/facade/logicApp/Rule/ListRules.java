package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.RuleObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.IncompPropObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PropositionObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.RuleObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
    }

    public void addRule(Set<Proposition> premises, Proposition conclusion, String label) {
        // We check if all the suggestions in the rule are already in the list of suggestions
        try {
            // Check that none of the proposition pairs from premises are in incompProp
            Set<Pair<Proposition, Proposition>> incompProp = listIncompProp.getIncompatiblePropositions();
            for (Proposition premise1 : premises) {
                for (Proposition premise2 : premises) {
                    Pair<Proposition, Proposition> pair1 = new Pair<>(premise1, premise2);
                    Pair<Proposition, Proposition> pair2 = new Pair<>(premise2, premise1);
                    if (incompProp.contains(pair1) || incompProp.contains(pair2)) {
                        throw new IllegalArgumentException("You cannot add a rule that contains inappropriate proposals in premises.");
                    }
                }
            }

            Rule newRule = new Rule(premises, conclusion, label);

            // Check if the conclusion is one of the decisions
            Pair<Proposition, Proposition> decisions = listIncompProp.getDecisions();
            if (!conclusion.equals(decisions.getFirst()) && !conclusion.equals(decisions.getSecond())) {
                throw new IllegalArgumentException("The conclusion must be one of the decisions.");
            }

            // We add a new rule only if no exception occurred
            listRules.add(newRule);
            notifyObservers();
        } catch (IllegalArgumentException e) {
            // Handling situations where the conclusion is not a decision or the premises contains inadequate proposals
            System.err.println(e.getMessage());
        }
    }

    private void updateRules(Proposition removedProposition) {
        // Usuwamy zasady, które zawierają usuniętą propozycję
        listRules.removeIf(rule -> rule.getPremises().contains(removedProposition) || rule.getConclusion().equals(removedProposition));
        notifyObservers();
    }

    @Override
    public void updateProposition(Proposition updatedProposition) {
        if (!this.propositions.contains(updatedProposition)) {
            this.propositions.add(updatedProposition);
        } else {
            this.propositions.remove(updatedProposition);
            updateRules(updatedProposition);
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
}
