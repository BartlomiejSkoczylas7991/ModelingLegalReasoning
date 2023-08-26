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

public class DecisionVoting implements DecisionObservable, RCObserver, IncompPropObserver {
    private ListReasoningChain listReasoningChain = new ListReasoningChain();
    private Map<Proposition, Set<Agent>> pp; // pro appellant
    private Map<Proposition, Set<Agent>> pd; // pro appellee
    private int sumVotesAgentsWithDecision;
    private int sumVotesAllAgents;
    private Proposition decision;
    private Pair<Proposition, Proposition> decisions;
    private List<DecisionObserver> observers = new ArrayList<>();
    public DecisionVoting() {}

    private void updateDecision(List<Agent> agents) {
        int ppCount = 0;
        int pdCount = 0;
        sumVotesAllAgents = 0;
        sumVotesAgentsWithDecision = 0;
        if (this.listReasoningChain.getAgents() != null) {
            for (Agent agent : agents) {
                ReasoningChain agentRC = listReasoningChain.getReasoningChainByAgent(agent);
                if (agentRC.getDecision() != null) {
                    Proposition agentDecision = agentRC.getDecision();
                    if (agentDecision.equals(decisions.getFirst())) {
                        ppCount++;
                        pp.get(decisions.getFirst()).add(agent);

                    } else if (agentDecision.equals(decisions.getSecond())) {
                        pdCount++;
                        pd.get(decisions.getSecond()).add(agent);
                    }
                }
                sumVotesAllAgents++;
            }
            sumVotesAgentsWithDecision = ppCount + pdCount;
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

    public int getSumVotesAgentsWithDecision() {
        return sumVotesAgentsWithDecision;
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
            pp = new HashMap<>();
            pp.put(listIncompProp.getDecisions().getFirst(), new HashSet<>());
            pd = new HashMap<>();
            pd.put(listIncompProp.getDecisions().getSecond(), new HashSet<>());
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
                ", sum_votes=" +
                '}';
    }
}
