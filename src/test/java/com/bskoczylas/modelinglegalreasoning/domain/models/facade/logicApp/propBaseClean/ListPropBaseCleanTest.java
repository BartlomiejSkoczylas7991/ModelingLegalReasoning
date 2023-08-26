package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.propBaseClean;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AVObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.ListValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValueProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

public class ListPropBaseCleanTest {
    @Test
    public void testCalculateAgentPropositions() {
        // Przygotowanie danych testowych
        Agent agent1 = new Agent("Agent1");
        Proposition prop1 = new Proposition("Guilty");
        Value truth = new Value("Truth");
        List<Proposition> propositions = Arrays.asList(prop1);
        ListAgent listAgent = new ListAgent();
        ListValue listValue = new ListValue();
        ListProposition listProposition = new ListProposition();

        AgentValueToWeight avWeight = new AgentValueToWeight();
        AgentValuePropWeight avpWeight = new AgentValuePropWeight();
        listAgent.addAgentObserver(avWeight);
        listValue.addObserver(avWeight);
        listAgent.addAgentObserver(avpWeight);
        listValue.addObserver(avpWeight);
        listProposition.addObserver(avpWeight);
        ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
        avWeight.addAVObserver(listPropBaseClean);
        avpWeight.addObserver(listPropBaseClean);
        listAgent.addAgent(agent1);
        listValue.addValue(truth);
        listProposition.addProposition(prop1);
        avWeight.editWeight(agent1, truth, 2);
        avpWeight.editWeight(agent1, truth, prop1, 3);
        System.out.println(avWeight.getAgentValueWeights());
        System.out.println(avpWeight.getAgentValuePropWeights());

        Set<Proposition> result = listPropBaseClean.calculateAgentPropositions(agent1, propositions, avWeight, avpWeight);

        // Oczekiwany wynik
        Set<Proposition> expected = new HashSet<>();
        expected.add(prop1);

        // Sprawdzenie wyniku
        assertEquals(expected, result, "The calculated agent propositions do not match the expected result");
    }

    @Test
    public void testCalculatePropBaseCleanWithEmptyAgents() {
        List<Agent> agents = new ArrayList<>();
        List<Proposition> propositions = new ArrayList<>();
        // Dodaj tu propozycje, jeśli to potrzebne

        AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
        AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();

        ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
        HashMap<Agent, Set<Proposition>> result = listPropBaseClean.calculatePropBaseClean(agents, propositions, agentValueToWeight, agentValuePropWeight);

        assertTrue(result.isEmpty(), "Result should be an empty map when there are no agents");
    }

    @Test
    public void testCalculatePropBaseCleanWithEmptyPropositions() {
        List<Agent> agents = new ArrayList<>();
        // Dodaj tu agentów, jeśli to potrzebne

        List<Proposition> propositions = new ArrayList<>();

        AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
        AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();

        ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
        HashMap<Agent, Set<Proposition>> result = listPropBaseClean.calculatePropBaseClean(agents, propositions, agentValueToWeight, agentValuePropWeight);

        for (Set<Proposition> props : result.values()) {
            assertTrue(props.isEmpty(), "Each agent should have an empty set of propositions when there are no propositions");
        }
    }

    @Test
    public void testCalculatePropBaseCleanWithEmptyAgentsAndPropositions() {
        List<Agent> agents = new ArrayList<>();
        List<Proposition> propositions = new ArrayList<>();

        AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
        AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();

        ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
        HashMap<Agent, Set<Proposition>> result = listPropBaseClean.calculatePropBaseClean(agents, propositions, agentValueToWeight, agentValuePropWeight);

        assertTrue(result.isEmpty(), "Result should be an empty map when there are no agents and no propositions");
    }

    @Test
    public void testCalculateAgentPropositionsWithSpecificWeightsAndValues() {
        Agent agent = new Agent("Agent1");
        List<Agent> agents = Collections.singletonList(agent);
        Proposition proposition1 = new Proposition("Prop1");
        Proposition proposition2 = new Proposition("Prop2");
        List<Proposition> propositions = Arrays.asList(proposition1, proposition2);

        Value value1 = new Value("Value1");
        AgentValue agentValue1 = new AgentValue(agent, value1);
        AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
        agentValueToWeight.addWeight(agent, value1, Weight.of(5));

        AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
        agentValuePropWeight.addWeight(agent, value1, proposition1, Weight.of(3));
        agentValuePropWeight.addWeight(agent, value1, proposition2, Weight.of(6));

        ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
        Set<Proposition> result = listPropBaseClean.calculateAgentPropositions(agent, propositions, agentValueToWeight, agentValuePropWeight);

        assertEquals(1, result.size(), "Should only have one proposition passing the filter");
        assertTrue(result.contains(proposition2), "Proposition2 should pass the filter");
    }

