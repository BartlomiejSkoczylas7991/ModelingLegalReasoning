package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.observables.AgentObservable;
import com.bskoczylas.modelinglegalreasoning.models.observers.AgentObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListProposition implements AgentObservable {
    private List<Proposition> listProposition = new LinkedList<>();
    private List<AgentObserver> observers;


    public ListProposition(List<Proposition> listProposition) {
        this.listProposition = listProposition;
        this.observers = new ArrayList<>();
    }

    public List<Proposition> getPropositions() {
        return listProposition;
    }

    public void addProposition(Proposition Proposition) {
        listProposition.add(Proposition);
        notifyObservers();
    }

    public void removeProposition(Proposition Proposition) {
        listProposition.remove(Proposition);
        notifyObservers();
    }

    public void setListProposition(List<Proposition> listProposition) {
        this.listProposition = listProposition;
    }

    @Override
    public void addObserver(AgentObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(AgentObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (AgentObserver observer : this.observers) {
            observer.update();
        }
    }
}

