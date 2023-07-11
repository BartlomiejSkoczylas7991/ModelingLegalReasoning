package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Weight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AVObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AgentObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ScaleObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ValueObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AgentValueWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.AVObservable;

import java.util.*;

public class AgentValueToWeight extends AgentValueWeight implements AgentObserver, ValueObserver, AVObservable, ScaleObserver {
    private Map<AgentValue, Weight> agentValueWeights;
    private List<Agent> agents;
    private List<Value> values;
    private final List<AVObserver> weightObservers = new ArrayList<>();
    private Scale scale;
    private boolean isEditing = false;

    public AgentValueToWeight() {
        this.agentValueWeights = new HashMap<>();
        this.agents = new LinkedList<Agent>();
        this.values = new LinkedList<Value>();
    }

    public AgentValueToWeight(Map<AgentValue, Weight> agent_value_weights, List<Agent> agents, List<Value> values, Scale scale) {
        this.agentValueWeights = agent_value_weights;
        this.agents = agents;
        this.values = values;
        this.scale = scale;
    }

    public AgentValueToWeight(List<Agent> agents, List<Value> values) {
        this.agentValueWeights = new HashMap<>();
        this.agents = agents;
        this.values = values;
    }

    public Set<AgentValue> keySet() {
        return agentValueWeights.keySet();
    }

    @Override
    public void addAgent(Agent agent) {
        agents.add(agent);
    }

    @Override
    public void addValue(Value value) {
        values.add(value);
    }

    @Override
    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public void addValue(Agent agent, Value value, Weight weight) {
        AgentValue agentValue = new AgentValue(agent, value);
        this.agentValueWeights.put(agentValue, weight);
    }

    public Weight getWeight(AgentValue agentValue) {
        return this.agentValueWeights.get(agentValue);
    }

    @Override
    public String toString() {
        for (Agent agent : this.agents) {
            for (Value value : this.values) {
                AgentValue agentValue = new AgentValue(agent, value);
                if (!this.agentValueWeights.containsKey(agentValue)) {
                    this.agentValueWeights.put(agentValue, new Weight(scale,"?")); // Domyślna wartość "?" dla nowych wag
                }
            }
        }
        StringBuilder weightsStr = new StringBuilder("{");
        for (var entry : this.agentValueWeights.entrySet()) {
            weightsStr.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        weightsStr.append("}");
        return weightsStr.toString();
    }

    @Override
    public void updateAgent(Agent updatedAgent) {
        if (!this.agents.contains(updatedAgent)) {
            this.agents.add(updatedAgent);
            addAgentValuesForAgent(updatedAgent);
        } else {
            this.agents.remove(updatedAgent);
            this.agentValueWeights.entrySet().removeIf(entry -> entry.getKey().getAgent().equals(updatedAgent));
        }
        setEditing(false);
        notifyAVObservers();
    }

    @Override
    public void updateValue(Value updatedValue) {
        if (!this.values.contains(updatedValue)) {
            this.values.add(updatedValue);
            addAgentValuesForValue(updatedValue);
        } else {
            this.values.remove(updatedValue);
            this.agentValueWeights.entrySet().removeIf(entry -> entry.getKey().getValue().equals(updatedValue));
        }
        setEditing(false);
        notifyAVObservers();
    }

    private void addAgentValuesForAgent(Agent agent) {
        if (!this.values.isEmpty()){
            for (Value value : this.values) {
                addAgentValue(agent, value);
            }
        }
    }

    private void addAgentValuesForValue(Value value) {
        if (!this.agents.isEmpty()){
            for (Agent agent : this.agents) {
                addAgentValue(agent, value);
            }
        }
    }

    private void addAgentValue(Agent agent, Value value) {
        AgentValue agentValue = new AgentValue(agent, value);
        if (!this.agentValueWeights.containsKey(agentValue)) {
            this.agentValueWeights.put(agentValue, new Weight(scale, "?"));
        }
    }

    public void editWeight(AgentValue agentValue, double newWeight) {
        Weight weight = agentValueWeights.get(agentValue);
        if (weight != null && newWeight >= scale.getMin() && newWeight <= scale.getMax()) {
            weight.setWeight(newWeight);
            setEditing(true);
            notifyAVObservers();
        }
    }

    @Override
    public void addAVObserver(AVObserver observer) {
        weightObservers.add(observer);
    }

    @Override
    public void removeAVObserver(AVObserver observer) {
        weightObservers.remove(observer);
    }

    @Override
    public void notifyAVObservers() {
        for (AVObserver observer : weightObservers) {
            observer.updateAV(this);
        }
    }

    @Override
    public void updateScale(Scale updatedScale) {
        this.scale = updatedScale;
        agentValueWeights.values().forEach(this::updateWeightAccordingToScale);
    }

    @Override
    protected void updateAllWeightsAccordingToScale() {
        agentValueWeights.values().forEach(this::updateWeightAccordingToScale);
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

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }
}