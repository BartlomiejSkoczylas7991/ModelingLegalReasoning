package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AgentValuePropositionTest {
    @Test
    void testGetAndSetAgent() {
        Agent agent = new Agent("Alice");
        Value value = new Value("Truth");
        Proposition proposition = new Proposition("Statement");

        AgentValueProposition avp = new AgentValueProposition(agent, value, proposition);
        assertEquals(agent, avp.getAgent());

        Agent newAgent = new Agent("Bob");
        avp.setAgent(newAgent);
        assertEquals(newAgent, avp.getAgent());
    }

    @Test
    void testGetAndSetValue() {
        Agent agent = new Agent("Alice");
        Value value = new Value("Truth");
        Proposition proposition = new Proposition("Statement");

        AgentValueProposition avp = new AgentValueProposition(agent, value, proposition);
        assertEquals(value, avp.getValue());

        Value newValue = new Value("Freedom");
        avp.setValue(newValue);
        assertEquals(newValue, avp.getValue());
    }

    @Test
    void testGetAndSetProposition() {
        Agent agent = new Agent("Alice");
        Value value = new Value("Truth");
        Proposition proposition = new Proposition("Statement");

        AgentValueProposition avp = new AgentValueProposition(agent, value, proposition);
        assertEquals(proposition, avp.getProposition());

        Proposition newProposition = new Proposition("New Statement");
        avp.setProposition(newProposition);
        assertEquals(newProposition, avp.getProposition());
    }

    @Test
    void testEqualsAndHashCode() {
        Agent agent = new Agent("Alice");
        Value value = new Value("Truth");
        Proposition proposition = new Proposition("Statement");

        AgentValueProposition avp1 = new AgentValueProposition(agent, value, proposition);
        AgentValueProposition avp2 = new AgentValueProposition(agent, value, proposition);

        assertTrue(avp1.equals(avp2) && avp2.equals(avp1));
        assertEquals(avp1.hashCode(), avp2.hashCode());
    }

    @Test
    void testToString() {
        Agent agent = new Agent("Alice");
        Value value = new Value("Truth");
        Proposition proposition = new Proposition("Statement");

        AgentValueProposition avp = new AgentValueProposition(agent, value, proposition);

        String expectedString = "(" + agent + ", " + value + ", " + proposition + ")";
        assertEquals(expectedString, avp.toString());
    }
}