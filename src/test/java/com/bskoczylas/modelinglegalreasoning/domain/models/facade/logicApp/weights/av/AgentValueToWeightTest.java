package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.AVObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Scale;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight.Weight;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class AgentValueToWeightTest {


    @Test
    void testAddAndEditWeight() {
        Agent agent = new Agent("Alice");
        Value value = new Value("Truth");
        AgentValueToWeight avtWeight = new AgentValueToWeight();
        avtWeight.setScale(new Scale(0, 10));

        avtWeight.addWeight(agent, value, new Weight(avtWeight.getScale(), 5));

        AgentValue agentValue = new AgentValue(agent, value);
        assertEquals(new Weight(avtWeight.getScale(), 5), avtWeight.getWeight(agentValue));

        avtWeight.editWeight(agentValue, 7);
        assertEquals(new Weight(avtWeight.getScale(), 7), avtWeight.getWeight(agentValue));
    }

    @Test
    void testObserverNotification() {
        Agent agent = new Agent("Alice");
        Value value = new Value("Truth");
        AgentValueToWeight avtWeight = new AgentValueToWeight();

        // Tworzymy atrapę obserwatora (spy)
        AVObserver observerSpy = Mockito.spy(AVObserver.class);

        // Dodajemy atrapę obserwatora
        avtWeight.addAVObserver(observerSpy);

        // Sprawdzamy, czy metoda updateAV została wywołana na obserwatorzeadd
        Mockito.verify(observerSpy).updateAV(avtWeight);
    }

    @Test
    void testToString() {
        Agent agent = new Agent("Alice");
        Value value = new Value("Truth");
        Scale scale = new Scale(0, 10);
        AgentValueToWeight avtWeight = new AgentValueToWeight();
        avtWeight.setScale(scale);
        avtWeight.addWeight(agent, value, new Weight(scale, 5));

        String expectedString = "{(Alice, Truth): 5, }"; // Zakładam, że AgentValue i Weight mają odpowiednie metody toString
        assertEquals(expectedString, avtWeight.toString(), "toString should generate the correct string");
    }
}