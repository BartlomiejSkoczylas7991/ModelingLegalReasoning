package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

public class Rule {
    private static int nextId = 1;
    private final int id;
    private Set<Proposition> premises;
    private Proposition conclusion;
    private String label;
    private LocalDateTime created;

    public Rule(Set<Proposition> premises, Proposition conclusion, String label) {
        this.id = nextId++;
        this.premises = premises;
        this.conclusion = conclusion;
        this.label = label;
    }
    public Set<Proposition> getPremises() {
        return premises;
    }

    @Override
    public String toString() {
        String premisesString = this.premises.stream()
                .map(Proposition::getStatement)
                .collect(Collectors.joining(", "));
        return this.label + ": " + premisesString + " -> " + this.conclusion.getStatement();
    }

    public Proposition getConclusion() {
        return conclusion;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        Rule.nextId = nextId;
    }

    public void setPremises(Set<Proposition> premises) {
        this.premises = premises;
    }

    public void setConclusion(Proposition conclusion) {
        this.conclusion = conclusion;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public boolean isApplicableTo(Set<Proposition> propositions) {
        return propositions.containsAll(this.premises);
    }
}