    //@Test
    //public void testCalculateAgentPropositionsWithEqualWeights() {
    //    // Podobnie jak w poprzednim teście, ale używaj tych samych wag dla wszystkich wartości i propozycji
    //    Agent agent = new Agent("Agent1");
    //    List<Agent> agents = Collections.singletonList(agent);
    //    Proposition proposition1 = new Proposition("Prop1");
    //    Proposition proposition2 = new Proposition("Prop2");
    //    List<Proposition> propositions = Arrays.asList(proposition1, proposition2);
//
    //    Value value1 = new Value("Value1");
    //    AgentValue agentValue1 = new AgentValue(agent, value1);
    //    AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
    //    agentValueToWeight.addWeight(agent, value1, Weight.of(5));
//
    //    AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
    //    agentValuePropWeight.addWeight(agent, value1, proposition1, Weight.of(6));
    //    agentValuePropWeight.addWeight(agent, value1, proposition2, Weight.of(6));
//
    //    ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
    //    Set<Proposition> result = listPropBaseClean.calculateAgentPropositions(agent, propositions, agentValueToWeight, agentValuePropWeight);
//
    //    assertEquals(1, result.size(), "Should only have one proposition passing the filter");
    //    assertTrue(result.contains(proposition2), "Proposition2 should pass the filter");
    //}

    //@Test
    //public void testCalculateAgentPropositionsWithQuestionMarkWeight() {
    //    // Tutaj testujemy, czy klasa poprawnie obsługuje wagi oznaczone znakiem zapytania ("?") w Twojej logice biznesowej
    //    Agent agent = new Agent("Agent1");
    //    Proposition proposition1 = new Proposition("Prop1");
    //    Proposition proposition2 = new Proposition("Prop2");
    //    List<Proposition> propositions = Arrays.asList(proposition1, proposition2);
//
    //    Value value1 = new Value("Value1");
    //    AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
    //    agentValueToWeight.addWeight(agent, value1, Weight.indeterminate());
//
    //    AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
    //    agentValuePropWeight.addWeight(agent, value1, proposition1, Weight.of(3));
    //    agentValuePropWeight.addWeight(agent, value1, proposition2, Weight.of(6));
//
    //    ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
    //    Set<Proposition> result = listPropBaseClean.calculateAgentPropositions(agent, propositions, agentValueToWeight, agentValuePropWeight);
//
    //    // Zakładając, że znak zapytania jest traktowany jako coś, co nie filtruje propozycji, oczekiwany wynik to obie propozycje
    //    assertEquals(2, result.size(), "Both propositions should pass the filter when weight is marked with '?'");
    //}

    @Test
    public void testFilterPropositionsBasedOnWeightsAndValues() {
        Agent agent = new Agent("Agent1");
        Proposition proposition = new Proposition("Prop1");
        Value value = new Value("Value1");

        AgentValue agentValue = new AgentValue(agent, value);
        AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
        agentValueToWeight.addWeight(agent, value, Weight.of(5));

        AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
        agentValuePropWeight.addWeight(agent, value, proposition, Weight.of(3));

        ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
        boolean result = listPropBaseClean.filterPropositions(agent, proposition, agentValueToWeight, agentValuePropWeight);

        assertFalse(result, "Proposition should be filtered out based on the weights and values");
    }

    @Test
    public void testFilterPropositionsWhenWeightIsEqualToValue() {
        Agent agent = new Agent("Agent1");
        Proposition proposition = new Proposition("Prop1");
        Value value = new Value("Value1");

        AgentValue agentValue = new AgentValue(agent, value);
        AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
        agentValueToWeight.addWeight(agent, value, Weight.of(5));

        AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
        agentValuePropWeight.addWeight(agent, value, proposition, Weight.of(5));

        ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
        boolean result = listPropBaseClean.filterPropositions(agent, proposition, agentValueToWeight, agentValuePropWeight);

        assertTrue(result, "Proposition should not be filtered out when weight is equal to value");
    }


