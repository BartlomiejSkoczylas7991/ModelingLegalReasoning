package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PropositionTest {
    @Test
    public void testConstructors() {
        Proposition proposition1 = new Proposition("Test Statement 1");
        assertEquals("Test Statement 1", proposition1.getStatement());
        assertFalse(proposition1.isDecision());

        Proposition proposition2 = new Proposition("Test Statement 2", true);
        assertEquals("Test Statement 2", proposition2.getStatement());
        assertTrue(proposition2.isDecision());

        LocalDateTime now = LocalDateTime.now();
        Proposition proposition3 = new Proposition(3, "Test Statement 3", true, now);
        assertEquals(3, proposition3.getId());
        assertEquals("Test Statement 3", proposition3.getStatement());
        assertEquals(now, proposition3.getCreated());
    }

    @Test
    public void testEquals() {
        Proposition proposition1 = new Proposition("Equal Test");
        Proposition proposition2 = new Proposition("Equal Test");
        Proposition proposition3 = new Proposition("Different Test");

        assertEquals(proposition1, proposition2);
        assertNotEquals(proposition1, proposition3);
    }

    @Test
    public void testGettersAndSetters() {
        Proposition proposition = new Proposition("Test Statement");

        proposition.setId(5);
        proposition.setStatement("Updated Statement");
        proposition.setDecision(true);
        proposition.setCreated(LocalDateTime.of(2023, 8, 14, 0, 0));

        assertEquals(5, proposition.getId());
        assertEquals("Updated Statement", proposition.getStatement());
        assertTrue(proposition.isDecision());
        assertEquals(LocalDateTime.of(2023, 8, 14, 0, 0), proposition.getCreated());
    }

    @Test
    public void testFormattedCreated() {
        LocalDateTime created = LocalDateTime.of(2023, 8, 14, 10, 30);
        Proposition proposition = new Proposition(1, "Test Statement", false, created);

        assertEquals("2023-08-14 10:30:00", proposition.getFormattedCreated());
    }
}