package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.observables.AgentObservable;
import com.bskoczylas.modelinglegalreasoning.models.observables.ValueObservable;
import com.bskoczylas.modelinglegalreasoning.models.observers.AgentObserver;
import com.bskoczylas.modelinglegalreasoning.models.observers.ValueObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListValue implements ValueObservable {
    private List<Value> listValue = new LinkedList<>();
    private List<ValueObserver> observers;


    public ListValue(List<Value> listValue) {
        this.listValue = listValue;
        this.observers = new ArrayList<>();
    }

    public ListValue(){this.observers = new ArrayList<>();}
    public List<Value> getValues() {
        return listValue;
    }

    public void addValue(Value value) {
        listValue.add(value);
        notifyObservers(value);
    }

    public void removeValue(Value value) {
        listValue.remove(value);
        notifyObservers(value);
    }

    public void setListValue(List<Value> listValue) {
        this.listValue = listValue;
    }


    @Override
    public void addObserver(ValueObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(ValueObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(Value value) {
        for (ValueObserver observer : this.observers){
            observer.updateValue(value);
        }
    }
}
