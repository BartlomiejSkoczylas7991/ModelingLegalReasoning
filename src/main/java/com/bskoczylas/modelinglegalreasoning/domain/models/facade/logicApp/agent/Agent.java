package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent;

import java.time.LocalDateTime;
import java.util.Objects;

public class Agent {
    private static int nextId = 1;
    private final int id;
    private String name;
    private LocalDateTime created;

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

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        Agent.nextId = nextId;
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

    @Override
    public String toString() {
        return name;
    }
}