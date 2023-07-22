package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.PropositionObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.IncompPropObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PropositionObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ListProposition implements PropositionObservable, IncompPropObserver {
    private List<Proposition> listProposition = new ArrayList<>();
    private final List<PropositionObserver> observers = new ArrayList<>();

    public ListProposition() {}

    public ListProposition(List<Proposition> listProposition) {
        this.listProposition = listProposition;
    }

    public void addProposition(Proposition proposition) {
        listProposition.add(proposition);
        notifyObservers(this);
    }

    public void removePropositions(List<Proposition> propositions) {
        listProposition.removeAll(propositions);
        notifyObservers(this);
    }

    public boolean removeProposition(Proposition proposition) {
        boolean result = listProposition.remove(proposition);
        notifyObservers(this);
        return result;
    }

    public List<Proposition> getListProposition() {
        return listProposition;
    }

    public List<Proposition> getPropositionsNotDecisions() {
        return listProposition.stream()
                .filter(proposition -> !proposition.isDecision())
                .collect(Collectors.toList());
    }

    public void setListProposition(List<Proposition> listProposition) {
        this.listProposition = listProposition;
        notifyObservers(this);
    }

    public List<PropositionObserver> getObservers() {
        return observers;
    }

    @Override
    public void addObserver(PropositionObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(PropositionObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers(ListProposition listProposition) {
        for (PropositionObserver observer : this.observers) {
            observer.updateProposition(listProposition);
        }
    }

    @Override
    public void updateIncomp(ListIncompProp listIncompProp) {
        Pair<Proposition, Proposition> decisions = listIncompProp.getDecisions();

        // if a pair of decisions has been added, then we change the given pair of proposals to "true" in the proposal pool
        if (decisions != null) {
            for (Proposition prop : listProposition) {
                if (prop.equals(decisions.getFirst()) || prop.equals(decisions.getSecond())) {
                    prop.setDecision(true);
                }
                notifyObservers(this);
            }
        } else {  // if the decisions are removed, we unset the decisions in the proposal pool
            for (Proposition prop : listProposition) {
                if (prop.isDecision()) {
                    prop.setDecision(false);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Propositions = {");

        for(int i=0; i<listProposition.size(); i++){
            sb.append(listProposition.get(i).getStatement());

            if (listProposition.get(i).isDecision()) {
                sb.append(" (decision)");
            }

            if(i < listProposition.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("}");
        return sb.toString();
    }
}

