package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AVPObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AgentObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.propBaseClean.ListPropBaseClean;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AgentValuePropWeightTest {
    @Test
    public void testConstructorInitialization() {
        AgentValuePropWeight avpw = new AgentValuePropWeight();

        assertNotNull(avpw.getAgentValuePropWeights());
        assertEquals(0, avpw.getAgentValuePropWeights().size());

        assertNotNull(avpw.getAgents());
        assertEquals(0, avpw.getAgents().size());

        assertNotNull(avpw.getValues());
        assertEquals(0, avpw.getValues().size());

        assertNotNull(avpw.getPropositions());
        assertEquals(0, avpw.getPropositions().size());

        assertNotNull(avpw.getScale());
    }





}