package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.consortium;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase.KnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ReasoningChain;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ListConsortiumTest {
    @Test
    public void testUpdateConsortiumDifferentReasoningChains() {
        // Tworzenie potrzebnych obiektów
        Proposition decision1 = new Proposition("prop1", true);
        Proposition decision2 = new Proposition("prop2",true);

        ReasoningChain rc1 = new ReasoningChain(new KnowledgeBase(), decision1);
        ReasoningChain rc2 = new ReasoningChain(new KnowledgeBase(), decision2);

        Agent agent1 = new Agent("agent1");
        Agent agent2 = new Agent("agent2");

        ListReasoningChain listReasoningChain = new ListReasoningChain();
        listReasoningChain.getListReasoningChain().put(agent1, rc1);
        listReasoningChain.getListReasoningChain().put(agent2, rc2);

        ListConsortium listConsortium = new ListConsortium();

        // Wywołanie metody do testu
        listConsortium.updateConsortium(listReasoningChain);

        // Oczekiwane wyniki
        int expectedNumberOfConsortiums = 2;

        // Porównanie wyników
        Assertions.assertEquals(expectedNumberOfConsortiums, listConsortium.getConsortiumMap().size());
    }

    @Test
    public void testUpdateConsortiumSameReasoningChains() {
        // Tworzenie potrzebnych obiektów
        Proposition decision = new Proposition("prop1", true);

        ReasoningChain rc = new ReasoningChain(new KnowledgeBase(), decision);

        Agent agent1 = new Agent("agent1");
        Agent agent2 = new Agent("agent2");

        ListReasoningChain listReasoningChain = new ListReasoningChain();
        listReasoningChain.getListReasoningChain().put(agent1, rc);
        listReasoningChain.getListReasoningChain().put(agent2, rc);

        ListConsortium listConsortium = new ListConsortium();

        // Wywołanie metody do testu
        listConsortium.updateConsortium(listReasoningChain);

        // Oczekiwane wyniki
        int expectedNumberOfConsortiums = 1;

        // Porównanie wyników
        Assertions.assertEquals(expectedNumberOfConsortiums, listConsortium.getConsortiumMap().size());
    }
}