    @Test
    public void testFilterPropositionsWhenWeightIsGreaterThanValue() {
        Agent agent = new Agent("Agent1");
        Proposition proposition = new Proposition("Prop1");
        Value value = new Value("Value1");

        AgentValue agentValue = new AgentValue(agent, value);
        AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
        agentValueToWeight.addWeight(agent, value, Weight.of(3));

        AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
        agentValuePropWeight.addWeight(agent, value, proposition, Weight.of(5));

        ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
        boolean result = listPropBaseClean.filterPropositions(agent, proposition, agentValueToWeight, agentValuePropWeight);

        assertTrue(result, "Proposition should not be filtered out when weight is greater than value");
    }
   // @Test
   // public void testFilterPropositionsWithQuestionMarkWeight() {
   //     // W tym przypadku zakładamy, że Twoja logika biznesowa zwraca true, gdy waga jest oznaczona jako "?"
   //     Agent agent = new Agent("Agent1");
   //     Proposition proposition = new Proposition("Prop1");
   //     Value value = new Value("Value1");
//
   //     AgentValue agentValue = new AgentValue(agent, value);
   //     AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
   //     agentValueToWeight.addWeight(agent, value, Weight.indeterminate());
//
   //     AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
   //     agentValuePropWeight.addWeight(agent, value, proposition, Weight.of(3));
//
   //     ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
   //     boolean result = listPropBaseClean.filterPropositions(agent, proposition, agentValueToWeight, agentValuePropWeight);
//
   //     assertTrue(result, "Proposition should not be filtered out when weight is marked with '?'");
   // }

    @Test
    public void testFilterWhenPropositionWeightIsEqualToValueWeight() {
        Agent agent = new Agent("Agent1");
        Proposition proposition = new Proposition("Prop1");
        Value value = new Value("Value1");

        AgentValue agentValue = new AgentValue(agent, value);
        AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
        agentValueToWeight.addWeight(agent, value, Weight.of(5));

        AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
        agentValuePropWeight.addWeight(agent, value, proposition, Weight.of(5));

        ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
        boolean result = listPropBaseClean.filterPropositions(agent, proposition, agentValueToWeight, agentValuePropWeight);

        assertTrue(result, "Proposition should pass the filter since the weights are equal");
    }

    @Test
    public void testFilterWhenPropositionWeightIsGreaterThanValueWeight() {
        // Podobnie jak powyżej, ale z wagą propozycji większą niż waga wartości, powinno zwrócić true
        Agent agent = new Agent("Agent1");
        Proposition proposition = new Proposition("Prop1");
        Value value = new Value("Value1");

        AgentValue agentValue = new AgentValue(agent, value);
        AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
        agentValueToWeight.addWeight(agent, value, Weight.of(3));

        AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
        agentValuePropWeight.addWeight(agent, value, proposition, Weight.of(5)); // Greater than the value weight

        ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
        boolean result = listPropBaseClean.filterPropositions(agent, proposition, agentValueToWeight, agentValuePropWeight);

        assertTrue(result, "Proposition should not be filtered out when proposition weight is greater than value weight");
    }

    @Test
    public void testFilterWhenPropositionWeightIsLessThanValueWeight() {
        // Podobnie jak powyżej, ale z wagą propozycji mniejszą niż waga wartości, powinno zwrócić false
        Agent agent = new Agent("Agent1");
        Proposition proposition = new Proposition("Prop1");
        Value value = new Value("Value1");

        AgentValue agentValue = new AgentValue(agent, value);
        AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
        agentValueToWeight.addWeight(agent, value, Weight.of(5)); // Greater than the proposition weight

        AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
        agentValuePropWeight.addWeight(agent, value, proposition, Weight.of(3)); // Less than the value weight

        ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
        boolean result = listPropBaseClean.filterPropositions(agent, proposition, agentValueToWeight, agentValuePropWeight);

        assertFalse(result, "Proposition should be filtered out when proposition weight is less than value weight");
    }

