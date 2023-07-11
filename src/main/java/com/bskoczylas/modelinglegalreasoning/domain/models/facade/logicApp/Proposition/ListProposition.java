package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.PropositionObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PropositionObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListProposition implements PropositionObservable {
    private List<Proposition> listProposition = new LinkedList<>();
    private final List<PropositionObserver> observers = new ArrayList<>();;

    public ListProposition() {}

    public ListProposition(List<Proposition> listProposition) {
        this.listProposition = listProposition;
    }

    public void addProposition(Proposition proposition) {
        listProposition.add(proposition);
        notifyObservers(proposition);
    }

    public void removeProposition(List<Proposition> propositions) {
        listProposition.removeAll(propositions);
        for (Proposition proposition : propositions) {
            notifyObservers(proposition);
        }
    }

    public void setListProposition(List<Proposition> listProposition) {
        this.listProposition = listProposition;
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
    public void notifyObservers(Proposition proposition) {
        for (PropositionObserver observer : this.observers) {
            observer.updateProposition(proposition);
        }
    }
}

