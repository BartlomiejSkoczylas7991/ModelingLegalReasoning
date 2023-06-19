package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;
import com.bskoczylas.modelinglegalreasoning.models.observables.Scale_Observable;
import com.bskoczylas.modelinglegalreasoning.models.observers.AgentObserver;
import com.bskoczylas.modelinglegalreasoning.models.observers.Scale_Observer;

import java.util.ArrayList;
import java.util.List;

public class Scale implements Scale_Observable {
    private List<Integer> elements = new ArrayList<>();
    private List<Scale_Observer> observers;

    public Scale(int min_value, int max_value) {
        this.observers = new ArrayList<>();
        setScale(min_value, max_value);
    }

    public Scale(){setScale(0, 10);}
    public boolean contains(int value) {
        return elements.contains(value);
    }

    public void setScale(int min_value, int max_value) {
        elements.clear();
        for (int i = min_value; i <= max_value; i++) {
            elements.add(i);
        }
        notifyObservers();
    }

    public int getMin() {
        if (elements.isEmpty()) {
            throw new IllegalStateException("Scale is empty");
        }
        return elements.get(0);
    }

    public int getMax() {
        if (elements.isEmpty()) {
            throw new IllegalStateException("Scale is empty");
        }
        return elements.get(elements.size() - 1);
    }

    @Override
    public String toString() {
        return elements.toString();
    }

    @Override
    public void addObserver(Scale_Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Scale_Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Scale_Observer observer : this.observers) {
            observer.updateScale(this);
        }
    }
}