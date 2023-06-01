package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Objects;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Proposition;
import javafx.util.Pair;

import java.util.HashSet;
import java.util.Set;

public class IncompProp {
    private Set<Pair<Proposition, Proposition>> incompProp;

    public IncompProp() {
        this.incompProp = new HashSet<>();
    }

    public void addIncompatiblePropositions(Proposition prop1, Proposition prop2) {
        // Ensure that the pair is not already present in the set
        // and that prop1 and prop2 are not the same proposition
        if (!prop1.equals(prop2) && !containsPair(prop1, prop2)) {
            incompProp.add(new Pair<>(prop1, prop2));
        } else {
            System.out.println("Cannot add pair: propositions are the same or pair already exists");
        }
    }

    boolean containsPair(Proposition prop1, Proposition prop2) {
        // Check if the pair is in the set in either order
        return incompProp.contains(new Pair<>(prop1, prop2)) || incompProp.contains(new Pair<>(prop2, prop1));
    }

    public Set<Pair<Proposition, Proposition>> getIncompatiblePropositions() {
        return incompProp;
    }
}
