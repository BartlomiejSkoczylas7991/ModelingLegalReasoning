package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp;

import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.AVPObserverController;
import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.AVPObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.ListValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable.AVPObservableController;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.*;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;

import java.io.Serializable;
import java.util.*;

public class AgentValuePropWeight implements Serializable, AgentObserver, ValueObserver, PropositionObserver, ScaleObserver, IncompPropObserver, AVPObservable {
    private Map<AgentValueProposition, Weight> agentValuePropWeights;
    private List<Agent> agents;
    private List<Value> values;
    private List<Proposition> propositions;
    private List<AVPObserver> observers = new ArrayList<>();
    private Scale scale;

    public AgentValuePropWeight() {
        this.agentValuePropWeights = new HashMap<>();
        this.agents = new LinkedList<Agent>();
        this.values = new LinkedList<Value>();
        this.propositions = new LinkedList<Proposition>();
        this.scale = new Scale();
    }

    public Map<AgentValueProposition, Weight> getSerializableCopyOfMap() {
        return new HashMap<>(agentValuePropWeights);
    }

    public Set<AgentValueProposition> keySet() {
        return agentValuePropWeights.keySet();
    }

    public Weight getWeight(Agent agent, Value value, Proposition prop) {
        AgentValueProposition agentValueProp = new AgentValueProposition(agent, value, prop);
        return this.agentValuePropWeights.get(agentValueProp);
    }

    public Weight getWeight(AgentValueProposition agentValueProposition) {
        return this.agentValuePropWeights.get(agentValueProposition);
    }

    public Map<AgentValueProposition, Weight> getAgentValuePropWeights() {
        return agentValuePropWeights;
    }

    public void setAgentValuePropWeights(Map<AgentValueProposition, Weight> agent_value_prop_weights) {
        this.agentValuePropWeights = agent_value_prop_weights;
        notifyObservers();
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                        agents = agents;
    }

