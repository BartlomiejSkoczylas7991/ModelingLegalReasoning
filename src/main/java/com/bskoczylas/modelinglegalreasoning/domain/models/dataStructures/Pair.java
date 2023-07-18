package com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class Pair<F, S> {
    @JsonProperty
    private F first;
    @JsonProperty
    private S second;

    public Pair() {
        this.first = null;
        this.second = null;
    }

    public Pair(@JsonProperty("first") F key, @JsonProperty("second") S second) {
        this.first = key;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(first, pair.first) &&
                Objects.equals(second, pair.second);
    }
    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return first + ", " + second;
    }

    public F getKey() {
        return this.getFirst();
    }

    public S getValue() {
        return this.second;
    }
}
