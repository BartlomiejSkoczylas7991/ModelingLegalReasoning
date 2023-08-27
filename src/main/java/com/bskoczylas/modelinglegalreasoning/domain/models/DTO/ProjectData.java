package com.bskoczylas.modelinglegalreasoning.domain.models.DTO;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.IncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProjectData implements Serializable {
    private List<Agent> agents;
    private List<Value> values;
    private List<Proposition> propositions;
    private List<IncompProp> incompPropList;
    private Scale scale;
    private Map<AgentValue, Weight> agentValueToWeight;
    private Map<AgentValueProposition, Weight> agentValuePropWeight;
    private List<Rule> listRule;

    public ProjectData(List<Agent> agents, List<Value> values, List<Proposition> propositions, List<IncompProp> incompPropList, Scale scale, Map<AgentValue, Weight> agentValueToWeight, Map<AgentValueProposition, Weight> agentValuePropWeight, List<Rule> listRule) {
        this.agents = agents;
        this.values = values;
        this.propositions = propositions;
        this.incompPropList = incompPropList;
        this.scale = scale;
        this.agentValueToWeight = agentValueToWeight;
        this.agentValuePropWeight = agentValuePropWeight;
        this.listRule = listRule;
    }

    public ProjectData() {}

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public List<Value> getValues() {
        return values;
    }

    public List<Proposition> getPropositions() {
        return propositions;
    }

    public List<IncompProp> getIncompPropList() {
        return incompPropList;
    }

    public Map<AgentValue, Weight> getAgentValueToWeight() {
        return agentValueToWeight;
    }

    public Map<AgentValueProposition, Weight> getAgentValuePropWeight() {
        return agentValuePropWeight;
    }

    public List<Rule> getListRule() {
        return listRule;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    public void setValues(List<Value> values) {
        this.values = values;
    }

    public void setPropositions(List<Proposition> propositions) {
        this.propositions = propositions;
    }

    public void setIncompPropList(List<IncompProp> incompPropList) {
        this.incompPropList = incompPropList;
    }

    public void setAgentValueToWeight(Map<AgentValue, Weight> agentValueToWeight) {
        this.agentValueToWeight = agentValueToWeight;
    }

    public void setAgentValuePropWeight(Map<AgentValueProposition, Weight> agentValuePropWeight) {
        this.agentValuePropWeight = agentValuePropWeight;
    }

    public void setListRule(List<Rule> listRule) {
        this.listRule = listRule;
    }
}
