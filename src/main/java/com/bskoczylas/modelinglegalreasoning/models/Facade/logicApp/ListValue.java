package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.observables.AgentObservable;
import com.bskoczylas.modelinglegalreasoning.models.observers.AgentObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListValue implements AgentObservable {
    private List<Value> listValue = new LinkedList<>();
    private List<AgentObserver> observers;


    public ListValue(List<Value> listValue) {
        this.listValue = listValue;
        this.observers = new ArrayList<>();
    }

    public ListValue(){}
    public List<Value> getValues() {
        return listValue;
    }

    public void addValue(Value value) {
        listValue.add(value);
        notifyObservers();
    }

    public void removeValue(Value value) {
        listValue.remove(value);
        notifyObservers();
    }

    public void setListValue(List<Value> listValue) {
        this.listValue = listValue;
    }

    @Override
    public void addObserver(AgentObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(AgentObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (AgentObserver observer : this.observers) {
            observer.update();
        }
    }
}
