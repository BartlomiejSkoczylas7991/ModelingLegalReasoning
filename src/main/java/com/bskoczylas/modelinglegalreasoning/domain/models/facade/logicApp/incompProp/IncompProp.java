package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class IncompProp implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;
    private int id;
    private Pair<Proposition, Proposition> propositionsPair;
    private LocalDateTime created;
    private transient BooleanProperty isDecision;

    public IncompProp(Pair<Proposition, Proposition> propositionsPair, boolean isDecision) {
        this.id = nextId++;
        this.propositionsPair = propositionsPair;
        this.isDecision = new SimpleBooleanProperty(isDecision);
        this.created = LocalDateTime.now();
    }

    public IncompProp(IncompProp other) {
        if (other == null) {
            throw new IllegalArgumentException("Other IncompProp cannot be null");
        }
        this.id = other.id;
        this.propositionsPair = other.propositionsPair != null ? new Pair<>(other.propositionsPair.getFirst(), other.propositionsPair.getSecond()) : null;
        this.isDecision = new SimpleBooleanProperty(other.isDecision());
        this.created = other.created != null ? LocalDateTime.from(other.created) : null;
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

    private void writeObject(ObjectOutputStream oos) throws IOException {
        oos.defaultWriteObject();
        oos.writeBoolean(isDecision.get());
    }

    private void readObject(ObjectInputStream ois) throws ClassNotFoundException, IOException {
        ois.defaultReadObject();
        isDecision = new SimpleBooleanProperty(ois.readBoolean());
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

    public BooleanProperty decisionProperty() {
        return isDecision;
    }

    public String getFormattedCreated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return created.format(formatter);
    }

    public void setDecision(boolean decision) {
        propositionsPair.getFirst().setDecision(decision);
        propositionsPair.getSecond().setDecision(decision);
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
        return isDecision.get() == that.isDecision.get() &&
                propositionsPair.equals(that.getPropositionsPair());
    }

    @Override
    public int hashCode() {
        return Objects.hash(propositionsPair, isDecision.get());
    }
}
