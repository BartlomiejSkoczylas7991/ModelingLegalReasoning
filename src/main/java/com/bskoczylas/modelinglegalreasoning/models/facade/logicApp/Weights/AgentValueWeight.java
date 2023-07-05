package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Weights.Scale_Weight;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Value.Value;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Weights.Scale_Weight.AV.AgentValue;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.AV_Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AgentValueWeight {
    protected Map<AgentValue, Weight> agent_value_weights;
    protected List<Agent> agents;
    protected List<Value> values;
    protected final List<AV_Observer> weightObservers = new ArrayList<>();
    protected Scale scale;
    protected boolean isEditing = false;

    // wspólne metody

    public abstract void addValue(Agent agent, Value value, Weight weight);

    public abstract Weight getWeight(AgentValue agentValue);

    // reszta metod abstrakcyjnych lub ogólnych
}
