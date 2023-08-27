package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Value implements Serializable {
    private static final long serialVersionUID = 1L;
    private static int nextId = 1;
    private int id;
    private String name;
    private LocalDateTime created;

    public Value(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        this.id = nextId++;
        this.name = name;
        this.created = LocalDateTime.now();
    }

    public Value(Value other) {
        if (other == null) {
            throw new IllegalArgumentException("Other Value cannot be null");
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

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public String getFormattedCreated() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return created.format(formatter);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value value = (Value) o;
        return Objects.equals(name, value.name);
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
