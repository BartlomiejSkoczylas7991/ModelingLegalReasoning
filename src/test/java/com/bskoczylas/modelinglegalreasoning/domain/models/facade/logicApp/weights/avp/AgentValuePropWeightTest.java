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

    @Test
    public void testAddValueAndGetWeight() {
        // Tworzenie instancji AgentValuePropWeight
        AgentValuePropWeight avpw = new AgentValuePropWeight();

        // Tworzenie instancji Agent, Value, Proposition i Weight
        Agent agent = new Agent(""); // Zakładając, że Agent ma dostępny konstruktor bezparametrowy
        Value value = new Value(""); // Zakładając, że Value ma dostępny konstruktor bezparametrowy
        Proposition prop = new Proposition(""); // Zakładając, że Proposition ma dostępny konstruktor bezparametrowy
        Weight weight = new Weight(5); // Zakładając, że Weight ma dostępny konstruktor bezparametrowy

        // Dodawanie wartości
        avpw.addWeight(agent, value, prop, weight);

        // Pobieranie wagi i sprawdzanie, czy zgadza się z oczekiwaną
        Weight retrievedWeight = avpw.getWeight(agent, value, prop);
        assertEquals(weight, retrievedWeight);
    }

    @Test
    public void testGetWeightForNonExistentCombination() {
        // Tworzenie instancji AgentValuePropWeight
        AgentValuePropWeight avpw = new AgentValuePropWeight();

        // Tworzenie instancji Agent, Value, Proposition
        // które nie zostały wcześniej dodane do AgentValuePropWeight
        Agent agent = new Agent(""); // Zakładając, że Agent ma dostępny konstruktor bezparametrowy
        Value value = new Value(""); // Zakładając, że Value ma dostępny konstruktor bezparametrowy
        Proposition prop = new Proposition(""); // Zakładając, że Proposition ma dostępny konstruktor bezparametrowy

        // Próba pobrania wagi dla nieistniejącej kombinacji
        Weight retrievedWeight = avpw.getWeight(agent, value, prop);

        // Sprawdzanie, czy wynik jest null, co oznacza, że kombinacja nie istnieje
        assertNull(retrievedWeight);
    }

    @Test
    public void testSetValues() {
        AgentValuePropWeight avpw = new AgentValuePropWeight();
        List<Value> newValues = new ArrayList<>(); // Tutaj można dodać wartości
        avpw.setValues(newValues);
        assertEquals(newValues, avpw.getValues());
    }

    @Test
    public void testSetPropositions() {
        AgentValuePropWeight avpw = new AgentValuePropWeight();
        List<Proposition> newPropositions = new ArrayList<>(); // Tutaj można dodać wartości
        avpw.setPropositions(newPropositions);
        assertEquals(newPropositions, avpw.getPropositions());
    }

    @Test
    public void testSetScale() {
        AgentValuePropWeight avpw = new AgentValuePropWeight();
        Scale newScale = new Scale(); // Zakładając, że Scale ma dostępny konstruktor bezparametrowy
        avpw.setScale(newScale);
        assertEquals(newScale, avpw.getScale());
    }

    @Test
    public void testEditWeight_ExistingWeight() {
        AgentValuePropWeight avpw = new AgentValuePropWeight();
        Agent agent = new Agent("");
        Value value = new Value("");
        Proposition prop = new Proposition("");
        Weight weight = new Weight(new Scale(), 5);
        avpw.addWeight(agent, value, prop, weight);
        Integer newWeightValue = 10;
        avpw.editWeight(new AgentValueProposition(agent, value, prop), newWeightValue);
        assertEquals(newWeightValue, avpw.getWeight(agent, value, prop).getWeight());
    }

    @Test
    public void testEditWeight_NonExistingWeight() {
        AgentValuePropWeight avpw = new AgentValuePropWeight();
        Agent agent = new Agent("");
        Value value = new Value("");
        Proposition prop = new Proposition("");
        Integer newWeightValue = 10;
        avpw.editWeight(new AgentValueProposition(agent, value, prop), newWeightValue);
        assertNull(avpw.getWeight(agent, value, prop));
    }

    @Test
    public void testEditWeight_OutOfScaleBounds() {
        AgentValuePropWeight avpw = new AgentValuePropWeight();
        Scale scale = new Scale(1, 10); // Zakładając, że Scale przyjmuje min i max jako parametry konstruktora
        Agent agent = new Agent("");
        Value value = new Value("");
        Proposition prop = new Proposition("");
        Weight weight = new Weight(scale, 5);
        avpw.addWeight(agent, value, prop, weight);
        avpw.setScale(scale);
        Integer newWeightValue = 20; // Wartość poza zakresem skali
        avpw.editWeight(new AgentValueProposition(agent, value, prop), newWeightValue);
        assertEquals(5, avpw.getWeight(agent, value, prop).getWeight()); // Waga powinna pozostać niezmieniona
    }

    @Test
    public void testUpdateAgent() {
        // Tworzenie mocka obiektu obserwowanego
        ListAgent observable = new ListAgent();

        // Utworzenie instancji testowanej klasy i podłączenie jej jako obserwatora do obiektu obserwowanego
        AgentValuePropWeight agentValuePropWeight = new AgentValuePropWeight();
        observable.addAgentObserver(agentValuePropWeight);

        Agent oldAgent = new Agent("OldAgent");
        Agent newAgent = new Agent("NewAgent");

        // Dodajemy początkowego agenta
        observable.addAgent(oldAgent);
        // Sprawdzamy, czy początkowy agent został dodany do obserwującego
        assertTrue(agentValuePropWeight.getAgents().contains(oldAgent));

        // Usuwamy starego agenta i dodajemy nowego
        observable.removeAgent(oldAgent);
        observable.addAgent(newAgent);

        // Sprawdzanie, czy obserwujący zareagował poprawnie
        assertFalse(agentValuePropWeight.getAgents().contains(oldAgent));
        assertTrue(agentValuePropWeight.getAgents().contains(newAgent));
    }


    @Test
    public void testSetAgentValuePropWeights() {
        AgentValuePropWeight avpw = new AgentValuePropWeight();
        Map<AgentValueProposition, Weight> newWeights = new HashMap<>(); // Tutaj można dodać wartości
        avpw.setAgentValuePropWeights(newWeights);
        assertEquals(newWeights, avpw.getAgentValuePropWeights());
    }

    @Test
    public void testSetAgents() {
        AgentValuePropWeight avpw = new AgentValuePropWeight();
        List<Agent> newAgents = new ArrayList<>(); // Tutaj można dodać wartości
        avpw.setAgents(newAgents);
        assertEquals(newAgents, avpw.getAgents());
    }



    @Test
    void testAddValue() {
        Agent agent = new Agent("Alice");
        Value value = new Value("Truth");
        Proposition proposition = new Proposition("P");
        Weight weight = new Weight(new Scale(), 5);
        AgentValuePropWeight avpWeight = new AgentValuePropWeight();
        avpWeight.addWeight(agent, value, proposition, weight);

        Weight retrievedWeight = avpWeight.getWeight(agent, value, proposition);
        assertEquals(weight, retrievedWeight, "The weight should be correctly stored");
    }

    @Test
    void testToString() {
        Agent agent = new Agent("Alice");
        Value value = new Value("Truth");
        Proposition proposition = new Proposition("P");
        Weight weight = new Weight(new Scale(), 5);
        AgentValuePropWeight avpWeight = new AgentValuePropWeight();
        avpWeight.addAgent(agent);
        avpWeight.addValue(value);
        avpWeight.addProposition(proposition);
        avpWeight.addWeight(agent, value, proposition, weight);

        String expectedString = "{(Alice, Truth, P): 5, }"; // Adapt this to the actual expected format
        assertEquals(expectedString, avpWeight.toString(), "toString should generate the correct string");
    }

    @Test
    void testObserverNotification() {
        AgentValuePropWeight avpWeight = new AgentValuePropWeight();
        AVPObserver observerSpy = Mockito.spy(AVPObserver.class);
        avpWeight.addObserver(observerSpy);

        // Trigger some change that should notify the observer
        avpWeight.setScale(new Scale(0, 10));

        // Verify that the observer was notified
        Mockito.verify(observerSpy).updateAVP(avpWeight);
    }

    @Test
    void testUpdateWeightAccordingToScale() {
        Scale scale = new Scale(1, 10);
        Agent agent = new Agent("Alice");
        Value value = new Value("Truth");
        Proposition proposition = new Proposition("P");
        Weight weight = new Weight(scale, 5);
        AgentValuePropWeight avpWeight = new AgentValuePropWeight();
        avpWeight.setScale(scale);
        avpWeight.addWeight(agent, value, proposition, weight);

        // Change the scale
        avpWeight.updateScale(new Scale(3, 8));

        // Check that the weight was adjusted according to the new scale
        Weight retrievedWeight = avpWeight.getWeight(agent, value, proposition);
        assertEquals(Integer.valueOf(5), retrievedWeight.getWeight(), "Weight should be adjusted according to the new scale");
    }




}