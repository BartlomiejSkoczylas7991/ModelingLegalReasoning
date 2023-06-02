package com.bskoczylas.modelinglegalreasoning.models.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Objects.Agent;
import com.bskoczylas.modelinglegalreasoning.models.observables.AgentObservable;
import com.bskoczylas.modelinglegalreasoning.models.observers.AgentObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListAgent implements AgentObservable {
    private List<Agent> listAgent = new LinkedList<>();
    private List<AgentObserver> observers;


    public ListAgent(List<Agent> listAgent) {
        this.listAgent = listAgent;
        this.observers = new ArrayList<>();
    }

    public List<Agent> getAgents() {
        return listAgent;
    }

    public void addAgent(Agent agent) {
        listAgent.add(agent);
        notifyObservers();
    }

    public void removeAgent(Agent agent) {
        listAgent.remove(agent);
        notifyObservers();
    }

    public void setListAgent(List<Agent> listAgent) {
        this.listAgent = listAgent;
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
