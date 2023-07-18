package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;

import java.time.LocalDateTime;

public class IncompProp {
    private static int nextId = 1;
    private final int id;
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



    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        IncompProp.nextId = nextId;
    }
}
