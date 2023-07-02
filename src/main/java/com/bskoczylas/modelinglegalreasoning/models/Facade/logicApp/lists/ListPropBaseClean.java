package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.lists;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.*;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observables.PBC_Observable;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.AVP_Observer;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.AV_Observer;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.PBC_Observer;

import java.util.*;

public class ListPropBaseClean implements AV_Observer, AVP_Observer, PBC_Observable {
    private Set<Agent> agents;
    private Set<Proposition> propositions;
    private AgentValueToWeight hashMap_AV_Weight;
    private AgentValuePropWeight hashMap_AVP_Weight;
    private HashMap<Agent, Set<Proposition>> listPropBaseClean;
    private List<PBC_Observer> observers;

    public ListPropBaseClean(){this.observers = new ArrayList<>();};

    private HashMap<Agent, Set<Proposition>> calculatePropBaseClean(Set<Agent> agents,
                                                                    Set<Proposition> props,
                                                                    AgentValueToWeight agentValueToWeight,
                                                                    AgentValuePropWeight agentValuePropWeight) {
        HashMap<Agent, Set<Proposition>> propBaseClean = new HashMap<>();
        for (Agent agent : agents) {
            Set<Proposition> agentProps = new HashSet<>();
            for (Proposition prop : props) {
                boolean passFilter = false;
                for (AgentValue agentValue : hashMap_AV_Weight.keySet()) {
                    if (agentValue.getAgent().equals(agent)) { // Zakładam, że AgentValue ma metodę getAgent()
                        Value value = agentValue.getValue(); // Zakładam, że AgentValue ma metodę getValue()
                        Weight weightAgentValueProp = agentValuePropWeight.getWeight(agent, value, prop);
                        Weight weightAgentValueTo = hashMap_AV_Weight.getWeight(agentValue);
                        if (weightAgentValueProp != null && (weightAgentValueProp.equals("?") ||
                                (weightAgentValueTo != null && weightAgentValueProp.compareTo(weightAgentValueTo) >= 0))) {
                            passFilter = true;
                            break;
                        }
                    }
                }
                if (passFilter) {
                    agentProps.add(prop);
                }
            }
            propBaseClean.put(agent, agentProps);
        }
        notifyObservers();
        return propBaseClean;
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
        // Aktualizujemy wagę dla danego AgentValue
        this.hashMap_AV_Weight = agentValueToWeight;
        // jeśli jest edytowana waga, to wykonujemy (by w przypadku gdy dodajemy agentów i wartości nie wykonało się podwójnie
        if(this.hashMap_AV_Weight.isEditing()) {
            this.listPropBaseClean = calculatePropBaseClean(this.agents, this.propositions, this.hashMap_AV_Weight, this.hashMap_AVP_Weight);
        }
    }

    @Override
    public void updateAVP(AgentValuePropWeight agentValuePropWeight) {
        // Aktualizujemy listę agentów i propozycji
        this.agents = new HashSet<>(agentValuePropWeight.getAgents());
        this.propositions = new HashSet<>(agentValuePropWeight.getPropositions());

        // Aktualizujemy hashMap_AVP_Weight
        this.hashMap_AVP_Weight = agentValuePropWeight;

        // Ponieważ listy agentów i propozycji zostały zaktualizowane, obliczamy na nowo propBaseClean
        this.listPropBaseClean = calculatePropBaseClean(this.agents, this.propositions, this.hashMap_AV_Weight, this.hashMap_AVP_Weight);

    }


    @Override
    public void addObserver(PBC_Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(PBC_Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (PBC_Observer observer : this.observers){
            observer.updatePBC(this);
        }
    }
}