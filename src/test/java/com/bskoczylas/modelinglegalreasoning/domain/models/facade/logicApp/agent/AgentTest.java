package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class AgentTest {
    @Test
    public void testConstructors() {
        Agent agent = new Agent("Agent Name");
        assertEquals("Agent Name", agent.getName());
        assertNotNull(agent.getCreated());
    }

    @Test
    public void testGettersAndSetters() {
        Agent agent = new Agent("Original Name");
        agent.setName("New Name");
        agent.setCreated(LocalDateTime.of(2023, 8, 14, 0, 0));

        assertEquals("New Name", agent.getName());
        assertEquals(LocalDateTime.of(2023, 8, 14, 0, 0), agent.getCreated());
    }

    @Test
    public void testFormattedCreated() {
        LocalDateTime created = LocalDateTime.of(2023, 8, 14, 10, 30);
        Agent agent = new Agent("Agent Name");
        agent.setCreated(created);

        assertEquals("2023-08-14 10:30:00", agent.getFormattedCreated());
    }

    @Test
    public void testEqualsAndHashCode() {
        Agent agent1 = new Agent("Agent Name");
        Agent agent2 = new Agent("Agent Name");
        Agent agent3 = new Agent("Different Name");

        assertEquals(agent1, agent2);
        assertNotEquals(agent1, agent3);
        assertEquals(agent1.hashCode(), agent2.hashCode());
        assertNotEquals(agent1.hashCode(), agent3.hashCode());
    }

    @Test
    public void testToString() {
        Agent agent = new Agent("Agent Name");
        assertEquals("Agent Name", agent.toString());
    }
}