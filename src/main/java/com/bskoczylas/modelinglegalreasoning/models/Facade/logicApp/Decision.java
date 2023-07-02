package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.lists.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observables.Decision_Observable;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.Decision_Observer;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.RC_Observer;
import javafx.util.Pair;

import java.util.*;

public class Decision implements Decision_Observable, RC_Observer {
    private ListReasoningChain listReasoningChain;
    private HashMap<Proposition, Iterator> pp;
    private HashMap<Proposition, Iterator> pd;
    private int sum_votes;
    private Proposition decision;
    private List<Decision_Observer> observers;
    private Pair<Proposition, Proposition> possibleDecisions;

    public Decision(){this.observers = new ArrayList<Decision_Observer>();}

    private void updateDecision(Set<Agent> agents) {
        int ppCount = 0;
        int pdCount = 0;

        for (Agent agent : agents) {
            Proposition conclusion = agent.getReasoningChain().getConclusion();
            if (conclusion.equals(Proposition.PP)) {
                ppCount++;
            } else if (conclusion.equals(Proposition.PD)) {
                pdCount++;
            }
        }

        if (ppCount > pdCount) {
            decision = Proposition.PP;
        } else {
            decision = Proposition.PD;
        }
    }

    public Proposition getDecision() {
        return this.decision;
    }

    @Override
    public void updateRC(ListReasoningChain listReasoningChain) {
        this.listReasoningChain = listReasoningChain;
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
