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
    // Przygotowanie danych
    Scale scale = new Scale(0, 10);
    Agent agent = new Agent("Arkadiusz");
    Value value = new Value("Prawda");
    AgentValueToWeight avWeight = new AgentValueToWeight();



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


}