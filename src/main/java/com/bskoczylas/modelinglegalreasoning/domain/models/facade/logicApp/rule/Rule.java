package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.stream.Collectors;

public class Rule implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;
    private int id = 1;
    private static int counter = 1;
    private Set<Proposition> premises;
    private Proposition conclusion;
    private LocalDateTime created;

    public Rule(Set<Proposition> premises, Proposition conclusion) {
        this.id = nextId++;
        this.premises = premises;
        this.conclusion = conclusion;
        this.id = counter++;
        this.created = LocalDateTime.now();
    }

    public Rule(Rule ruleToCopy) {
        this.id = ruleToCopy.id;
        this.premises = ruleToCopy.premises.stream()
                .map(Proposition::new)
                .collect(Collectors.toSet());

        this.conclusion = new Proposition(ruleToCopy.conclusion);

        this.created = ruleToCopy.created;
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

    public static void decrementID() {
        if(counter > 0) counter--;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getFormattedCreated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return created.format(formatter);
    }

    public String getPremisesAsString() {
        return this.premises.stream()
                .map(Proposition::getStatement)
                .collect(Collectors.joining(", "));
    }

    public String getConclusionAsString() {
        return this.conclusion.getStatement();
    }
}