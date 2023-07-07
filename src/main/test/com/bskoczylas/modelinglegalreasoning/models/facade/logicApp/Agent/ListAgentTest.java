package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AgentObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

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
    void addAgents() {
        Agent agent1 = new Agent("Agent 1");
        Agent agent2 = new Agent("Agent 2");
        List<Agent> agents = Arrays.asList(agent1, agent2);
        listAgent.addAgents(agents);
        assertTrue(listAgent.getAgents().containsAll(agents), "Agents should be added");
    }

    @Test
    void removeAgents() {
        Agent agent1 = new Agent("Agent 1");
        Agent agent2 = new Agent("Agent 2");
        List<Agent> agents = Arrays.asList(agent1, agent2);
        listAgent.addAgents(agents);
        listAgent.removeAgents(agents);
        assertFalse(listAgent.getAgents().contains(agent1), "Agent 1 should be removed");
        assertFalse(listAgent.getAgents().contains(agent2), "Agent 2 should be removed");
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
        AtomicReference<Agent> agentFromObserver = new AtomicReference<>();
        AgentObserver observer = agentFromObserver::set;
        listAgent.addAgentObserver(observer);

        Agent expectedAgent = new Agent("Agent 5");
        listAgent.addAgent(expectedAgent);  // This should trigger observer notification

        assertEquals(expectedAgent, agentFromObserver.get(), "Observer should receive correct agent");
    }
}