    @Test
    public void testGetSetStatementProp() {
        // Utwórz listę agentów
        Agent agent1 = new Agent("Agent1");
        List<Agent> agents = Collections.singletonList(agent1);

        // Utwórz propozycje i dodaj je do listy
        Proposition prop1 = new Proposition("Statement1");
        Proposition prop2 = new Proposition("Statement2");
        List<Proposition> propositions = Arrays.asList(prop1, prop2);

        // Utwórz wartości i wagi
        Value value = new Value("Value1");
        AgentValue agentValue = new AgentValue(agent1, value);
        AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
        agentValueToWeight.addWeight(agent1, value, Weight.of(5));


        AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
        agentValuePropWeight.addWeight(agent1, value, prop1, Weight.of(5));
        agentValuePropWeight.addWeight(agent1, value, prop2, Weight.of(6));

        // Oblicz PropBaseClean
        ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
        HashMap<Agent, Set<Proposition>> propBaseClean = listPropBaseClean.calculatePropBaseClean(agents, propositions, agentValueToWeight, agentValuePropWeight);

        // Pobierz zestaw instrukcji dla agenta1
        Set<String> statements = ListPropBaseClean.getSetStatementProp(agent1, propBaseClean);

        // Sprawdź, czy zestaw instrukcji jest poprawny
        Set<String> expectedStatements = new HashSet<>(Arrays.asList("Statement1", "Statement2"));
        assertEquals(expectedStatements, statements, "Set of statements for the given agent should match the expected set");
    }

    @Test
    public void testNoPropositionsForAgent() {
        // Utwórz listę agentów
        Agent agent1 = new Agent("Agent1");
        List<Agent> agents = Collections.singletonList(agent1);

        // Utwórz pustą listę propozycji
        List<Proposition> propositions = new ArrayList<>();

        // Utwórz wartości i wagi (mogą być puste, ponieważ nie ma propozycji)
        AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
        AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();

        // Oblicz PropBaseClean
        ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
        HashMap<Agent, Set<Proposition>> propBaseClean = listPropBaseClean.calculatePropBaseClean(agents, propositions, agentValueToWeight, agentValuePropWeight);

        // Pobierz zestaw instrukcji dla agenta1
        Set<String> statements = ListPropBaseClean.getSetStatementProp(agent1, propBaseClean);

        // Sprawdź, czy zestaw instrukcji jest pusty
        assertTrue(statements.isEmpty(), "Set of statements for the given agent should be empty as there are no propositions");
    }

    //@Test
    //public void testUpdateAgentValueWeight() {
        //// Tworzymy instancję ListPropBaseClean
        //ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
//
        //// Tworzymy agenta, wartość i wagę
        //Agent agent = new Agent("Agent1");
        //Value value = new Value("Value1");
        //Weight initialWeight = Weight.of(5);
        //Weight updatedWeight = Weight.of(6);
//
        //// Tworzymy AgentValue
        //AgentValue agentValue = new AgentValue(agent, value);
//
        //// Ustalamy początkową wagę
        //AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
        //agentValueToWeight.getAgentValueWeights().put(agentValue, initialWeight); // Bezpośrednie dodanie do mapy
//
        //// Aktualizujemy ListPropBaseClean z początkową wagą
        //listPropBaseClean.updateAV(agentValueToWeight);
//
        //// Sprawdzamy, czy początkowa waga została ustawiona
        //assertEquals(initialWeight, listPropBaseClean.getAVWeight().getWeight(agentValue), "Initial weight should be set correctly");
//
        //// Aktualizujemy wagę
        //agentValueToWeight.getAgentValueWeights().put(agentValue, updatedWeight); // Bezpośrednie aktualizowanie w mapie
        //listPropBaseClean.updateAV(agentValueToWeight);
//
        //// Sprawdzamy, czy waga została zaktualizowana
        //assertEquals(updatedWeight, listPropBaseClean.getAVWeight().getWeight(agentValue), "Updated weight should be set correctly");
    //}

