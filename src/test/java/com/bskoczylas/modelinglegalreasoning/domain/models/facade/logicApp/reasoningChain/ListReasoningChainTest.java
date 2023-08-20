package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase.KnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.*;

    public class ListReasoningChainTest {
        @Test
        public void testCalculateVotes() {
            // Tworzenie potrzebnych obiektów
            Proposition prop1 = new Proposition("Propozycja1", true);
            Proposition prop2 = new Proposition("Propozycja2", true);
            Proposition prop3 = new Proposition("Propozycja3", true);

            Rule rule1 = new Rule(new HashSet<Proposition>(Arrays.asList(new Proposition[] {prop2})), prop1);
            Rule rule2 = new Rule(new HashSet<Proposition>(Arrays.asList(new Proposition[] {prop3})), prop1);
            Rule rule3 = new Rule(new HashSet<Proposition>(Arrays.asList(new Proposition[] {prop1})), prop2);

            Set<Rule> rj = new HashSet<Rule>(Arrays.asList(new Rule[] {rule1, rule2, rule3}));
            KnowledgeBase kb = new KnowledgeBase(new HashSet<Proposition>(), rj);

            Set<Proposition> propBaseClean = new HashSet<Proposition>(Arrays.asList(new Proposition[] {prop1, prop2}));

            // Wywołanie metody do testu
            Map<Proposition, Integer> votes = new ListReasoningChain().calculateVotes(kb, propBaseClean);

            // Oczekiwane wyniki
            Map<Proposition, Integer> expectedVotes = new HashMap<>();
            expectedVotes.put(prop1, 1);
            expectedVotes.put(prop2, 1);

            // Porównanie wyników
            Assertions.assertEquals(expectedVotes, votes);
        }

        @Test
        public void testFindFinalProposition() {
            // Tworzenie potrzebnych obiektów
            Proposition prop1 = new Proposition("Propozycja1", true);
            Proposition prop2 = new Proposition("Propozycja2", true);

            Map<Proposition, Integer> votes = new HashMap<>();
            votes.put(prop1, 3);
            votes.put(prop2, 2);

            // Wywołanie metody do testu
            Proposition finalProp = new ListReasoningChain().findFinalProposition(votes);

            // Porównanie wyników
            Assertions.assertEquals(prop1, finalProp);
        }

        @Test
        public void testRemoveInconsistentRules() {
            // Tworzenie potrzebnych obiektów
            Proposition prop1 = new Proposition("Propozycja1", true);
            Proposition prop2 = new Proposition("Propozycja2", true);
            Proposition prop3 = new Proposition("Propozycja3", true);

            Rule rule1 = new Rule(new HashSet<Proposition>(Arrays.asList(new Proposition[] {prop2})), prop1);
            Rule rule2 = new Rule(new HashSet<Proposition>(Arrays.asList(new Proposition[] {prop3})), prop1);
            Rule rule3 = new Rule(new HashSet<Proposition>(Arrays.asList(new Proposition[] {prop1})), prop2);

            Set<Rule> minimalKB = new HashSet<Rule>(Arrays.asList(new Rule[] {rule1, rule2, rule3}));
            Set<Proposition> visited = new HashSet<Proposition>(Arrays.asList(new Proposition[] {prop1, prop2, prop3}));
            Set<Proposition> propBaseClean = new HashSet<Proposition>(Arrays.asList(new Proposition[] {prop1, prop2}));

            // Wywołanie metody do testu
            new ListReasoningChain().removeInconsistentRules(minimalKB, visited, propBaseClean);

            // Oczekiwane wyniki
            Set<Rule> expectedKB = new HashSet<Rule>(Arrays.asList(new Rule[] {rule1, rule3}));
            Set<Proposition> expectedVisited = new HashSet<Proposition>(Arrays.asList(new Proposition[] {prop1, prop2}));

            // Porównanie wyników
            Assertions.assertEquals(expectedKB, minimalKB);
            Assertions.assertEquals(expectedVisited, visited);
        }
    }
