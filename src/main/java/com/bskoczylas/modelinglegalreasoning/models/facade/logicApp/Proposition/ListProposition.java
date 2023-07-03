package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Proposition;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observables.PropositionObservable;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.PropositionObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListProposition implements PropositionObservable {
    private List<Proposition> listProposition = new LinkedList<>();
    private List<PropositionObserver> observers;


    public ListProposition(List<Proposition> listProposition) {
        this.listProposition = listProposition;
        this.observers = new ArrayList<>();
    }

    public ListProposition(){this.observers = new ArrayList<>();}

    public List<Proposition> getPropositions() {
        return listProposition;
    }

    public void addProposition(Proposition proposition) {
        listProposition.add(proposition);
        notifyObservers(proposition);
    }

    public void removeProposition(Proposition proposition) {
        listProposition.remove(proposition);
        notifyObservers(proposition);
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

