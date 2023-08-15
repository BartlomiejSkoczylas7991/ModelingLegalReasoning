package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class IncompPropTest {
    @Test
    void testConstructor() {
        Proposition prop1 = new Proposition("Prop1");
        Proposition prop2 = new Proposition("Prop2");
        Pair<Proposition, Proposition> pair = new Pair<>(prop1, prop2);
        IncompProp incompProp = new IncompProp(pair, true);

        assertEquals(pair, incompProp.getPropositionsPair());
        assertTrue(incompProp.isDecision());
        assertNotNull(incompProp.getCreated());
    }

    @Test
    void testGetPropositions() {
        Proposition prop1 = new Proposition("Prop1");
        Proposition prop2 = new Proposition("Prop2");
        Pair<Proposition, Proposition> pair = new Pair<>(prop1, prop2);
        IncompProp incompProp = new IncompProp(pair, false);

        assertEquals(prop1, incompProp.getProp1());
        assertEquals(prop2, incompProp.getProp2());
        assertEquals("Prop1", incompProp.getProp1Name());
        assertEquals("Prop2", incompProp.getProp2Name());
    }

    @Test
    void testSetDecision() {
        Proposition prop1 = new Proposition("Prop1");
        Proposition prop2 = new Proposition("Prop2");
        Pair<Proposition, Proposition> pair = new Pair<>(prop1, prop2);
        IncompProp incompProp = new IncompProp(pair, false);

        assertFalse(incompProp.isDecision());
        incompProp.setDecision(true);
        assertTrue(incompProp.isDecision());
    }

    @Test
    void testDateFormat() {
        IncompProp incompProp = new IncompProp(null, false);
        LocalDateTime created = incompProp.getCreated();
        String formattedDate = created.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        assertEquals(formattedDate, incompProp.getFormattedCreated());
    }

    @Test
    void testEqualsAndHashCode() {
        Proposition prop1 = new Proposition("Prop1");
        Proposition prop2 = new Proposition("Prop2");
        Pair<Proposition, Proposition> pair = new Pair<>(prop1, prop2);
        IncompProp incompProp1 = new IncompProp(pair, false);
        IncompProp incompProp2 = new IncompProp(pair, false);

        assertEquals(incompProp1, incompProp2);
        assertEquals(incompProp1.hashCode(), incompProp2.hashCode());

        incompProp2.setDecision(true);

        assertNotEquals(incompProp1, incompProp2);
        assertNotEquals(incompProp1.hashCode(), incompProp2.hashCode());
    }
}