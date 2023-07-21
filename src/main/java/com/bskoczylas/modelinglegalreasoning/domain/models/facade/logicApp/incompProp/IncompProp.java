package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;

import java.time.LocalDateTime;
import java.util.Objects;

public class IncompProp {
    private static int nextId = 1;
    private int id;
    private Pair<Proposition, Proposition> propositionsPair;
    private LocalDateTime created;
    private boolean isDecision;

    public IncompProp(Pair<Proposition, Proposition> propositionsPair, boolean isDecision) {
        this.id = nextId++;
        this.propositionsPair = propositionsPair;
        this.isDecision = isDecision;
        this.created = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public Pair<Proposition, Proposition> getPropositionsPair() {
        return propositionsPair;
    }

    public void setPropositionsPair(Pair<Proposition, Proposition> propositionsPair) {
        this.propositionsPair = propositionsPair;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public boolean isDecision() {
        return isDecision;
    }

    public void setDecision(boolean decision) {
        isDecision = decision;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        IncompProp.nextId = nextId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IncompProp that = (IncompProp) o;
        return id == that.id && isDecision == that.isDecision && Objects.equals(propositionsPair, that.propositionsPair);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, propositionsPair, isDecision);
    }
}