    //@Test
    //public void testReactionToWeightEditing() {
    //    // Tworzymy instancję ListPropBaseClean
    //    ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
//
    //    // Tworzymy agenta, wartość, wagę i propozycję
    //    Agent agent = new Agent("Agent1");
    //    Value value = new Value("Value1");
    //    Weight initialWeight = Weight.of(10);
    //    Weight updatedWeight = Weight.of(20);
    //    Proposition proposition = new Proposition("Prop1");
//
    //    // Tworzymy AgentValue
    //    AgentValue agentValue = new AgentValue(agent, value);
//
    //    // Ustalamy początkową wagę
    //    AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
    //    agentValueToWeight.getAgentValueWeights().put(agentValue, initialWeight); // Bezpośrednie dodanie do mapy
//
    //    // Ustalamy wagę propozycji dla agenta, wartości i propozycji
    //    AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
    //    agentValuePropWeight.addWeight(agent, value, proposition, initialWeight); // Używamy metody addValue
//
    //    // Ustalamy listę agentów i propozycji
    //    List<Agent> agents = Collections.singletonList(agent);
    //    List<Proposition> propositions = Collections.singletonList(proposition);
//
    //    // Obliczamy PropBaseClean z początkową wagą
    //    HashMap<Agent, Set<Proposition>> initialPropBaseClean = listPropBaseClean.calculatePropBaseClean(agents, propositions, agentValueToWeight, agentValuePropWeight);
//
    //    // Sprawdzamy, czy początkowy PropBaseClean został obliczony poprawnie
    //    assertEquals(initialPropBaseClean, listPropBaseClean.getListPropBaseClean(), "Initial PropBaseClean should be calculated correctly");
//
    //    // Aktualizujemy wagę
    //    agentValueToWeight.getAgentValueWeights().put(agentValue, updatedWeight); // Bezpośrednie aktualizowanie w mapie
    //    agentValuePropWeight.addWeight(agent, value, proposition, updatedWeight); // Aktualizujemy wagę za pomocą metody addValue
    //    listPropBaseClean.updateAV(agentValueToWeight);
//
    //    // Obliczamy oczekiwany PropBaseClean po edycji wagi
    //    HashMap<Agent, Set<Proposition>> expectedUpdatedPropBaseClean = listPropBaseClean.calculatePropBaseClean(agents, propositions, agentValueToWeight, agentValuePropWeight);
//
    //    // Sprawdzamy, czy PropBaseClean został zaktualizowany po edycji wagi
    //    assertEquals(expectedUpdatedPropBaseClean, listPropBaseClean.getListPropBaseClean(), "PropBaseClean should be updated correctly after weight editing");
    //}
//
    //@Test
    //public void testUpdateWeightForAgentValueProp() {
    //    // Tworzymy agenta, wartość i propozycję
    //    Agent agent = new Agent("Agent1");
    //    Value value = new Value("Value1");
    //    Proposition proposition = new Proposition("Prop1");
//
    //    // Tworzymy początkową i zaktualizowaną wagę
    //    Weight initialWeight = Weight.of(10);
    //    Weight updatedWeight = Weight.of(20);
//
    //    // Tworzymy mapę AgentValueProp do wagi i ustawiamy początkową wagę
    //    AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
    //    agentValuePropWeight.addWeight(agent, value, proposition, initialWeight);
//
    //    // Sprawdzamy, czy początkowa waga została ustawiona poprawnie
    //    assertEquals(initialWeight, agentValuePropWeight.getWeight(agent, value, proposition), "Initial weight should be set correctly");
//
    //    // Aktualizujemy wagę
    //    agentValuePropWeight.addWeight(agent, value, proposition, updatedWeight); // Ponownie używamy metody addValue
//
    //    // Sprawdzamy, czy waga została zaktualizowana poprawnie
    //    assertEquals(updatedWeight, agentValuePropWeight.getWeight(agent, value, proposition), "Weight should be updated correctly");
    //}
//
    @Test
    public void testObserversNotified() {
        // Tworzenie instancji klasy AgentValueToWeight
        AgentValueToWeight agentValueToWeight = new AgentValueToWeight();
        Scale scale = new Scale(); // Tworzenie instancji klasy Scale, jeśli jest to wymagane
        agentValueToWeight.setScale(scale);

        // Tworzenie mocków obserwatorów
        AVObserver observer1 = mock(AVObserver.class);
        AVObserver observer2 = mock(AVObserver.class);

        // Dodawanie obserwatorów
        agentValueToWeight.addAVObserver(observer1);
        agentValueToWeight.addAVObserver(observer2);

        // Tworzenie AgentValue
        Agent agent = new Agent("Agent1");
        Value value = new Value("Value1");
        AgentValue agentValue = new AgentValue(agent, value);

        // Ustawianie początkowej wagi
        Weight weight = Weight.of( 10); // zakładając, że konstruktor Weight przyjmuje Scale i wartość
        agentValueToWeight.addWeight(agent, value, weight);

        // Wywołanie metody edycji wagi, która powinna powiadomić obserwatorów
        agentValueToWeight.editWeight(agentValue, 20);

        // Weryfikacja, czy obserwatorzy zostali powiadomieni
        verify(observer1).updateAV(agentValueToWeight);
        verify(observer2).updateAV(agentValueToWeight);
    }


}