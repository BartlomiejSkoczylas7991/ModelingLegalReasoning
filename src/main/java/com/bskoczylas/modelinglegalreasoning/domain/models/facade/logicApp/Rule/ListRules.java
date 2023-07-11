package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule;

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
    private List<Proposition> propositions;
    private List<Rule> listRules = new LinkedList<>();
    private List<RuleObserver> observers;
    private ListIncompProp listIncompProp;

    public ListRules(List<Rule> listRules) {
        this.listRules = listRules;
    }

    public ListRules(){this.observers = new ArrayList<>();}

    public List<Rule> getListRules() {
        return listRules;
    }

    public void setListRules(List<Rule> listRules) {
        this.listRules = listRules;
    }



    public void addRule(Set<Proposition> premises, Proposition conclusion, String label) {
        // Sprawdzamy, czy wszystkie propozycje w regule są już na liście propozycji
        try {
            Rule newRule = new Rule(premises, conclusion, label);
            // Dodajemy nową regułę tylko jeśli nie wystąpił wyjątek
            listRules.add(newRule);
            notifyObservers();
        } catch (IllegalArgumentException e) {
            // Obsługa sytuacji, gdy konkluzja nie jest decyzją
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
