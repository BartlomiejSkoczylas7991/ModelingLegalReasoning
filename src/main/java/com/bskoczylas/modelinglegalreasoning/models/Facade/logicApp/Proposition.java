package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

public class Proposition {
    private static int next_id = 1;
    private int id;
    private boolean isDecision = false;
    private String statement;

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

    public static void setNext_id(int next_id) {
        Proposition.next_id = next_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isDecision() {
        return isDecision;
    }

    public void setDecision(boolean decision) {
        isDecision = decision;
    }
}