package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight;

import java.util.Objects;

public class Weight implements Comparable<Weight> {
    public enum ValueType {
        NUMBER, INDETERMINATE
    }

    private final ValueType type;
    private final Integer numberValue;

    private Weight(ValueType type, Integer numberValue) {
        this.type = type;
        this.numberValue = numberValue;
    }

    public static Weight of(Integer value) {
        return new Weight(ValueType.NUMBER, value);
    }

    public static Weight indeterminate() {
        return new Weight(ValueType.INDETERMINATE, null);
    }

    public boolean isIndeterminate() {
        return this.type == ValueType.INDETERMINATE;
    }

    public Integer getNumberValue() {
        if (type != ValueType.NUMBER) {
            return null;
        }
        return numberValue;
    }

    public Object getWeight() {
        return this.numberValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Weight weight = (Weight) o;
        return type == weight.type && Objects.equals(numberValue, weight.numberValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, numberValue);
    }

    @Override
    public int compareTo(Weight other) {
        if (this.isIndeterminate() || other.isIndeterminate()) {
            return 0;
        }
        return this.numberValue.compareTo(other.numberValue);
    }

    @Override
    public String toString() {
        return isIndeterminate() ? "?" : String.valueOf(numberValue);
    }
}
