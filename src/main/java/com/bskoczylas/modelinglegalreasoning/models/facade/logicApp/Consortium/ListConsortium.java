package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Consortium;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Decision.Decision;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.ReasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observables.Consortium_Observable;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.Consortium_Observer;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.Decision_Observer;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.RC_Observer;

import java.util.*;

public class ListConsortium implements RC_Observer, Consortium_Observable, Decision_Observer {
    private ListReasoningChain listReasoningChain;
    Map<Consortium, ConsortiumType> consortiumMap;
    private Set<Agent> agents;
    private List<Consortium_Observer> observers;
    private ListConsortium() {}

    private void updateConsortium(ListReasoningChain listReasoningChain, de){
        this.agents = listReasoningChain.getAgents();
        Set<Consortium> consortiumSet = new HashSet<>();
        // Create all Consortiums
        for ()
        // assign types for Consortiums
    }

    @Override
    public void updateRC(ListReasoningChain listReasoningChain) {
        this.listReasoningChain = listReasoningChain;
        updateConsortium(this.listReasoningChain);
    }

    @Override
    public void addObserver(Consortium_Observer observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(Consortium_Observer observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Consortium_Observer observer : this.observers){
            observer.updateCon(this);
        }
    }

    @Override
    public void update(Decision decision) {

    }
}
