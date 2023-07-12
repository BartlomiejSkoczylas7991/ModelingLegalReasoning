package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition;

import java.util.Objects;

public class Proposition {
    private static int next_id = 1;
    private int id;
    private String statement;
    private boolean isDecision;

    public Proposition(String statement) {
        this.id = Proposition.next_id++;
        this.statement = statement;
    }

    public String getStatement() {
        return this.statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    @Override
    public String toString() {
        return this.statement;
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
}