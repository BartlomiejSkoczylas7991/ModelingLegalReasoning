package com.bskoczylas.modelinglegalreasoning.adapters;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.IncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.weights.AgentValueKeyDeserializer;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.weights.AgentValuePropositionKeyDeserializer;
import com.bskoczylas.modelinglegalreasoning.repositories.deserializers.weights.WeightDeserializer;
import com.bskoczylas.modelinglegalreasoning.repositories.serializers.weights.AgentValueKeySerializer;
import com.bskoczylas.modelinglegalreasoning.repositories.serializers.weights.AgentValuePropositionKeySerializer;
import com.bskoczylas.modelinglegalreasoning.repositories.serializers.weights.WeightSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ProjectData {
    private static int nextId = 1;
    private int id;
    private String name;
    private List<Agent> agents;
    private List<Value> values;
    private List<Proposition> propositions;
    private List<Rule> rules;
    private List<IncompProp> incompProps;
    private Pair<Proposition, Proposition> decisions;
    @JsonSerialize(keyUsing = AgentValueKeySerializer.class, contentUsing = WeightSerializer.class)
    @JsonDeserialize(keyUsing = AgentValueKeyDeserializer.class, contentUsing = WeightDeserializer.class)
    private Map<AgentValue, Weight> agentValueWeightHashMap;
    @JsonSerialize(keyUsing = AgentValuePropositionKeySerializer.class, contentUsing = WeightSerializer.class)
    @JsonDeserialize(keyUsing = AgentValuePropositionKeyDeserializer.class, contentUsing = WeightDeserializer.class)
    private Map<AgentValueProposition, Weight> agentValuePropositionWeightHashMap;
    private Scale scale;
    private LocalDateTime created;
    private LocalDateTime modified;

    public ProjectData() {}

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

    public List<Rule> getRules() {
        return rules;
    }

    public void setRules(List<Rule> rules) {
        this.rules = rules;
    }

    public List<IncompProp> getIncompProps() {
        return incompProps;
    }

    public void setIncompProps(List<IncompProp> incompProps) {
        this.incompProps = incompProps;
    }

    public Pair<Proposition, Proposition> getDecisions() {
        return decisions;
    }

    public void setDecisions(Pair<Proposition, Proposition> decisions) {
        this.decisions = decisions;
    }

    public Map<AgentValue, Weight> getAgentValueWeightHashMap() {
        return agentValueWeightHashMap;
    }

    public void setAgentValueWeightHashMap(Map<AgentValue, Weight> agentValueWeightHashMap) {
        this.agentValueWeightHashMap = agentValueWeightHashMap;
    }

    public Map<AgentValueProposition, Weight> getAgentValuePropositionWeightHashMap() {
        return agentValuePropositionWeightHashMap;
    }

    public void setAgentValuePropositionWeightHashMap(Map<AgentValueProposition, Weight> agentValuePropositionWeightHashMap) {
        this.agentValuePropositionWeightHashMap = agentValuePropositionWeightHashMap;
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getModified() {
        return modified;
    }

    public void setModified(LocalDateTime modified) {
        this.modified = modified;
    }
}
