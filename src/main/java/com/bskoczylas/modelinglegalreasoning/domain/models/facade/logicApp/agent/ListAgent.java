package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AgentObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.AgentObservable;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListAgent implements AgentObservable {
    private List<Agent> listAgent = new ArrayList<>();
    private final List<AgentObserver> observers = new ArrayList<>();

    public ListAgent() {}

    public ListAgent(List<Agent> listAgent) {
        this.listAgent = listAgent;
    }

    public List<AgentObserver> getObservers() {
        return observers;
    }

    public List<Agent> getAgents() {
        return listAgent;
    }

    public void addAgent(Agent agent) {
        listAgent.add(agent);
        notifyAgentObservers(this);
    }

    public void addAgents(List<Agent> agents) {
        listAgent.addAll(agents);
        notifyAgentObservers(this);
    }

    public void removeAgents(List<Agent> agents) {
        listAgent.removeAll(agents);
        notifyAgentObservers(this);
    }

    public void setListAgent(List<Agent> listAgent) {
        this.listAgent = listAgent;
        notifyAgentObservers(this);
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
    public void notifyAgentObservers(ListAgent listAgent) {
        for (AgentObserver observer : this.observers) {
            observer.updateAgent(listAgent);
        }
    }

    public boolean removeAgent(Agent agent) {
        boolean result = listAgent.remove(agent);
        if(result) {
            notifyAgentObservers(this);
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Agents = {");

        for(int i=0; i<listAgent.size(); i++){
            sb.append(listAgent.get(i).getName());

            if(i < listAgent.size() - 1) { // do not add comma after last element
                sb.append(", ");
            }
        }

        sb.append("}");
        return sb.toString();
    }
}
