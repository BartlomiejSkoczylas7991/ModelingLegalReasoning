package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;
import java.util.ArrayList;
import java.util.List;

class Scale {
    private List<Integer> elements = new ArrayList<>();

    public Scale(int min_value, int max_value) {
        setScale(min_value, max_value);
    }

    public boolean contains(int value) {
        return elements.contains(value);
    }

    public void setScale(int min_value, int max_value) {
        elements.clear();
        for (int i = min_value; i <= max_value; i++) {
            elements.add(i);
        }
    }

    @Override
    public String toString() {
        return elements.toString();
    }
}