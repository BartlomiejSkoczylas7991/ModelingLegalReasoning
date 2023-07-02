package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.lists;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Agent;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observables.AgentObservable;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.AgentObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListAgent implements AgentObservable {
    private List<Agent> listAgent = new LinkedList<>();
    private List<AgentObserver> observers;

    public ListAgent(){this.observers = new ArrayList<>();}
    public ListAgent(List<Agent> listAgent) {
        this.listAgent = listAgent;
        this.observers = new ArrayList<>();
    }

    public List<Agent> getAgents() {
        return listAgent;
    }

    public void addAgent(Agent agent) {
        listAgent.add(agent);
        notifyAgentObservers(agent);
    }

    public void removeAgent(Agent agent) {
        listAgent.remove(agent);
        notifyAgentObservers(agent);
    }

    public void setListAgent(List<Agent> listAgent) {
        this.listAgent = listAgent;
    }

    @Override
    public void addAgentObserver(AgentObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeAgentObserver(AgentObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyAgentObservers(Agent agent) {
        for (AgentObserver observer : this.observers) {
            observer.updateAgent(agent);
        }
    }
}
