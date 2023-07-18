package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight;
import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.ScaleObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ScaleObserver;

import java.util.ArrayList;
import java.util.List;

public class Scale implements ScaleObservable {
    private Pair<Integer, Integer> elements;
    private List<ScaleObserver> observers = new ArrayList<>();

    public Scale(int min_value, int max_value) {
        setScale(min_value, max_value);
    }

    public Scale(){setScale(0, 10);}

    public boolean contains(int value) {
        return value >= elements.getFirst() && value <= elements.getSecond();
    }

    public void setScale(int min_value, int max_value) {
        elements = new Pair<>(min_value, max_value);
        notifyObservers();
    }

    public Pair<Integer, Integer> getElements() {
        return elements;
    }

    public void setElements(Pair<Integer, Integer> elements) {
        this.elements = elements;
    }

    public List<ScaleObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<ScaleObserver> observers) {
        this.observers = observers;
    }

    public int getMin() {
        if (elements == null) {
            throw new IllegalStateException("Scale is empty");
        }
        return elements.getFirst();
    }

    public int getMax() {
        if (elements == null) {
            throw new IllegalStateException("Scale is empty");
        }
        return elements.getSecond();
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