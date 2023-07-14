package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AVP;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.ListValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AgentValueWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Weight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.AVPObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.*;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;

import java.util.*;

public class AgentValuePropWeight extends AgentValueWeight implements AgentObserver, ValueObserver, PropositionObserver, ScaleObserver, IncompPropObserver, AVPObservable {
    private HashMap<AgentValueProposition, Weight> agentValuePropWeights;
    private List<Agent> agents;
    private List<Value> values;
    private List<Proposition> propositions;
    private boolean isEditing = false;
    private List<AVPObserver> weightObservers = new ArrayList<>();
    private Scale scale;

    public AgentValuePropWeight() {
        this.agentValuePropWeights = new HashMap<>();
        this.agents = new LinkedList<Agent>();
        this.values = new LinkedList<Value>();
        this.propositions = new LinkedList<Proposition>();
        this.scale = new Scale();
        this.weightObservers = new ArrayList<>();
    }

    public AgentValuePropWeight(HashMap<AgentValueProposition, Weight> agent_value_prop_weights, List<Agent> agents, List<Value> values, List<Proposition> propositions, Scale scale) {
        this.agentValuePropWeights = agent_value_prop_weights;
        this.agents = agents;
        this.values = values;
        this.propositions = propositions;
        this.scale = scale;
    }

    public void addValue(Agent agent, Value value, Proposition prop, Weight weight) {
        AgentValueProposition agentValueProp = new AgentValueProposition(agent, value, prop);
        this.agentValuePropWeights.put(agentValueProp, weight);
    }

    public Weight getWeight(Agent agent, Value value, Proposition prop) {
        AgentValueProposition agentValueProp = new AgentValueProposition(agent, value, prop);
        return this.agentValuePropWeights.get(agentValueProp);
    }

    public HashMap<AgentValueProposition, Weight> getAgentValuePropWeights() {
        return agentValuePropWeights;
    }

    public void setAgentValuePropWeights(HashMap<AgentValueProposition, Weight> agent_value_prop_weights) {
        this.agentValuePropWeights = agent_value_prop_weights;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
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
    }

    @Override
    public Weight getWeight(AgentValue agentValue) {
        return null;
    }

    public void editWeight(AgentValueProposition agentValueProposition, double newWeight) {
        Weight weight = agentValuePropWeights.get(agentValueProposition);
        if (weight != null && newWeight >= scale.getMin() && newWeight <= scale.getMax()) {
            weight.setWeight(newWeight);
            notifyAVPObservers();
        }
    }

    //Adding default weights "?" - waiting for user edits
    private void addWeightsForNewElement(Agent agent, Value value, Proposition proposition) {
        AgentValueProposition agentValueProposition = new AgentValueProposition(agent, value, proposition);
        if (!agentValuePropWeights.containsKey(agentValueProposition)) {
            agentValuePropWeights.put(agentValueProposition, new Weight(scale, "?"));  // Default "?" for new scales
        }
    }

    public void setEditing(boolean isEditing) {
        this.isEditing = isEditing;
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
            this.values.remove(agent);
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

        setEditing(false);
        notifyAVPObservers();
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

        setEditing(false);
        notifyAVPObservers();
    }

    @Override
    public void updateValue(ListValue ListValue) {
        List<Value> updatedValues = ListValue.getValues();

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

        setEditing(false);
        notifyAVPObservers();
    }

    public void addWeight(Agent agent, Value value, Proposition prop, Weight weight) {
        // if agent, value and proposition exists
        addAgent(agent);
        addValue(value);
        addProposition(prop);

        // add weight
        AgentValueProposition agentValueProp = new AgentValueProposition(agent, value, prop);
        agentValuePropWeights.put(agentValueProp, weight);
    }

    public void addAgent(Agent agent) {
        if (!agents.contains(agent)) {
            agents.add(agent);
        }
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
    }

    @Override
    protected void updateAllWeightsAccordingToScale() {
        agentValuePropWeights.values().forEach(this::updateWeightAccordingToScale);
    }

    private void updateWeightAccordingToScale(Weight weight) {
        Integer weightValue = (Integer) weight.getWeight();
        if (weightValue.equals("?")) {
            weight.setWeight(scale.getMax());
        } else if (!scale.contains(weightValue)) {
            adjustWeightToScaleBounds(weight, weightValue);
        }
    }

    private void adjustWeightToScaleBounds(Weight weight, Integer weightValue) {
        if (weightValue < scale.getMin()) {
            weight.setWeight(scale.getMin());
        } else {
            weight.setWeight(scale.getMax());
        }
    }

    @Override
    public void addObserver(AVPObserver observer) {
        weightObservers.add(observer);
    }

    @Override
    public void removeObserver(AVPObserver observer) {
        weightObservers.remove(observer);
    }

    @Override
    public void notifyAVPObservers() {
        for (AVPObserver observer : weightObservers) {
            observer.updateAVP(this);
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
}