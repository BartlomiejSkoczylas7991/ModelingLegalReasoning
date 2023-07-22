package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.IncompPropObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.IncompPropObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PropositionObserver;

import java.util.ArrayList;
import java.util.List;

public class ListIncompProp implements PropositionObserver, IncompPropObservable {
    private List<Proposition> propositions;
    private List<IncompPropObserver> observers;
    private List<IncompProp> incompPropList;

    public ListIncompProp() {
        this.incompPropList = new ArrayList<>();
        this.observers = new ArrayList<>();
        this.propositions = new ArrayList<>();
    }

    public void addIncompatiblePropositions(IncompProp incompProp) {
        // Ensure that the pair is not already present in the list
        incompPropList.add(incompProp);
        notifyObservers();
    }

    public void setDecision(IncompProp incompProp) {
        // Unset any existing decision
        for (IncompProp prop : incompPropList) {
            if (prop.isDecision()) {
                prop.setDecision(false);
            }
        }

        // Set the new decision
        incompProp.setDecision(true);
        notifyObservers();
    }

    public void setDecisions(Pair<Proposition, Proposition> decisions) {
        IncompProp incompProp = new IncompProp(decisions, true);
        this.incompPropList.add(incompProp);
        notifyObservers();
    }

    public IncompProp getDecision() {
        for (IncompProp prop : incompPropList) {
            if (prop.isDecision()) {
                return prop;
            }
        }
        return null;
    }

    public Pair<Proposition, Proposition> getDecisions() {
        for (IncompProp entry : this.incompPropList) {
            if(entry.isDecision()) {
                return entry.getPropositionsPair();
            }
        }
        return null;
    }
    boolean containsPair(Proposition prop1, Proposition prop2) {
        for (IncompProp pair : this.incompPropList) {
            Pair<Proposition, Proposition> propPair = pair.getPropositionsPair();
            if (propPair.getFirst().equals(prop1) && propPair.getSecond().equals(prop2)
                    || propPair.getFirst().equals(prop2) && propPair.getSecond().equals(prop1)) {
                return true;
            }
        }
        return false;
    }

    public List<IncompProp> getIncompatiblePropositions() {
        return incompPropList;
    }

    public List<Proposition> getPropositions() {
        return propositions;
    }

    public void setPropositions(List<Proposition> propositions) {
        this.propositions = propositions;
    }

    public void setIncompPropList(List<IncompProp> incompPropList) {
        this.incompPropList = incompPropList;
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
                removeDecisionProp(prop);
                removeIncompPropByProposition(prop);
            }
        }
    }

    public void removeIncompPropByProposition(Proposition prop) {
        // If prop is part of any incompatible pair, remove it
        incompPropList.removeIf(incompProp -> incompProp.getPropositionsPair().getFirst().equals(prop)
                || incompProp.getPropositionsPair().getSecond().equals(prop));
        notifyObservers();
    }

    public void removeIncompProp(IncompProp incompProp) {
        incompPropList.remove(incompProp);
        if (incompProp.isDecision()) {
            incompProp.setDecision(false);
        }
        notifyObservers();
    }

    public List<IncompPropObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<IncompPropObserver> observers) {
        this.observers = observers;
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
        for (IncompProp pair : this.incompPropList){
            if (pair.isDecision()) {
                return true;
            }
        }
        return false;
    }

    public void removeDecisionProp(Proposition prop) {
        for (IncompProp incompProp : incompPropList) {
            if (incompProp.isDecision() &&
                    (incompProp.getPropositionsPair().getFirst().equals(prop)
                            || incompProp.getPropositionsPair().getSecond().equals(prop))) {
                incompProp.setDecision(false);
                notifyObservers();
            }
        }
    }

    public void removeDecision(IncompProp incompProp) {
        for (IncompProp entry : incompPropList) {
            if (entry.isDecision()) {
                incompPropList.remove(entry);
            }
            notifyObservers();
        }

    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Objectively incompatible propositions: <");

        int i = 0;
        for (IncompProp pair : this.incompPropList) {
            sb.append("< ").append(pair.getPropositionsPair()
                            .getFirst().getStatement())
                            .append(", ").append(pair
                            .getPropositionsPair().getSecond()
                            .getStatement()).append(" >");
            if(i < this.incompPropList.size() - 1) { // do not add comma after last pair
                sb.append(", ");
            }
            i++;
        }

        sb.append("> ∈ IncompProp.");

        return sb.toString();
    }
}
