package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AVP;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
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

    private void addWeightsForNewElement(Agent agent, Value value, Proposition proposition) {
        AgentValueProposition agentValueProposition = new AgentValueProposition(agent, value, proposition);
        if (!agentValuePropWeights.containsKey(agentValueProposition)) {
            agentValuePropWeights.put(agentValueProposition, new Weight(scale, "?"));  // Domyślna wartość "?" dla nowych wag
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
    public void updateAgent(Agent updatedAgent) {
        // If the agent is new and not yet listed, add him
        if (!this.agents.contains(updatedAgent)) {
            this.agents.add(updatedAgent);
            for (Value value : values) {
                for (Proposition proposition : propositions) {
                    addWeightsForNewElement(updatedAgent, value, proposition);
                }
            }
        } else { // If an agent is already listed, we remove him
            this.agents.remove(updatedAgent);
            // Remove all pairs agent-value associated with this agent
            this.agentValuePropWeights.entrySet().removeIf(entry -> entry.getKey().getAgent().equals(updatedAgent));
        }
    }

    @Override
    public void updateProposition(Proposition updatedProposition) {
        if (!this.propositions.contains(updatedProposition)) {
            this.propositions.add(updatedProposition);
            for (Agent agent : agents) {
                for (Value value : values) {
                    addWeightsForNewElement(agent, value, updatedProposition);
                }
            }
        } else {
            this.propositions.remove(updatedProposition);
            this.agentValuePropWeights.entrySet().removeIf(entry -> entry.getKey().getProposition().equals(updatedProposition));
        }
    }

    @Override
    public void updateValue(Value updatedValue) {
        if (!this.values.contains(updatedValue)) {
            this.values.add(updatedValue);
            for (Agent agent : agents) {
                for (Proposition proposition : propositions) {
                    addWeightsForNewElement(agent, updatedValue, proposition);
                }
            }
        } else {
            this.values.remove(updatedValue);
            this.agentValuePropWeights.entrySet().removeIf(entry -> entry.getKey().getValue().equals(updatedValue));
        }
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