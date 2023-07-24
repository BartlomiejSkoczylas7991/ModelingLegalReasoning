package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value;

import java.time.LocalDateTime;
import java.util.Objects;

public class Value {
    private static int nextId = 1;
    private int id;
    private String name;
    private LocalDateTime created;

    public Value(String name) {
        this.id = nextId++;
        this.name = name;
        this.created = LocalDateTime.now();
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

    public static int getNextId() {
        return nextId;
    }

    public static void setNextId(int nextId) {
        Value.nextId = nextId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Value value = (Value) o;
        return id == value.id && Objects.equals(name, value.name);
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
