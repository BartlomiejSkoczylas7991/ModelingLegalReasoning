package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Agent implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;
    private final int id;
    private String name;
    private LocalDateTime created;

    public Agent(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.id = nextId++;
        this.name = name;
        this.created = LocalDateTime.now();
    }

    public Agent(Agent other) {
        if (other == null) {
            throw new IllegalArgumentException("Other Agent cannot be null");
        }
        this.id = other.id;
        this.name = other.name;
        this.created = other.created != null ? LocalDateTime.from(other.created) : null;
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

    public String getFormattedCreated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return created.format(formatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Agent agent = (Agent) o;
        return Objects.equals(name, agent.name);
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
    @Override
    public String toString() {
        return name;
    }
}