package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.PropBaseClean;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.AV.AgentValue;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.AV.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.AVP.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Scale_Weight.Weight;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Value.Value;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observables.PBC_Observable;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.AVP_Observer;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.AV_Observer;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.PBC_Observer;

import java.util.*;

public class ListPropBaseClean implements AV_Observer, AVP_Observer, PBC_Observable {
    private Set<Agent> agents;
    private Set<Proposition> propositions;
    private AgentValueToWeight AVWeight;
    private AgentValuePropWeight AVPWeight;
    private HashMap<Agent, Set<Proposition>> listPropBaseClean;
    private List<PBC_Observer> observers;

    public ListPropBaseClean(){this.observers = new ArrayList<>();}

    private HashMap<Agent, Set<Proposition>> calculatePropBaseClean(Set<Agent> agents,
                                                                    Set<Proposition> props,
                                                                    AgentValueToWeight agentValueToWeight,
                                                                    AgentValuePropWeight agentValuePropWeight) {
        HashMap<Agent, Set<Proposition>> propBaseClean = new HashMap<>();
        for (Agent agent : agents) {
            Set<Proposition> agentProps = calculateAgentPropositions(agent, props, agentValueToWeight, agentValuePropWeight);
            propBaseClean.put(agent, agentProps);
        }
        notifyObservers();
        return propBaseClean;
    }

    private Set<Proposition> calculateAgentPropositions(Agent agent, Set<Proposition> propositions,
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

    private boolean filterPropositions(Agent agent, Proposition prop,
                                       AgentValueToWeight agentValueToWeight,
                                       AgentValuePropWeight agentValuePropWeight) {
        for (AgentValue agentValue : agentValueToWeight.keySet()) {
            if (agentValue.getAgent().equals(agent)) {
                Value value = agentValue.getValue();
                Weight weightAgentValueProp = agentValuePropWeight.getWeight(agent, value, prop);
                Weight weightAgentValueTo = agentValueToWeight.getWeight(agentValue);
                if (weightAgentValueProp != null && (weightAgentValueProp.equals("?") ||
                        (weightAgentValueTo != null && weightAgentValueProp.compareTo(weightAgentValueTo) >= 0))) {
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

    public HashMap<Agent, Set<Proposition>> getPropBaseClean() {
        return listPropBaseClean;
    }

    public Set<Proposition> getAgentPropBaseClean(Agent agent){
        return listPropBaseClean.get(agent);
    }

    public void setPropBaseClean(HashMap<Agent, Set<Proposition>> propBaseClean) {
        this.listPropBaseClean = propBaseClean;
    }

    public void setAgents(Set<Agent> agents) {
        this.agents = agents;
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    @Override
    public void updateAV(AgentValueToWeight agentValueToWeight) {
        // Update the weight for a given AgentValue
        this.AVWeight = agentValueToWeight;
        // if the weight is edited, then do (so that when we add agents and values, it is not done twice)
        if(this.AVWeight.isEditing()) {
            this.listPropBaseClean = calculatePropBaseClean(this.agents, this.propositions, this.AVWeight, this.AVPWeight);
        }
    }

    @Override
    public void updateAVP(AgentValuePropWeight agentValuePropWeight) {
        // Update the list of agents and proposals
        this.agents = new HashSet<>(agentValuePropWeight.getAgents());
        this.propositions = new HashSet<>(agentValuePropWeight.getPropositions());

        // Update hashMap_AVP_Weight
        this.AVPWeight = agentValuePropWeight;

        // Agent and proposal lists have been updated, so we are recalculating propBaseClean
        this.listPropBaseClean = calculatePropBaseClean(this.agents, this.propositions, this.AVWeight, this.AVPWeight);
    }

    @Override
    public void addObserver(PBC_Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(PBC_Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (PBC_Observer observer : this.observers){
            observer.updatePBC(this);
        }
    }
}