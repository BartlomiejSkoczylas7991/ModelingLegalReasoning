package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.propBaseClean;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AVObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AVPObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.PBCObservable;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.AVPObserverController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.AVObserverController;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PBCObserver;

import java.io.Serializable;
import java.util.*;

public class ListPropBaseClean implements Serializable, AVPObserver, AVObserver, PBCObservable {
    private List<Agent> agents;
    private List<Proposition> propositions;
    private AgentValueToWeight aVWeight;
    private AgentValuePropWeight aVPWeight;
    private Map<Agent, Set<Proposition>> listPropBaseClean;
    private transient List<PBCObserver> observers;

    public ListPropBaseClean(){
        this.observers = new ArrayList<>();
        this.agents = new ArrayList<>();
        this.propositions = new ArrayList<>();
        this.aVWeight = new AgentValueToWeight();
        this.aVPWeight = new AgentValuePropWeight();
        this.listPropBaseClean = new HashMap<Agent, Set<Proposition>>();
    }

    public HashMap<Agent, Set<Proposition>> calculatePropBaseClean(List<Agent> agents,
                                                                      List<Proposition> props,
                                                                      AgentValueToWeight agentValueToWeight,
                                                                      AgentValuePropWeight agentValuePropWeight) {
        HashMap<Agent, Set<Proposition>> propBaseClean = new HashMap<>();
        for (Agent agent : agents) {
            Set<Proposition> agentProps = calculateAgentPropositions(agent, props, agentValueToWeight, agentValuePropWeight);
            propBaseClean.put(agent, agentProps);
        }
        return propBaseClean;
    }

    protected Set<Proposition> calculateAgentPropositions(Agent agent, List<Proposition> propositions,
                                                        AgentValueToWeight agentValueToWeight,
                                                        AgentValuePropWeight agentValuePropWeight) {
        Set<Proposition> agentProps = new HashSet<>();
        for (Proposition prop : propositions) {
            if (filterPropositions(agent, prop, agentValueToWeight, agentValuePropWeight)) {
                agentProps.add(prop);
            }
        }
        return agentProps;
    }

    protected boolean filterPropositions(Agent agent, Proposition prop,
                                         AgentValueToWeight agentValueToWeight,
                                         AgentValuePropWeight agentValuePropWeight) {
        for (AgentValue agentValue : agentValueToWeight.keySet()) {
            if (agentValue.getAgent().equals(agent)) {
                Value value = agentValue.getValue();
                Weight weightAgentValueProp = agentValuePropWeight.getWeight(agent, value, prop);
                Weight weightAgentValueTo = agentValueToWeight.getWeight(agentValue);
                if (weightAgentValueProp != null && (weightAgentValueProp.isIndeterminate() ||
                        (weightAgentValueTo != null &&
                                !weightAgentValueTo.isIndeterminate() &&
                                weightAgentValueProp.getNumberValue() >= weightAgentValueTo.getNumberValue()))) {
                    return true;
                }
            }
        }
        return false;
    }

    public static Set<String> getSetStatementProp(Agent agent, HashMap<Agent, Set<Proposition>> propBaseClean) {
        Set<String> propStatements = new HashSet<>();
        Set<Proposition> propositions = propBaseClean.get(agent);
        if (propositions != null) {
            for (Proposition prop : propositions) {
                propStatements.add(prop.getStatement());
            }
        }
        return propStatements;
    }

    @Override
    public void updateAV(AgentValueToWeight agentValueToWeight) {
        this.aVWeight = agentValueToWeight;
        this.agents = agentValueToWeight.getAgents();
            if(!(propositions).isEmpty() && !(this.aVPWeight).isEmpty()) {
                this.listPropBaseClean = calculatePropBaseClean(this.agents, this.propositions, this.aVWeight, this.aVPWeight);
                notifyObservers();
        }
    }

    @Override
    public void updateAVP(AgentValuePropWeight agentValuePropWeight) {
        this.aVPWeight = agentValuePropWeight;
        this.agents = agentValuePropWeight.getAgents();
        this.propositions = agentValuePropWeight.getPropositions();
        if(!(this.aVPWeight).isEmpty()) {
            this.listPropBaseClean = calculatePropBaseClean(this.agents, this.propositions, this.aVWeight, this.aVPWeight);
            notifyObservers();
        }
    }

    public Set<Proposition> getAgentPropBaseClean(Agent agent){
        return listPropBaseClean.get(agent);
    }

    public List<Proposition> getPropositions() {
        return propositions;
    }

    public void setPropositions(List<Proposition> propositions) {
        this.propositions = propositions;
    }

    public HashMap<Agent, Set<Proposition>> getListPropBaseClean() {
        return listPropBaseClean;
    }

    public void setListPropBaseClean(HashMap<Agent, Set<Proposition>> listPropBaseClean) {
        this.listPropBaseClean = listPropBaseClean;
    }

    public List<PBCObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<PBCObserver> observers) {
        this.observers = observers;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public AgentValueToWeight getAVWeight() {
        return aVWeight;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    @Override
    public void addObserver(PBCObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(PBCObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (PBCObserver observer : this.observers){
            observer.updatePBC(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (listPropBaseClean != null) {
            for (Map.Entry<Agent, Set<Proposition>> entry : listPropBaseClean.entrySet()) {
                sb.append(entry.getKey().getName()).append(" = {");

                Iterator<Proposition> iterator = entry.getValue().iterator();
                while (iterator.hasNext()) {
                    sb.append(iterator.next().getStatement());
                    if (iterator.hasNext()) {
                        sb.append(", ");
                    }
                }
                sb.append("}\n");
            }
        }
        return sb.toString();
    }
}