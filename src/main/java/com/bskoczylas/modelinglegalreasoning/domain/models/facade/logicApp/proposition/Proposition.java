package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.Objects;

public class Proposition {
    private static int next_id = 1;
    private int id;
    private String statement;
    private boolean isDecision;
    private LocalDateTime created;

    public Proposition(String statement) {
        this.id = Proposition.next_id++;
        this.statement = statement;
    }

    @JsonCreator
    public Proposition(@JsonProperty("id") int id,
                       @JsonProperty("statement") String statement,
                       @JsonProperty("isDecision") boolean isDecision,
                       @JsonProperty("created") LocalDateTime created) {
        this.id = id;
        this.statement = statement;
        this.isDecision = isDecision;
        this.created = created;
        if (id >= next_id) {
            next_id = id + 1;
        }
    }
    @JsonProperty
    public String getStatement() {
        return this.statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public static int getNext_id() {
        return next_id;
    }

    public boolean isDecision() {
        return isDecision;
    }

    public void setDecision(boolean decision) {
        isDecision = decision;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public static void setNext_id(int next_id) {
        Proposition.next_id = next_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proposition that = (Proposition) o;
        return id == that.id && Objects.equals(statement, that.statement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, statement);
    }

    @Override
    public String toString() {
        return this.statement;
    }
}