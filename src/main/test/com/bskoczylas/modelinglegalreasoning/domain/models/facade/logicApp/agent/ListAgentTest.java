package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AgentObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ListAgentTest {
    private AgentObserver mockObserver;
    private ListAgent listAgent;

    @BeforeEach
    public void setUp() {
        mockObserver = Mockito.mock(AgentObserver.class);
        listAgent = new ListAgent();
        listAgent.addAgentObserver(mockObserver);
    }

    @Test
    void testGetAgents() {
        assertTrue(listAgent.getAgents().isEmpty());
    }

    @Test
    void addAgents() {
        Agent agent = new Agent("Agent 1");
        listAgent.addAgent(agent);
        assertEquals(Collections.singletonList(agent), listAgent.getAgents());
        verify(mockObserver).updateAgent(listAgent);
    }

    @Test
    void removeAgents() {
        Agent agent1 = new Agent("Agent 3");
        Agent agent2 = new Agent("Agent 4");
        List<Agent> agents = Arrays.asList(agent1, agent2);
        listAgent.addAgents(agents);
        listAgent.removeAgents(agents);
        assertFalse(listAgent.getAgents().contains(agent1), "Agent 3 should be removed");
        assertFalse(listAgent.getAgents().contains(agent2), "Agent 4 should be removed");
    }

    @Test
    void setListAgent() {
        Agent agent1 = new Agent("Agent 5");
        Agent agent2 = new Agent("Agent 6");
        List<Agent> newAgents = new ArrayList<>();
        newAgents.add(agent1);
        newAgents.add(agent2);
        listAgent.setListAgent(newAgents);
        assertEquals(newAgents, listAgent.getAgents(), "List of agents should be updated");
    }

    @Test
    public void testAgentEquals() {
        Agent agent1 = new Agent("John");
        Agent agent2 = new Agent("John");
        assertEquals(agent1, agent2);
    }

    @Test
    public void testObserverRegistration() {
        AgentObserver observer = mock(AgentObserver.class);
        ListAgent listAgent = new ListAgent();
        listAgent.addAgentObserver(observer);
        assertTrue(listAgent.getObservers().contains(observer));
        listAgent.removeAgentObserver(observer);
        assertFalse(listAgent.getObservers().contains(observer));
    }

    @Test
    public void testNotifyObservers() {
        AgentObserver observer = mock(AgentObserver.class);
        ListAgent listAgent = new ListAgent();
        listAgent.addAgentObserver(observer);
        Agent agent = new Agent("John");
        listAgent.addAgent(agent);
        verify(observer, times(1)).updateAgent(listAgent);
    }

    @Test
    void testToString() {
        Agent agent1 = new Agent("Agent 1");
        Agent agent2 = new Agent("Agent 2");
        listAgent.addAgents(Arrays.asList(agent1, agent2));

        String result = listAgent.toString();

        assertEquals("Agents = {Agent 1, Agent 2}", result);
    }
}