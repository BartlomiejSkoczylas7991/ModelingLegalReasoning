package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight;
import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.ScaleObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ScaleObserver;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Scale implements ScaleObservable, Serializable {
    private static final long serialVersionUID = 1L;
    private Pair<Integer, Integer> elements;
    private transient List<ScaleObserver> observers = new ArrayList<>();

    public Scale(int min_value, int max_value) {
        setScale(min_value, max_value);
    }

    public Scale(Scale other) {
        this.elements = new Pair<>(other.getMin(), other.getMax());
        this.observers = new ArrayList<>(other.getObservers());
    }

    public Scale(){setScale(0, 10);}

    public boolean contains(int value) {
        return value >= elements.getFirst() && value <= elements.getSecond();
    }

    public void setScale(int min_value, int max_value) {
        elements = new Pair<>(min_value, max_value);
        notifyObservers();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Scale scale = (Scale) o;
        return Objects.equals(elements, scale.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(elements);
    }
}