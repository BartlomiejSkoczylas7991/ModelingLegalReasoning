package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

public class Rule {
    private static int nextId = 1;
    private int id = 0;
    private Set<Proposition> premises;
    private Proposition conclusion;
    private LocalDateTime created;

    public Rule(Set<Proposition> premises, Proposition conclusion) {
        this.id = nextId++;
        this.premises = premises;
        this.conclusion = conclusion;
        this.created = LocalDateTime.now();
    }
    public Set<Proposition> getPremises() {
        return premises;
    }

    @Override
    public String toString() {
        String premisesString = this.premises.stream()
                .map(Proposition::getStatement)
                .collect(Collectors.joining(", "));
        return premisesString + " -> " + this.conclusion.getStatement();
    }

    public Proposition getConclusion() {
        return conclusion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFormattedCreated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return created.format(formatter);
    }
}