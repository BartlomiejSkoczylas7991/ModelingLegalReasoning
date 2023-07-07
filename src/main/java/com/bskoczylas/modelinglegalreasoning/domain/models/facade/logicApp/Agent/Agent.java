package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent;

import java.util.Objects;

public class Agent {
    private static int nextId = 1;
    private final int id;
    private String name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return id == agent.id && Objects.equals(name, agent.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}