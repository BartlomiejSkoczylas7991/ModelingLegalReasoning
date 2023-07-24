package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight;

public class Weight implements Comparable<Weight> {
    private Scale scale;
    private Object value;

    public Weight(Scale scale, Object value) {
        if (!value.equals("?") && !scale.contains((Integer) value)) {
            throw new IllegalArgumentException("Value not in scale");
        }
        this.scale = scale;
        this.value = value;
    }

    public Object getWeight() {
        return this.value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    @Override
    public int compareTo(Weight other) {
        if (this.value.equals("?") || other.value.equals("?")) {
            return 0;
        }
        return ((Integer)this.value).compareTo((Integer)other.value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }

    public void setWeight(Object value) {
        this.value = value;
    }

    public Scale getScale() {
        return scale;
    }

    public void setScale(Scale scale) {
        this.scale = scale;
    }


}
