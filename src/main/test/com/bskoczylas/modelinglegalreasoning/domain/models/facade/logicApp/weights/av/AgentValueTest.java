package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AgentValueTest {
    @Test
    void testConstructor() {
        Agent agent = new Agent("Alice");
        Value value = new Value("Truth"); // Ustal wartość według konstruktora klasy Value
        AgentValue agentValue = new AgentValue(agent, value);
        assertEquals(agent, agentValue.getAgent());
        assertEquals(value, agentValue.getValue());
    }

    @Test
    void testSetAgent() {
        Agent agent1 = new Agent("Alice");
        Agent agent2 = new Agent("Bob");
        Value value = new Value("Truth"); // Ustal wartość według konstruktora klasy Value
        AgentValue agentValue = new AgentValue(agent1, value);
        agentValue.setAgent(agent2);
        assertEquals(agent2, agentValue.getAgent());
    }

    @Test
    void testSetValue() {
        Agent agent = new Agent("Alice");
        Value value1 = new Value("Honor");
        Value value2 = new Value("Liberty");
        AgentValue agentValue = new AgentValue(agent, value1);
        agentValue.setValue(value2);
        assertEquals(value2, agentValue.getValue());
    }

    @Test
    void testEqualsAndHashCode() {
        Agent agent1 = new Agent("Alice");
        Value value1 = new Value("Truth"); // Ustal wartość według konstruktora klasy Value
        AgentValue agentValue1 = new AgentValue(agent1, value1);

        Agent agent2 = new Agent("Alice");
        Value value2 = new Value("Truth"); // Ustal tę samą wartość co value1
        AgentValue agentValue2 = new AgentValue(agent2, value2);

        assertTrue(agentValue1.equals(agentValue2) && agentValue2.equals(agentValue1));
        assertEquals(agentValue1.hashCode(), agentValue2.hashCode());
    }

    @Test
    void testToString() {
        Agent agent = new Agent("Alice");
        Value value = new Value("Power"); // Ustal wartość według konstruktora klasy Value
        AgentValue agentValue = new AgentValue(agent, value);
        String expectedString = "(" + agent + ", " + value + ")";
        assertEquals(expectedString, agentValue.toString());
    }
}