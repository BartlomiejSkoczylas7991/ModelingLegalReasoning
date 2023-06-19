package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.observables.IncompProp_Observable;
import com.bskoczylas.modelinglegalreasoning.models.observers.IncompProp_Observer;
import com.bskoczylas.modelinglegalreasoning.models.observers.PropositionObserver;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListIncompProp implements PropositionObserver, IncompProp_Observable {
    private List<Proposition> propositions;
    private List<IncompProp_Observer> observers;
    private Set<Pair<Proposition, Proposition>> incompProp;

    public ListIncompProp() {
        this.incompProp = new HashSet<>();
    }

    public void addIncompatiblePropositions(Proposition prop1, Proposition prop2) {
        // Ensure that the pair is not already present in the set
        // and that prop1 and prop2 are not the same proposition
        if (!prop1.equals(prop2) && !containsPair(prop1, prop2)) {
            incompProp.add(new Pair<>(prop1, prop2));
        } else {
            System.out.println("Cannot add pair: propositions are the same or pair already exists");
        }
    }

    public void setDecision(Pair<Proposition, Proposition> incompProp) {
        incompProp.getKey().setDecision(true);
        incompProp.getValue().setDecision(true);
    }

    boolean containsPair(Proposition prop1, Proposition prop2) {
        // Check if the pair is in the set in either order
        return incompProp.contains(new Pair<>(prop1, prop2)) || incompProp.contains(new Pair<>(prop2, prop1));
    }

    public Set<Pair<Proposition, Proposition>> getIncompatiblePropositions() {
        return incompProp;
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
    public void addObserver(IncompProp_Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(IncompProp_Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (IncompProp_Observer observer : this.observers) {
            observer.updateIncomp(this);
        }
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
    }
}
