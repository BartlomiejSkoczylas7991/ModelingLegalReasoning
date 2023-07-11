package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Decision;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.ReasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.ReasoningChain.ReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.DecisionObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.DecisionObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.IncompPropObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.RCObserver;

import java.util.*;

public class Decision implements DecisionObservable, RCObserver, IncompPropObserver {
    private ListReasoningChain listReasoningChain;
    private HashMap<Proposition, Iterator> pp;
    private HashMap<Proposition, Iterator> pd;
    private int sum_votes;
    private Proposition decision;
    private Pair<Proposition, Proposition> decisions;
    private List<DecisionObserver> observers;
    private Pair<Proposition, Proposition> possibleDecisions;

    public Decision(){this.observers = new ArrayList<DecisionObserver>();}

    private void updateDecision(Set<Agent> agents) {
        int ppCount = 0;
        int pdCount = 0;

        for (Agent agent : agents) {
            ReasoningChain agentRC = listReasoningChain.getReasoningChainByAgent(agent);
            if (agentRC != null) {
                Proposition agentDecision = agentRC.getDecision();
                if (agentDecision != null) {
                    if (agentDecision.equals(decisions.getFirst())) {
                        ppCount++;
                    } else if (agentDecision.equals(decisions.getSecond())) {
                        pdCount++;
                    }
                }
            }
        }

        if (ppCount > pdCount) {
            decision = decisions.getFirst();
        } else if (pdCount > ppCount) {
            decision = decisions.getSecond();
        } else {
            decision = null;  // Remis, żadna z propozycji nie zdobyła większości głosów.
        }
    }

    public Proposition getDecision() {
        return this.decision;
    }

    public ListReasoningChain getListReasoningChain() {
        return listReasoningChain;
    }

    @Override
    public void updateRC(ListReasoningChain listReasoningChain) {
        this.listReasoningChain = listReasoningChain;
        if (this.decisions != null) {
            updateDecision(this.listReasoningChain.getAgents());
        }
    }

    @Override
    public void updateIncomp(ListIncompProp listIncompProp) {
        this.decisions = listIncompProp.getDecisions();
        if(this.listReasoningChain != null){
            updateDecision(this.listReasoningChain.getAgents());
        }
    }

    @Override
    public void addObserver(DecisionObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(DecisionObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (DecisionObserver observer : observers){
            observer.update(this);
        }
    }
}
