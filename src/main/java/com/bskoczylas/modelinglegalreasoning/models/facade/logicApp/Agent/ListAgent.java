package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observables.AgentObservable;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.AgentObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListAgent implements AgentObservable {
    private List<Agent> listAgent = new LinkedList<>();
    private final List<AgentObserver> observers = new ArrayList<>();

    public ListAgent() {}

    public ListAgent(List<Agent> listAgent) {
        this.listAgent = listAgent;
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
