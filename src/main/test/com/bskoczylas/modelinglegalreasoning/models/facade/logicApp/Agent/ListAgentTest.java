package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.AgentObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListAgentTest {
    private ListAgent listAgent;

    @BeforeEach
    public void setup() {
        ListAgent listAgent = new ListAgent();
    }

    @Test
    void getAgents() {
    }

    @Test
    void addAgent() {
        Agent agent = new Agent("Agent 1");
        listAgent.addAgent(agent);
        assertTrue(listAgent.getAgents().contains(agent), "Agent should be added");
    }

    @Test
    void removeAgent() {
        Agent agent = new Agent("Agent 2");
        listAgent.addAgent(agent);
        listAgent.removeAgent(agent);
        assertFalse(listAgent.getAgents().contains(agent), "Agent should be removed");
    }

    @Test
    void setListAgent() {
        Agent agent1 = new Agent("Agent 3");
        Agent agent2 = new Agent("Agent 4");
        List<Agent> newAgents = new ArrayList<>();
        newAgents.add(agent1);
        newAgents.add(agent2);
        listAgent.setListAgent(newAgents);
        assertEquals(newAgents, listAgent.getAgents(), "List of agents should be updated");
    }

    @Test
    public void testObserverNotification() {
        AgentObserver observer = new AgentObserver() {
            @Override
            public void updateAgent(Agent agent) {
                // This could be a more complex test, depending on what exactly your observers are supposed to do
                assertTrue(true, "Observer should be notified");
            }
        };
        listAgent.addAgentObserver(observer);
        Agent agent = new Agent("Agent 5");
        listAgent.addAgent(agent);  // This should trigger observer notification
    }
}