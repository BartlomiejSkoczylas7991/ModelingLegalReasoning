package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;

import java.util.Set;
import java.util.stream.Collectors;

public class Rule {
    private Set<Proposition> premises;
    private Proposition conclusion;
    private String label;

    public Rule(Set<Proposition> premises, Proposition conclusion, String label) {
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

    public boolean isApplicableTo(Set<Proposition> propositions) {
        return propositions.containsAll(this.premises);
    }
}