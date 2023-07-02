package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import java.util.Objects;

public class Agent {
    private static int nextId = 1;
    private int id;
    private String name;
    private boolean isJudge = false;

    public Agent(String name) {
        this.id = nextId++;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public boolean isJudge() {
        return isJudge;
    }

    public void setJudge(boolean judge) {
        isJudge = judge;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return id == agent.id && isJudge == agent.isJudge && Objects.equals(name, agent.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, isJudge);
    }
}