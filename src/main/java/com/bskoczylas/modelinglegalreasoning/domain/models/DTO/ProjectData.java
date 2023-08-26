package com.bskoczylas.modelinglegalreasoning.domain.models.DTO;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.IncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ProjectData {
    List<Agent> agents;
    List<Value> values;
    List<Proposition> propositions;
    List<IncompProp> incompPropList;
    Map<AgentValue, Weight> agentValueToWeight;
    Map<AgentValueProposition, Weight> agentValuePropWeight;

}
