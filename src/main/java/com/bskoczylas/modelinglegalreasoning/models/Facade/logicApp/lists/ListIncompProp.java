package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.lists;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Proposition;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observables.IncompProp_Observable;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.IncompProp_Observer;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.PropositionObserver;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ListIncompProp implements PropositionObserver, IncompProp_Observable {
    private List<Proposition> propositions;
    private List<IncompProp_Observer> observers;
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
    }

    public void setDecisions(Pair<Proposition, Proposition> incompProp) {
        incompProp.getKey().setDecision(true);
        incompProp.getValue().setDecision(true);
        this.decisions = incompProp;
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
