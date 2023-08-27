package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Proposition implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int next_id = 1;
    private int id;
    private String statement;
    private boolean isDecision;
    private LocalDateTime created;

    public Proposition(String statement) {
        if (statement == null) {
            throw new IllegalArgumentException("Statement cannot be null or empty");
        }
        this.id = Proposition.next_id++;
        this.statement = statement;
        this.created = LocalDateTime.now();
    }

    public Proposition( String statement, boolean isDecision) {
        this.id = id;
        this.statement = statement;
        this.isDecision = isDecision;
        this.created = LocalDateTime.now();
        if (id >= next_id) {
            next_id = id + 1;
        }
    }

    public Proposition(Proposition other) {
        if (other == null) {
            throw new IllegalArgumentException("Other Proposition cannot be null");
        }
        this.id = other.id;
        this.statement = other.statement;
        this.isDecision = other.isDecision;
        this.created = other.created != null ? LocalDateTime.from(other.created) : null;
    }

    public Proposition( int id, String statement, boolean isDecision, LocalDateTime created) {
        this.id = id;
        this.statement = statement;
        this.isDecision = isDecision;
        this.created = created;
        if (id >= next_id) {
            next_id = id + 1;
        }
    }

    public String getStatement() {
        return this.statement;
    }

    public boolean isDecision() {
        return isDecision;
    }

    public void setDecision(boolean decision) {
        isDecision = decision;
    }

    public String getFormattedCreated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return created.format(formatter);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Proposition that = (Proposition) o;
        return Objects.equals(statement, that.statement);
    }

    @Override
    public int hashCode() {
        return Objects.hash(statement);
    }

    @Override
    public String toString() {
        return this.statement == null ? "not decided" : this.statement;
    }
}