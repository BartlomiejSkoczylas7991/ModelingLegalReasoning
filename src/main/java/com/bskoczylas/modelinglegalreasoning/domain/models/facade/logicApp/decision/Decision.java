package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.decision;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.DecisionObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.DecisionObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.IncompPropObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.RCObserver;

import java.util.*;

public class Decision implements DecisionObservable, RCObserver, IncompPropObserver {
    private ListReasoningChain listReasoningChain = new ListReasoningChain();
    private HashMap<Proposition, Iterator> pp;
    private HashMap<Proposition, Iterator> pd;
    private int sum_votes;
    private Proposition decision;
    private Pair<Proposition, Proposition> decisions;
    private List<DecisionObserver> observers = new ArrayList<>();;
    private Pair<Proposition, Proposition> possibleDecisions;

    public Decision() {}

    private void updateDecision(List<Agent> agents) {
        int ppCount = 0;
        int pdCount = 0;
        if (this.listReasoningChain.getAgents() != null) {
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
        }

        if (ppCount > pdCount) {
            decision = decisions.getFirst();
            notifyObservers();
        } else if (pdCount > ppCount) {
            decision = decisions.getSecond();
            notifyObservers();
        } else {
            decision = null; // A tie, no proposal won a majority of votes.
            notifyObservers();
        }
    }

    public Proposition getDecision() {
        return this.decision;
    }

    public ListReasoningChain getListReasoningChain() {
        return listReasoningChain;
    }

    public void setDecision(Proposition decision) {
        this.decision = decision;
    }

    public Pair<Proposition, Proposition> getDecisions() {
        return decisions;
    }

    public void setDecisions(Pair<Proposition, Proposition> decisions) {
        this.decisions = decisions;
    }

    public List<DecisionObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<DecisionObserver> observers) {
        this.observers = observers;
    }

    public Pair<Proposition, Proposition> getPossibleDecisions() {
        return possibleDecisions;
    }

    public void setPossibleDecisions(Pair<Proposition, Proposition> possibleDecisions) {
        this.possibleDecisions = possibleDecisions;
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

    @Override
    public String toString() {
        return "Decision{" +
                "listReasoningChain=" + listReasoningChain +
                ", pp=" + pp +
                ", pd=" + pd +
                ", sum_votes=" + sum_votes +
                ", observers=" + observers +
                ", possibleDecisions=" + possibleDecisions +
                '}';
    }
}
