package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Decision;

import com.bskoczylas.modelinglegalreasoning.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.IncompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.ReasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observables.Decision_Observable;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.Decision_Observer;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.IncompProp_Observer;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.RC_Observer;

import java.util.*;

public class Decision implements Decision_Observable, RC_Observer, IncompProp_Observer {
    private ListReasoningChain listReasoningChain;
    private HashMap<Proposition, Iterator> pp;
    private HashMap<Proposition, Iterator> pd;
    private int sum_votes;
    private Proposition decision;
    private Pair<Proposition, Proposition> decisions;
    private List<Decision_Observer> observers;
    private Pair<Proposition, Proposition> possibleDecisions;

    public Decision(){this.observers = new ArrayList<Decision_Observer>();}

    private void updateDecision(Set<Agent> agents) {
        int ppCount = 0;
        int pdCount = 0;

        for (Agent agent : agents) {

        }
    }

    public Proposition getDecision() {
        return this.decision;
    }

    @Override
    public void updateRC(ListReasoningChain listReasoningChain) {
        this.listReasoningChain = listReasoningChain;
        if (!(this.decisions == null)) {
            updateDecision();
        }
    }

    @Override
    public void updateIncomp(ListIncompProp listIncompProp) {
        if(!(listReasoningChain == null){

        }
        decisions = listIncompProp.getDecisions();
        updateDecision();
    }

    @Override
    public void addObserver(Decision_Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(Decision_Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (Decision_Observer observer : observers){
            observer.update(this);
        }
    }
}
