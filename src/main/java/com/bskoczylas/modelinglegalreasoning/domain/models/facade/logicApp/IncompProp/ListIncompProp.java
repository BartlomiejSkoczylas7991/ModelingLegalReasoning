package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.IncompPropObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.IncompPropObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PropositionObserver;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListIncompProp implements PropositionObserver, IncompPropObservable {
    private List<Proposition> propositions;
    private List<IncompPropObserver> observers;
    private Set<Pair<Proposition, Proposition>> incompProp;
    private Pair<Proposition, Proposition> decisions;

    public ListIncompProp() {
        this.incompProp = new HashSet<>();
        this.observers = new ArrayList<>();
    }

    public void addIncompatiblePropositions(Proposition prop1, Proposition prop2) {
        // Ensure that the pair is not already present in the set
        // and that prop1 and prop2 are not the same proposition
        incompProp.add(new Pair<>(prop1, prop2));
        notifyObservers();
    }

    public void setDecisions(Pair<Proposition, Proposition> incompProp) {
        incompProp.getFirst().setDecision(true);
        incompProp.getSecond().setDecision(true);
        this.decisions = incompProp;
        notifyObservers();
    }

    public Pair<Proposition, Proposition> getDecisions() {
        return decisions;
    }

    boolean containsPair(Proposition prop1, Proposition prop2) {
        // Check if the pair is in the set in either order
        return incompProp.contains(new Pair<>(prop1, prop2)) || incompProp.contains(new Pair<>(prop2, prop1));
    }

    public Set<Pair<Proposition, Proposition>> getIncompatiblePropositions() {
        return incompProp;
    }

    public List<Proposition> getPropositions() {
        return propositions;
    }

    public void setPropositions(List<Proposition> propositions) {
        this.propositions = propositions;
    }

    public Set<Pair<Proposition, Proposition>> getIncompProp() {
        return incompProp;
    }

    public void setIncompProp(Set<Pair<Proposition, Proposition>> incompProp) {
        this.incompProp = incompProp;
        notifyObservers();
    }

    @Override
    public void updateProposition(Proposition updatedProposition) {
        if (!this.propositions.contains(updatedProposition)) {
            this.propositions.add(updatedProposition);
        } else {
            this.propositions.remove(updatedProposition);
        }
    }

    @Override
    public void addObserver(IncompPropObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IncompPropObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IncompPropObserver observer : this.observers) {
            observer.updateIncomp(this);
        }
    }

    public boolean decisionsExist() {
        return decisions != null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Objectively incompatible propositions: <");

        int i = 0;
        for (Pair<Proposition, Proposition> pair : incompProp) {
            sb.append("< ").append(pair.getFirst().getStatement()).append(", ").append(pair.getSecond().getStatement()).append(" >");
            if(i < incompProp.size() - 1) { // do not add comma after last pair
                sb.append(", ");
            }
            i++;
        }

        sb.append("> ∈ IncompProp.");

        return sb.toString();
    }
}
