package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class KnowledgeBaseTest {
    private Set<Proposition> premises;
    private Proposition conclusion;

    @BeforeEach
    public void setUp() {
        premises = new HashSet<>();
        premises.add(new Proposition("prop1"));
        premises.add(new Proposition("prop2"));
        premises.add(new Proposition("prop3"));
        conclusion = new Proposition("decision");
    }

    @Test
    public void testConstructor() {
        Set<Proposition> premises = new HashSet<Proposition>();
        premises.add(new Proposition("prop1"));
        premises.add(new Proposition("prop2"));
        premises.add(new Proposition("prop3"));
        Proposition conclusion = new Proposition("decision");
        Rule rule = new Rule(premises, conclusion);

        assertEquals(premises, rule.getPremises());
        assertEquals(conclusion, rule.getConclusion());
        assertNotNull(rule.getCreated());
    }

    @Test
    public void testUniqueIds() {
        Rule rule1 = new Rule(new HashSet<>(), new Proposition(""));
        Rule rule2 = new Rule(new HashSet<>(), new Proposition(/* wartości */));

        assertNotEquals(rule1.getId(), rule2.getId());
    }

}