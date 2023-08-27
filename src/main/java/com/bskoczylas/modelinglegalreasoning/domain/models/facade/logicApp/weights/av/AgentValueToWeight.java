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

import java.io.Serializable;
import java.util.*;

public class AgentValueToWeight implements Serializable, AgentObserver, ValueObserver, AVObservable, ScaleObserver {
    private Map<AgentValue, Weight> agentValueWeights;
    private List<Agent> agents;
    private List<Value> values;
    private final List<AVObserver> weightObservers = new ArrayList<>();
    private Scale scale;

    public AgentValueToWeight() {
        this.agentValueWeights = new HashMap<>();
        this.agents = new LinkedList<>();
        this.values = new LinkedList<>();
        this.scale = new Scale();
    }

    public Weight getWeight(AgentValue agentValue) {
        return this.agentValueWeights.get(agentValue);
    }

    public Set<AgentValue> keySet() {
        return agentValueWeights.keySet();
    }

    public void addWeight(Agent agent, Value value, Weight weight) {
        AgentValue agentValue = new AgentValue(agent, value);
        this.agentValueWeights.put(agentValue, weight);
        notifyAVObservers();
    }

    public void setScale(Scale scale) {
        this.scale = scale;
        agentValueWeights.values().forEach(this::updateWeightAccordingToScale);
        notifyAVObservers();
    }

    public Map<AgentValue, Weight> getAgentValueWeights() {
        return agentValueWeights;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public List<Value> getValues() {
        return values;
    }

    public Scale getScale() {
        return scale;
    }

    public void setAgentValueWeights(Map<AgentValue, Weight> agentValueWeights) {
        this.agentValueWeights = agentValueWeights;
        notifyAVObservers();
    }

    @Override
    public void updateAgent(ListAgent listAgent) {
        List<Agent> updatedAgents = listAgent.getAgents();
        boolean changed = false; // Flaga do śledzenia, czy coś się zmieniło

        // Znajdź agentów, które zostały usunięte
        List<Agent> removedAgents = new ArrayList<>(this.agents);
        removedAgents.removeAll(updatedAgents);
        if (!removedAgents.isEmpty()) {
            changed = true;
            // Usuń związane z nimi AgentValues
            for (Agent agent : removedAgents) {
                this.agents.remove(agent);
                this.agentValueWeights.entrySet().removeIf(entry -> entry.getKey().getAgent().equals(agent));
            }
        }

        // Znajdź nowo dodane agentów
        List<Agent> addedAgents = new ArrayList<>(updatedAgents);
        addedAgents.removeAll(this.agents);
        if (!addedAgents.isEmpty()) {
            changed = true;
            // Dodaj związane z nimi AgentValues
            for (Agent agent : addedAgents) {
                this.agents.add(agent);
                addAgentValuesForAgent(agent);
            }
        }

        if (changed && !this.agentValueWeights.isEmpty()) { // Powiadamiaj tylko wtedy, gdy coś się zmieniło i agentValueWeights nie jest pusty
            notifyAVObservers();
        }
    }

    @Override
    public void updateValue(ListValue listValue) {
        List<Value> updatedValues = listValue.getValues();
        boolean changed = false; // Flaga do śledzenia, czy coś się zmieniło

        // Znajdź wartosci, które zostały usunięte
        List<Value> removedValues = new ArrayList<>(this.values);
        removedValues.removeAll(updatedValues);
        if (!removedValues.isEmpty()) {
            changed = true;
            // Usuń związane z nimi AgentValues
            for (Value value : removedValues) {
                this.values.remove(value);
                this.agentValueWeights.entrySet().removeIf(entry -> entry.getKey().getValue().equals(value));
            }
        }

        // Znajdź nowo dodane wartości
        List<Value> addedValues = new ArrayList<>(updatedValues);
        addedValues.removeAll(this.values);
        if (!addedValues.isEmpty()) {
            changed = true;
            // Dodaj związane z nimi AgentValues
            for (Value value : addedValues) {
                this.values.add(value);
                addAgentValuesForValue(value);
            }
        }

        if (changed && !this.agentValueWeights.isEmpty()) { // Powiadamiaj tylko wtedy, gdy coś się zmieniło i agentValueWeights nie jest pusty
            notifyAVObservers();
        }
    }

    private void addAgentValuesForAgent(Agent agent) {
        if (!this.values.isEmpty()) {
            for (Value value : this.values) {
                addAgentValue(agent, value);
            }
        }
    }

    private void addAgentValuesForValue(Value value) {
        if (!this.agents.isEmpty()) {
            for (Agent agent : this.agents) {
                addAgentValue(agent, value);
            }
        }
    }

    private void addAgentValue(Agent agent, Value value) {
        AgentValue agentValue = new AgentValue(agent, value);
        if (!this.agentValueWeights.containsKey(agentValue)) {
            this.agentValueWeights.put(agentValue, Weight.indeterminate());
        }
    }

    public void editWeight(AgentValue agentValue, Integer newWeight) {
        if (newWeight != null && newWeight >= scale.getMin() && newWeight <= scale.getMax()) {
            this.agentValueWeights.put(agentValue, Weight.of(newWeight));
            notifyAVObservers();
        } else if (newWeight == null) {
            this.agentValueWeights.put(agentValue, Weight.indeterminate());
            notifyAVObservers();
        }
    }

    public void editWeight(Agent agent, Value value, Integer newWeight) {
        AgentValue agentValue = new AgentValue(agent, value);
        editWeight(agentValue, newWeight);
    }

    public boolean isEmpty() {
        return this.agentValueWeights.isEmpty();
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
        notifyAVObservers();
    }

    private void updateWeightAccordingToScale(Weight weight) {
        Integer weightValue = weight.getNumberValue();
        if (weightValue != null && !scale.contains(weightValue)) {
            adjustWeightToScaleBounds(weight, weightValue);
        }
    }

    private void adjustWeightToScaleBounds(Weight weight, Integer weightValue) {
        Map.Entry<AgentValue, Weight> entry = agentValueWeights.entrySet()
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
    public String toString() {
        StringBuilder weightsStr = new StringBuilder("{");
        for (var entry : this.agentValueWeights.entrySet()) {
            weightsStr.append(entry.getKey()).append(": ").append(entry.getValue().getWeight()).append(", ");
        }
        weightsStr.append("}");
        return weightsStr.toString();
    }
}