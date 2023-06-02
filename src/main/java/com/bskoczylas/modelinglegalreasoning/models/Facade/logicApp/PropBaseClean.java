package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Objects.Agent;

import java.util.*;

public class PropBaseClean {
    private HashMap<Agent, Set<Proposition>> propBaseClean;

    public PropBaseClean(List<Agent> agents, List<Value> values, List<Proposition> props,
                         AgentValueToWeight agentValueToWeight, AgentValuePropWeight agentValuePropWeight) {
        this.propBaseClean = calculatePropBaseClean(agents, values, props, agentValueToWeight, agentValuePropWeight);
    }

    private HashMap<Agent, Set<Proposition>> calculatePropBaseClean(List<Agent> agents, List<Value> values,
                                                                    List<Proposition> props,
                                                                    AgentValueToWeight agentValueToWeight,
                                                                    AgentValuePropWeight agentValuePropWeight) {
        HashMap<Agent, Set<Proposition>> propBaseClean = new HashMap<>();
        for (Agent agent : agents) {
            Set<Proposition> agentProps = new HashSet<>();
            for (Proposition prop : props) {
                boolean passFilter = false;
                for (Value value : values) {
                    // TODO: add case when value is "?"
                    Weight weightAgentValueProp = agentValuePropWeight.getWeight(agent, value, prop);
                    Weight weightAgentValueTo = agentValueToWeight.getWeight(agent, value);
                    if (weightAgentValueProp != null && weightAgentValueTo != null &&
                            weightAgentValueProp.compareTo(weightAgentValueTo) >= 0) {
                        passFilter = true;
                        break;
                    }
                }
                if (passFilter) {
                    agentProps.add(prop);
                }
            }
            propBaseClean.put(agent, agentProps);
        }
        return propBaseClean;
    }

    public static Set<String> getSetStatementProp(Agent agent, HashMap<Agent, Set<Proposition>> propBaseClean) {
        Set<String> propStatements = new HashSet<>();
        Set<Proposition> propositions = propBaseClean.get(agent);
        if (propositions != null) {
            for (Proposition prop : propositions) {
                propStatements.add(prop.getStatement());
            }
        }
        return propStatements;
    }

    public HashMap<Agent, Set<Proposition>> getPropBaseClean() {
        return propBaseClean;
    }

    public Set<Proposition> getPropositions(Agent agent){
        return propBaseClean.get(agent);
    }
}