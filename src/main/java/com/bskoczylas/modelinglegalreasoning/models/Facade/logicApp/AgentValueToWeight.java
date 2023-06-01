package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Objects.Agent;
import com.bskoczylas.modelinglegalreasoning.models.observers.AgentObserver;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AgentValueToWeight implements AgentObserver {
    private HashMap<AgentValue, Weight> agent_value_weights;
    private List<Agent> agents;
    private List<Value> values;

    private Scale scale;

    public AgentValueToWeight() {
        this.agent_value_weights = new HashMap<>();
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
    public void update() {
        // Tworzymy kopię mapy, aby uniknąć błędów związanych z modyfikacją mapy podczas iteracji.
        HashMap<AgentValue, Weight> copyMap = new HashMap<>(this.agent_value_weights);

        for (Map.Entry<AgentValue, Weight> entry : copyMap.entrySet()) {
            AgentValue agentValue = entry.getKey();
            Agent agent = agentValue.getAgent();
            Value value = agentValue.getValue();

            // Jeśli agenta nie ma na liście agentów, usuwamy odpowiedni wpis.
            if (!this.agents.contains(agent)) {
                this.agent_value_weights.remove(agentValue);
            }
            if (!this.values.contains(value)) {
                this.agent_value_weights.remove(agentValue);
            }
        }

        // Dodajemy nowe pary agent-wartość do mapy, jeżeli jeszcze ich nie ma.
        for (Agent agent : this.agents) {
            for (Value value : this.values) {
                AgentValue agentValue = new AgentValue(agent, value);
                if (!this.agent_value_weights.containsKey(agentValue)) {
                    this.agent_value_weights.put(agentValue, new Weight(scale, "?")); // Domyślna wartość "?" dla nowych wag
                }
            }
        }
    }
}