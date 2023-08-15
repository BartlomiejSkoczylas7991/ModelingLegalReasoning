package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.IncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.PropositionObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ListPropositionTest {
    private PropositionObserver mockObserver;
    private ListProposition listProposition;

    @BeforeEach
    void setUp() {
        mockObserver = Mockito.mock(PropositionObserver.class);
        listProposition = new ListProposition();
        listProposition.addObserver(mockObserver);
    }

    @Test
    void getPropositions() {
        assertTrue(listProposition.getListProposition().isEmpty());
    }

    @Test
    void addProposition() {
        Proposition proposition = new Proposition("test");
        listProposition.addProposition(proposition);
        assertEquals(Collections.singletonList(proposition), listProposition.getListProposition());
        verify(mockObserver).updateProposition(listProposition);
    }

    @Test
    void removeProposition() {
        Proposition proposition = new Proposition("test");
        listProposition.addProposition(proposition);
        boolean result = listProposition.removeProposition(proposition);
        assertTrue(result);
        assertTrue(listProposition.getListProposition().isEmpty());
        verify(mockObserver, times(2)).updateProposition(listProposition);
    }

    @Test
    void testSetListProposition() {
        Proposition proposition = new Proposition("test");
        listProposition.setListProposition(Arrays.asList(proposition));
        assertEquals(Collections.singletonList(proposition), listProposition.getListProposition());
        verify(mockObserver).updateProposition(listProposition);
    }

    @Test
    void testAddObserver() {
        PropositionObserver newObserver = Mockito.mock(PropositionObserver.class);
        listProposition.addObserver(newObserver);
        assertTrue(listProposition.getObservers().contains(newObserver));
    }

    @Test
    void testRemoveObserver() {
        listProposition.removeObserver(mockObserver);
        assertFalse(listProposition.getObservers().contains(mockObserver));
    }

    @Test
    void notifyObservers() {
        listProposition.notifyObservers(listProposition);
        verify(mockObserver).updateProposition(listProposition);
    }

    @Test
    void testGetPropositionsNotDecisions() {
        Proposition prop1 = new Proposition("Test 1", false);
        Proposition prop2 = new Proposition("Test 2", true);
        ListProposition listProp = new ListProposition(Arrays.asList(prop1, prop2));

        List<Proposition> result = listProp.getPropositionsNotDecisions();

        assertEquals(1, result.size());
        assertEquals(prop1, result.get(0));
    }

    @Test
    void testUpdateIncomp() {
        Proposition prop1 = new Proposition("Decision 1");
        Proposition prop2 = new Proposition("Decision 2");
        ListIncompProp listIncompProp = new ListIncompProp();
        listIncompProp.addIncompatiblePropositions(new IncompProp(new Pair<>(prop1, prop2), true));
        ListProposition listProp = new ListProposition(Arrays.asList(prop1, prop2));

        listProp.updateIncomp(listIncompProp);

        assertTrue(prop1.isDecision());
        assertTrue(prop2.isDecision());
    }

    @Test
    void testToString() {
        Proposition prop1 = new Proposition("Test 1");
        Proposition prop2 = new Proposition("Test 2", true);
        ListProposition listProp = new ListProposition(Arrays.asList(prop1, prop2));

        String result = listProp.toString();

        assertEquals("Propositions = {Test 1, Test 2 (decision)}", result);
    }
}