    public List<Value> getValues() {
        return values;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public List<Proposition> getPropositions() {
        return propositions;
    }

    public void setPropositions(List<Proposition> propositions) {
        this.propositions = propositions;
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
        agentValuePropWeights.values().forEach(this::updateWeightAccordingToScale);
        notifyObservers();
    }

    public void addWeight(Agent agent, Value value, Proposition prop, Weight weight) {
        AgentValueProposition agentValueProp = new AgentValueProposition(agent, value, prop);
        agentValuePropWeights.put(agentValueProp, weight);
        notifyObservers();
    }

    public void editWeight(AgentValueProposition agentValueProposition, Integer newWeight) {
        if (newWeight != null && newWeight >= scale.getMin() && newWeight <= scale.getMax()) {
            agentValuePropWeights.put(agentValueProposition, Weight.of(newWeight));
            notifyObservers();
        } else if (newWeight == null) {
            agentValuePropWeights.put(agentValueProposition, Weight.indeterminate());
            notifyObservers();
        }
    }

    public void editWeight(Agent agent, Value value, Proposition proposition, Integer newWeight) {
        AgentValueProposition agentValueProposition = new AgentValueProposition(agent, value, proposition);
        editWeight(agentValueProposition, newWeight);
    }

    //Adding default weights "?" - waiting for user edits
    private void addWeightsForNewElement(Agent agent, Value value, Proposition proposition) {
        AgentValueProposition agentValueProposition = new AgentValueProposition(agent, value, proposition);
        if (!agentValuePropWeights.containsKey(agentValueProposition)) {
            agentValuePropWeights.put(agentValueProposition, Weight.indeterminate());
        }
    }

    @Override
    public String toString() {
        for (Agent agent : agents) {
            for (Value value : values) {
                for (Proposition proposition : propositions) {
                    addWeightsForNewElement(agent, value, proposition);
                }
            }
        }
        StringBuilder weightsStr = new StringBuilder("{");
        for (var entry : this.agentValuePropWeights.entrySet()) {
            weightsStr.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        weightsStr.append("}");
        return weightsStr.toString();
    }

    @Override
    public void updateAgent(ListAgent listAgent) {
        List<Agent> updatedAgents = listAgent.getAgents();

        // Find agents that have been removed
        List<Agent> removedAgents = new ArrayList<>(this.agents);
        removedAgents.removeAll(updatedAgents);
        // Remove the associated AgentValuePropWeights
        for (Agent agent : removedAgents) {
            this.agents.remove(agent);
            this.agentValuePropWeights.entrySet().removeIf(entry -> entry.getKey().getAgent().equals(agent));
        }

        // Find new added agents
        List<Agent> addedAgents = new ArrayList<>(updatedAgents);
        addedAgents.removeAll(this.agents);
        // Add the associated AgentValuePropWeights
        for (Agent agent : addedAgents) {
            this.agents.add(agent);
            for (Value value : values) {
                for (Proposition proposition : propositions) {
                    addWeightsForNewElement(agent, value, proposition);
                }
            }
        }
        notifyObservers();
    }

    @Override
    public void updateProposition(ListProposition listProposition) {
        List<Proposition> updatedPropositions = listProposition.getListProposition();

        // Find propositions that have been removed
        List<Proposition> removedPropositions = new ArrayList<>(this.propositions);
        removedPropositions.removeAll(updatedPropositions);
        // Remove the associated AgentValuePropWeights
        for (Proposition proposition : removedPropositions) {
            this.propositions.remove(proposition);
            this.agentValuePropWeights.entrySet().removeIf(entry -> entry.getKey().getProposition().equals(proposition));
        }

        // Find newly added propositions
        List<Proposition> addedPropositions = new ArrayList<>(updatedPropositions);
        addedPropositions.removeAll(this.propositions);
        // Add the associated AgentValuePropWeights
        for (Proposition proposition : addedPropositions) {
            this.propositions.add(proposition);
            for (Value value : values) {
                for (Agent agent : agents) {
                    addWeightsForNewElement(agent, value, proposition);
                }
            }
        }

        notifyObservers();
    }

    @Override
    public void updateValue(ListValue ListValue) {
        List<Value> updatedValues = ListValue.getListValue();

        // Find the values that have been removed
        List<Value> removedValues = new ArrayList<>(this.values);
        removedValues.removeAll(updatedValues);
        // Remove the associated AgentValuePropWeights
        for (Value value : removedValues) {
            this.values.remove(value);
            this.agentValuePropWeights.entrySet().removeIf(entry -> entry.getKey().getValue().equals(value));
        }

        // Find newly added values
        List<Value> addedValues = new ArrayList<>(updatedValues);
        addedValues.removeAll(this.values);
        // Add the associated AgentValuePropWeights
        for (Value value : addedValues) {
            this.values.add(value);
            for (Agent agent : agents) {
                for (Proposition proposition : propositions) {
                    addWeightsForNewElement(agent, value, proposition);
                }
            }
        }

        notifyObservers();
    }

    public void addAgent(Agent agent) {
        if (!agents.contains(agent)) {
            agents.add(agent);
        }
    }

    public boolean isEmpty() {
        return this.agentValuePropWeights.isEmpty();
    }

    public void addValue(Value value) {
        if (!values.contains(value)) {
            values.add(value);
        }
    }

    public void addProposition(Proposition prop) {
        if (!propositions.contains(prop)) {
            propositions.add(prop);
        }
    }

    @Override
    public void updateScale(Scale updatedScale) {
        this.scale = updatedScale;
        agentValuePropWeights.values().forEach(this::updateWeightAccordingToScale);
        notifyObservers();
    }

    private void updateWeightAccordingToScale(Weight weight) {
        Integer weightValue = weight.getNumberValue();
        if (weightValue != null && !scale.contains(weightValue)) {
            adjustWeightToScaleBounds(weight, weightValue);
        }
    }

    private void adjustWeightToScaleBounds(Weight weight, Integer weightValue) {
        Map.Entry<AgentValueProposition, Weight> entry = agentValuePropWeights.entrySet()
                .stream()
                .filter(e -> e.getValue().equals(weight))
                .findFirst()
                .orElse(null);
        if (entry == null) {
            return;
        }

        if (weightValue < scale.getMin()) {
            entry.setValue(Weight.of(scale.getMin()));
        } else {
            entry.setValue(Weight.of(scale.getMax()));
        }
    }

    @Override
    public void updateIncomp(ListIncompProp listIncompProp) {
        Pair<Proposition, Proposition> decisions = listIncompProp.getDecisions();

        // if a pair of decisions has been added, then we change the given pair of proposals to "true" in the proposal pool
        if (decisions != null) {
            for (Proposition prop : propositions) {
                if (prop.equals(decisions.getFirst()) || prop.equals(decisions.getSecond())) {
                    prop.setDecision(true);
                }
            }
        }
    }

    @Override
    public void addObserver(AVPObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(AVPObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (AVPObserver observer : observers) {
            observer.updateAVP(this);
        }
    }
}