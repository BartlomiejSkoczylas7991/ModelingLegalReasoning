package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.observers.AgentObserver;
import com.bskoczylas.modelinglegalreasoning.models.observers.PropositionObserver;
import com.bskoczylas.modelinglegalreasoning.models.observers.ValueObserver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class AgentValuePropWeight implements AgentObserver, ValueObserver, PropositionObserver {
    private HashMap<AgentValueProposition, Weight> agent_value_prop_weights;
    private List<Agent> agents;
    private List<Value> values;
    private List<Proposition> propositions;

    private Scale scale;

    public AgentValuePropWeight() {
        this.agent_value_prop_weights = new HashMap<>();
        this.agents = new LinkedList<Agent>();
        this.values = new LinkedList<Value>();
        this.propositions = new LinkedList<Proposition>();
        this.scale = new Scale();
    }

    public AgentValuePropWeight(HashMap<AgentValueProposition, Weight> agent_value_prop_weights, List<Agent> agents, List<Value> values, List<Proposition> propositions, Scale scale) {
        this.agent_value_prop_weights = agent_value_prop_weights;
        this.agents = agents;
        this.values = values;
        this.propositions = propositions;
        this.scale = scale;
    }


    public void addValue(Agent agent, Value value, Proposition prop, Weight weight) {
        AgentValueProposition agentValueProp = new AgentValueProposition(agent, value, prop);
        this.agent_value_prop_weights.put(agentValueProp, weight);
    }

    public Weight getWeight(Agent agent, Value value, Proposition prop) {
        AgentValueProposition agentValueProp = new AgentValueProposition(agent, value, prop);
        return this.agent_value_prop_weights.get(agentValueProp);
    }

    public HashMap<AgentValueProposition, Weight> getAgent_value_prop_weights() {
        return agent_value_prop_weights;
    }

    public void setAgent_value_prop_weights(HashMap<AgentValueProposition, Weight> agent_value_prop_weights) {
        this.agent_value_prop_weights = agent_value_prop_weights;
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
    public String toString() {
        for (Agent agent : this.agents) {
            for (Value value : this.values) {
                for (Proposition proposition : this.propositions) {
                    AgentValueProposition agentValueProposition = new AgentValueProposition(agent, value, proposition);
                    if (!this.agent_value_prop_weights.containsKey(agentValueProposition)) {
                        this.agent_value_prop_weights.put(agentValueProposition, new Weight(scale, "?")); // Domyślna wartość "?" dla nowych wag
                    }
                }
            }
        }

        StringBuilder weightsStr = new StringBuilder("{");
        for (var entry : this.agent_value_prop_weights.entrySet()) {
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
            for (Value value : this.values) {
                for (Proposition proposition : this.propositions) {
                    AgentValueProposition agentValueProposition = new AgentValueProposition(updatedAgent, value, proposition);
                    if (!this.agent_value_prop_weights.containsKey(agentValueProposition)) {
                        this.agent_value_prop_weights.put(agentValueProposition, new Weight(scale, "?"));
                    }
                }
            }

        } else { // Jeśli agent jest już na liście, zakładamy, że jest usuwany
            this.agents.remove(updatedAgent);

            // Usuwamy wszystkie pary agent-wartość związane z tym agentem
            this.agent_value_prop_weights.entrySet().removeIf(entry -> entry.getKey().getAgent().equals(updatedAgent));
        }
    }

    @Override
    public void updateProposition(Proposition updatedProposition) {
        // Jeśli agent jest nowy i nie jest jeszcze na liście, dodajemy go
        if (!this.propositions.contains(updatedProposition)) {
            this.propositions.add(updatedProposition);

            // Dodajemy nowe pary agent-wartość do mapy wag
            for (Agent agent : this.agents) {
                for (Value value : this.values) {
                    AgentValueProposition agentValueProposition = new AgentValueProposition(agent, value, updatedProposition);
                    if (!this.agent_value_prop_weights.containsKey(agentValueProposition)) {
                        this.agent_value_prop_weights.put(agentValueProposition, new Weight(scale, "?"));
                    }
                }
            }

        } else { // Jeśli agent jest już na liście, zakładamy, że jest usuwany
            this.propositions.remove(updatedProposition);

            // Usuwamy wszystkie pary agent-wartość związane z tym agentem
            this.agent_value_prop_weights.entrySet().removeIf(entry -> entry.getKey().getProposition().equals(updatedProposition));
        }
    }

    @Override
    public void updateValue(Value updatedValue) {
        if (!this.values.contains(updatedValue)) {
            this.values.add(updatedValue);

            // Dodajemy nowe pary agent-wartość do mapy wag
            for (Agent agent : this.agents) {
                for (Proposition proposition : this.propositions) {
                    AgentValueProposition agentValueProposition = new AgentValueProposition(agent, updatedValue, proposition);
                    if (!this.agent_value_prop_weights.containsKey(agentValueProposition)) {
                        this.agent_value_prop_weights.put(agentValueProposition, new Weight(scale, "?"));
                    }
                }
            }

        } else { // Jeśli agent jest już na liście, zakładamy, że jest usuwany
            this.values.remove(updatedValue);

            // Usuwamy wszystkie pary agent-wartość związane z tym agentem
            this.agent_value_prop_weights.entrySet().removeIf(entry -> entry.getKey().getProposition().equals(updatedValue));
        }
    }
}