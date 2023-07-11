package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.ScaleObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ScaleObserver;

import java.util.ArrayList;
import java.util.List;

public class Scale implements ScaleObservable {
    private List<Integer> elements = new ArrayList<>();
    private List<ScaleObserver> observers;

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
    public void addObserver(ScaleObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ScaleObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (ScaleObserver observer : this.observers) {
            observer.updateScale(this);
        }
    }
}