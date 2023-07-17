package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.ValueObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ValueObserver;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ListValue implements ValueObservable {
    private List<Value> listValue = new ArrayList<>();
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
        notifyObservers(this);
    }

    public void removeValue(Value value) {
        listValue.remove(value);
        notifyObservers(this);
    }

    public void setListValue(List<Value> listValue) {
        this.listValue = listValue;
        notifyObservers(this);
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
    public void notifyObservers(ListValue listValue) {
        for (ValueObserver observer : this.observers){
            observer.updateValue(this);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Values = {");

        for(int i=0; i<listValue.size(); i++){
            sb.append(listValue.get(i).getName());

            if(i < listValue.size() - 1) {
                sb.append(", ");
            }
        }

        sb.append("}");
        return sb.toString();
    }
}
