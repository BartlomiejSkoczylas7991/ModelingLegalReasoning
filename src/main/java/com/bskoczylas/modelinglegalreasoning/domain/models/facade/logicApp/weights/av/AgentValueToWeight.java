package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.AVObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AVObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.ListValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.AVObserverController;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AgentObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ScaleObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ValueObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable.AVObservableController;

import java.util.*;

public class AgentValueToWeight extends HashMap<AgentValue, Weight> implements AgentObserver, ValueObserver, AVObservable, ScaleObserver {
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

    public AgentValueToWeight(HashMap<Object, Object> objectObjectHashMap, List<Agent> agents, List<Value> values, Scale scale) {
    }

    public Set<AgentValue> keySet() {
        return agentValueWeights.keySet();
    }

    public void addAgent(Agent agent) {
        agents.add(agent);
    }

    public void addValue(Value value) {
        values.add(value);
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public Map<AgentValue, Weight> getAgentValueWeights() {
        return agentValueWeights;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgentValueWeights(Map<AgentValue, Weight> agentValueWeights) {
        this.agentValueWeights = agentValueWeights;
        notifyAVObservers();
    }

    public void addValue(Agent agent, Value value, Weight weight) {
        AgentValue agentValue = new AgentValue(agent, value);
        this.agentValueWeights.put(agentValue, weight);
        notifyAVObservers();
    }

    public Weight getWeight(AgentValue agentValue) {
        return this.agentValueWeights.get(agentValue);
    }

    @Override
    public String toString() {
        StringBuilder weightsStr = new StringBuilder("{");
        for (var entry : this.agentValueWeights.entrySet()) {
            weightsStr.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        weightsStr.append("}");
        return weightsStr.toString();
    }

    @Override
    public void updateAgent(ListAgent listAgent) {
        List<Agent> updatedAgents = listAgent.getAgents();

        // Znajdź agentów, które zostały usunięte
        List<Agent> removedAgents = new ArrayList<>(this.agents);
        removedAgents.removeAll(updatedAgents);
        // Usuń związane z nimi AgentValues
        for (Agent agent : removedAgents) {
            this.agents.remove(agent);
            this.agentValueWeights.entrySet().removeIf(entry -> entry.getKey().getAgent().equals(agent));
        }

        // Znajdź nowo dodane agentów
        List<Agent> addedAgents = new ArrayList<>(updatedAgents);
        addedAgents.removeAll(this.agents);
        // Dodaj związane z nimi AgentValues
        for (Agent agent : addedAgents) {
            this.agents.add(agent);
            addAgentValuesForAgent(agent);
        }

        setEditing(false);
        notifyAVObservers();
    }

    @Override
    public void updateValue(ListValue listValue) {
        List<Value> updatedValues = listValue.getValues();
        // Znajdź wartosci, które zostały usunięte
        List<Value> removedValues = new ArrayList<>(this.values);
        removedValues.removeAll(updatedValues);
        // Usuń związane z nimi AgentValues
        for (Value value : removedValues) {
            this.values.remove(value);
            this.agentValueWeights.entrySet().removeIf(entry -> entry.getKey().getValue().equals(value));
        }

        // Znajdź nowo dodane wartości
        List<Value> addedValues = new ArrayList<>(updatedValues);
        addedValues.removeAll(this.values);
        // Dodaj związane z nimi AgentValues
        for (Value value : addedValues) {
            this.values.add(value);
            addAgentValuesForValue(value);
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

    public void editWeight(AgentValue agentValue, Integer newWeight) {
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

    protected void updateAllWeightsAccordingToScale() {
        agentValueWeights.values().forEach(this::updateWeightAccordingToScale);
    }

    private void updateWeightAccordingToScale(Weight weight) {
        if (weight.getWeight().equals("?")) {
            weight.setWeight(scale.getMax());
        } else {
            Integer weightValue = (Integer) weight.getWeight();
            if (!scale.contains(weightValue)) {
                adjustWeightToScaleBounds(weight, weightValue);
            }
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

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public List<AVObserver> getWeightObservers() {
        return weightObservers;
    }

    public List<Value> getValues() {
        return values;
    }

    public Scale getScale() {
        return scale;
    }
}