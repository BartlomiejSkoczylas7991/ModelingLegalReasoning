package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.AV;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Scale_Weight.Scale;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Value.Value;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Scale_Weight.Weight;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observables.AV_Observable;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.AV_Observer;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.AgentObserver;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.Scale_Observer;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.ValueObserver;

import java.util.*;

public class AgentValueToWeight implements AgentObserver, ValueObserver, AV_Observable, Scale_Observer {
    private Map<AgentValue, Weight> agent_value_weights;
    private List<Agent> agents;
    private List<Value> values;
    private final List<AV_Observer> weightObservers = new ArrayList<>();
    private Scale scale;
    private boolean isEditing = false;

    public AgentValueToWeight() {
        this.agent_value_weights = new HashMap<>();
        this.agents = new LinkedList<Agent>();
        this.values = new LinkedList<Value>();
    }

    public AgentValueToWeight(Map<AgentValue, Weight> agent_value_weights, List<Agent> agents, List<Value> values, Scale scale) {
        this.agent_value_weights = agent_value_weights;
        this.agents = agents;
        this.values = values;
        this.scale = scale;
    }

    public AgentValueToWeight(List<Agent> agents, List<Value> values) {
        this.agent_value_weights = new HashMap<>();
        this.agents = agents;
        this.values = values;
    }

    public Set<AgentValue> keySet() {
        return agent_value_weights.keySet();
    }

    public void addValue(Agent agent, Value value, Weight weight) {
        AgentValue agentValue = new AgentValue(agent, value);
        this.agent_value_weights.put(agentValue, weight);
    }

    public Weight getWeight(AgentValue agentValue) {
        return this.agent_value_weights.get(agentValue);
    }



    @Override
    public String toString() {
        for (Agent agent : this.agents) {
            for (Value value : this.values) {
                AgentValue agentValue = new AgentValue(agent, value);
                if (!this.agent_value_weights.containsKey(agentValue)) {
                    this.agent_value_weights.put(agentValue, new Weight(scale,"?")); // Domyślna wartość "?" dla nowych wag
                }
            }
        }

        StringBuilder weightsStr = new StringBuilder("{");
        for (var entry : this.agent_value_weights.entrySet()) {
            weightsStr.append(entry.getKey()).append(": ").append(entry.getValue()).append(", ");
        }
        weightsStr.append("}");
        return weightsStr.toString();
    }
    @Override
    public void updateAgent(Agent updatedAgent) {
        // If the agent is new and not yet listed, we add it
        if (!this.agents.contains(updatedAgent)) {
            this.agents.add(updatedAgent);

            // If an agent is already listed, we assume it is being removed
            if (!this.values.isEmpty()){
                for (Value value : this.values) {
                    AgentValue agentValue = new AgentValue(updatedAgent, value);
                    if (!this.agent_value_weights.containsKey(agentValue)) {
                        this.agent_value_weights.put(agentValue, new Weight(scale, "?"));
                    }
                }
            }

        } else { // If an agent is already listed, we assume it is being removed
            this.agents.remove(updatedAgent);

            // We remove all agent-value pairs associated with this agent
            this.agent_value_weights.entrySet().removeIf(entry -> entry.getKey().getAgent().equals(updatedAgent));
        }
        setEditing(false);
        notifyAVObservers();
    }


    @Override
    public void updateValue(Value updatedValue) {
        if (!this.values.contains(updatedValue)) {
            this.values.add(updatedValue);
            if (!this.agents.isEmpty()){
                for (Agent agent : this.agents) {
                    AgentValue agentValue = new AgentValue(agent, updatedValue);
                    if (!this.agent_value_weights.containsKey(agentValue)) {
                        this.agent_value_weights.put(agentValue, new Weight(scale, "?"));
                    }
                }
            }

        } else {
            this.values.remove(updatedValue);
            this.agent_value_weights.entrySet().removeIf(entry -> entry.getKey().getValue().equals(updatedValue));
        }
        setEditing(false);
        notifyAVObservers();
    }

    public void editWeight(AgentValue agentValue, double newWeight) {
        Weight weight = agent_value_weights.get(agentValue);
        if (weight != null && newWeight >= scale.getMin() && newWeight <= scale.getMax()) {
            weight.setWeight(newWeight);
            setEditing(true);
            notifyAVObservers();
        }
    }

    @Override
    public void addAVObserver(AV_Observer observer) {
        weightObservers.add(observer);
    }

    @Override
    public void removeAVObserver(AV_Observer observer) {
        weightObservers.remove(observer);
    }

    @Override
    public void notifyAVObservers() {
        for (AV_Observer observer : weightObservers) {
            observer.updateAV(this);
        }
    }

    @Override
    public void updateScale(Scale updatedScale) {
        this.scale = updatedScale;

        for (Map.Entry<AgentValue, Weight> entry : this.agent_value_weights.entrySet()) {
            Weight weight = entry.getValue();
            Integer weightValue = (Integer) weight.getWeight();

            if (weightValue.equals("?")) {
                // If the weight is undefined, we set it to the maximum value of the scale
                weight.setWeight(this.scale.getMax());
            } else if (!this.scale.contains(weightValue)) {
                if (weightValue < this.scale.getMin()) {
                    // If the weight is less than the minimum scale value, we set it to the minimum scale value
                    weight.setWeight(this.scale.getMin());
                } else {
                    // Otherwise, if the weight is greater than the maximum scale value, we set it to the maximum scale value
                    weight.setWeight(this.scale.getMax());
                }
            }
        }
    }

    public boolean isEditing() {
        return isEditing;
    }

    public void setEditing(boolean editing) {
        isEditing = editing;
    }
}