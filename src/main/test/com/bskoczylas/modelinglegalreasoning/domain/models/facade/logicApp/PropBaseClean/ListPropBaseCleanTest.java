package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.PropBaseClean;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AVP.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Scale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ListPropBaseCleanTest {
    private ListPropBaseClean listPropBaseClean;
    private AgentValueToWeight AVWeight;
    private AgentValuePropWeight AVPWeight;
    private List<Agent> agents;
    private List<Value> values;
    private List<Proposition> propositions;
    private Scale scale;

    @BeforeEach
    public void setUp() {
        agents = Arrays.asList(mock(Agent.class), mock(Agent.class));
        values = Arrays.asList(mock(Value.class), mock(Value.class));
        propositions = Arrays.asList(mock(Proposition.class), mock(Proposition.class));
        scale = mock(Scale.class);
        AVWeight = new AgentValueToWeight(new HashMap<>(), agents, values, scale);
        AVPWeight = new AgentValuePropWeight(new HashMap<>(), agents, values, propositions, scale);
        listPropBaseClean = new ListPropBaseClean();
    }
    @Test
    public void testCalculatePropBaseClean() {
        // Przykładowe dane wejściowe
        Set<Agent> agents = new HashSet<>();
        Set<Proposition> propositions = new HashSet<>();

        // Wywołanie metody do przetestowania
        HashMap<Agent, Set<Proposition>> result = listPropBaseClean.calculatePropBaseClean(agents, propositions, AVWeight, AVPWeight);

        // Sprawdzanie wyniku
        // Tu powinien być twój kod sprawdzający, czy wynik jest poprawny. To zależy od tego, jakie są oczekiwane wyniki.
    }

    @Test
    public void testCalculateAgentPropositions() {
        // Przykładowe dane wejściowe
        Agent agent = new Agent("Agent1"); // Utwórz agenta
        Set<Proposition> propositions = new HashSet<>();

        // Wywołanie metody do przetestowania
        Set<Proposition> result = listPropBaseClean.calculateAgentPropositions(agent, propositions, AVWeight, AVPWeight);

        // Sprawdzanie wyniku
        // Tu powinien być twój kod sprawdzający, czy wynik jest poprawny. To zależy od tego, jakie są oczekiwane wyniki.
    }

    @Test
    public void testFilterPropositions() {
        // Przykładowe dane wejściowe
        Agent agent = new Agent("Agent1"); // Utwórz agenta
        Proposition prop = new Proposition("Proposition1"); // Utwórz propozycję

        // Wywołanie metody do przetestowania
        boolean result = listPropBaseClean.filterPropositions(agent, prop, AVWeight, AVPWeight);

        // Sprawdzanie wyniku
        // Tu powinien być twój kod sprawdzający, czy wynik jest poprawny. To zależy od tego, jakie są oczekiwane wyniki.
    }

    @Test
    public void testUpdateAV() {
        // Przykładowe dane wejściowe
        AgentValueToWeight avtw = new AgentValueToWeight(); // Utwórz nową instancję AgentValueToWeight

        // Wywołanie metody do przetestowania
        listPropBaseClean.updateAV(avtw);

        // Sprawdzanie wyniku
        // Tu powinien być twój kod sprawdzający, czy metoda updateAV działa prawidłowo.
    }

    @Test
    public void testUpdateAVP() {
        // Przykładowe dane wejściowe
        AgentValuePropWeight avpw = new AgentValuePropWeight(); // Utwórz nową instancję AgentValuePropWeight

        // Wywołanie metody do przetestowania
        listPropBaseClean.updateAVP(avpw);

        // Sprawdzanie wyniku
        // Tu powinien być twój kod sprawdzający, czy metoda updateAVP działa prawidłowo.
    }
}