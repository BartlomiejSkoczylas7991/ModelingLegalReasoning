package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.propBaseClean;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.PBCObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AVPObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AVObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PBCObserver;

import java.util.*;

public class ListPropBaseClean implements AVObserver, AVPObserver, PBCObservable {
    private Set<Agent> agents;
    private Set<Proposition> propositions;
    private AgentValueToWeight AVWeight;
    private AgentValuePropWeight AVPWeight;
    private HashMap<Agent, Set<Proposition>> listPropBaseClean;
    private List<PBCObserver> observers;

    public ListPropBaseClean(){this.observers = new ArrayList<>();}

    protected HashMap<Agent, Set<Proposition>> calculatePropBaseClean(Set<Agent> agents,
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

    protected Set<Proposition> calculateAgentPropositions(Agent agent, Set<Proposition> propositions,
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

    public Set<Proposition> getPropositions() {
        return propositions;
    }

    public void setPropositions(Set<Proposition> propositions) {
        this.propositions = propositions;
    }

    public AgentValueToWeight getAVWeight() {
        return AVWeight;
    }

    public void setAVWeight(AgentValueToWeight AVWeight) {
        this.AVWeight = AVWeight;
    }

    public AgentValuePropWeight getAVPWeight() {
        return AVPWeight;
    }

    public void setAVPWeight(AgentValuePropWeight AVPWeight) {
        this.AVPWeight = AVPWeight;
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
        return sb.toString();
    }
}