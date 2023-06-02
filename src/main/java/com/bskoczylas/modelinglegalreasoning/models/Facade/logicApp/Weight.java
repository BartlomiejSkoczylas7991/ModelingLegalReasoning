package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

class Weight implements Comparable<Weight> {
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

    @Override
    public int compareTo(Weight other) {
        if (this.value.equals("?") || other.value.equals("?")) {
            throw new IllegalArgumentException("Cannot compare with value \"?\"");
        }
        return ((Integer)this.value).compareTo((Integer)other.value);
    }

    @Override
    public String toString() {
        return this.value.toString();
    }
}
