package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.observers.AgentObserver;
import com.bskoczylas.modelinglegalreasoning.models.observers.ValueObserver;

import java.util.*;

public class AgentValueToWeight implements AgentObserver, ValueObserver {
    private HashMap<AgentValue, Weight> agent_value_weights;
    private List<Agent> agents;
    private List<Value> values;

    private Scale scale;

    public AgentValueToWeight() {
        this.agent_value_weights = new HashMap<>();
        this.agents = new LinkedList<Agent>();
        this.values = new LinkedList<Value>();
    }

    public AgentValueToWeight(HashMap<AgentValue, Weight> agent_value_weights, List<Agent> agents, List<Value> values, Scale scale) {
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

    public void addValue(Agent agent, Value value, Weight weight) {
        AgentValue agentValue = new AgentValue(agent, value);
        this.agent_value_weights.put(agentValue, weight);
    }

    public Weight getWeight(Agent agent, Value value) {
        AgentValue agentValue = new AgentValue(agent, value);
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
        // Jeśli agent jest nowy i nie jest jeszcze na liście, dodajemy go
        if (!this.agents.contains(updatedAgent)) {
            this.agents.add(updatedAgent);

            // Dodajemy nowe pary agent-wartość do mapy wag
            if (!this.values.isEmpty()){
                for (Value value : this.values) {
                    AgentValue agentValue = new AgentValue(updatedAgent, value);
                    if (!this.agent_value_weights.containsKey(agentValue)) {
                        this.agent_value_weights.put(agentValue, new Weight(scale, "?"));
                    }
                }
            }

        } else { // Jeśli agent jest już na liście, zakładamy, że jest usuwany
            this.agents.remove(updatedAgent);

            // Usuwamy wszystkie pary agent-wartość związane z tym agentem
            this.agent_value_weights.entrySet().removeIf(entry -> entry.getKey().getAgent().equals(updatedAgent));
        }
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

        } else { // Jeśli agent jest już na liście, zakładamy, że jest usuwany
            this.values.remove(updatedValue);

            // Usuwamy wszystkie pary agent-wartość związane z tym agentem
            this.agent_value_weights.entrySet().removeIf(entry -> entry.getKey().getValue().equals(updatedValue));
        }
    }
}