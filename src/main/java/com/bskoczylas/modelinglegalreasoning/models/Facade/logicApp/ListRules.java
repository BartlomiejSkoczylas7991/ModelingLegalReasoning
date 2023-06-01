package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Objects.Rule;
import com.bskoczylas.modelinglegalreasoning.models.observers.AgentObserver;
import com.bskoczylas.modelinglegalreasoning.models.observers.RuleObserver;

import java.util.LinkedList;
import java.util.List;

public class ListRules {
    private List<Rule> listValue = new LinkedList<>();
    private List<RuleObserver> observers;

    public ListRules(List<Rule> listValue) {
        this.listValue = listValue;
    }

    public ListRules(){}


}
