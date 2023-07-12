package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;

import java.util.List;

public abstract class AgentValueWeight {
    // Common fields
    protected List<Agent> agents;
    protected List<Value> values;
    protected Scale scale;

    // Common methods
    public abstract void addAgent(Agent agent);
    public abstract void addValue(Value value);
    public abstract void setScale(Scale scale);

    public abstract Weight getWeight(AgentValue agentValue);

    public void updateScale(Scale updatedScale) {
        this.scale = updatedScale;
        updateAllWeightsAccordingToScale();
    }

    protected abstract void updateAllWeightsAccordingToScale();
}
