package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class IncompProp {
    private static int nextId = 1;
    private int id;
    private Pair<Proposition, Proposition> propositionsPair;
    private LocalDateTime created;
    private BooleanProperty isDecision;

    public IncompProp(Pair<Proposition, Proposition> propositionsPair, boolean isDecision) {
        this.id = nextId++;
        this.propositionsPair = propositionsPair;
        this.isDecision = new SimpleBooleanProperty(isDecision);
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

    public Proposition getProp1() {
        Proposition proposition = propositionsPair.getFirst();
        return proposition;
    }

    public String getProp1Name() {
        return this.getProp1().getStatement();
    }

    public Proposition getProp2() {
        Proposition proposition = propositionsPair.getSecond();
        return proposition;
    }

    public String getProp2Name() {
        return this.getProp2().getStatement();
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public boolean isDecision() {
        return isDecision.get();
    }

    // getter for BooleanProperty (required for PropertyValueFactory)
    public BooleanProperty decisionProperty() {
        return isDecision;
    }

    public String getFormattedCreated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return created.format(formatter);
    }

    public void setDecision(boolean decision) {
        isDecision.set(decision);
    }

    public void setId(int id) {
        this.id = id;
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
