package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.consortium;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ConsortiumObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.DecisionObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.decision.DecisionVoting;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.ConsortiumObservable;

import java.util.*;

public class ListConsortium implements ConsortiumObservable, DecisionObserver {
    private ListReasoningChain listReasoningChain;
    private List<Consortium> listConsortium;
    Map<Consortium, ConsortiumType> consortiumMap;
    private DecisionVoting decisionVoting;
    private List<Agent> agents;
    private List<ConsortiumObserver> observers;

    public ListConsortium() {
        this.observers = new ArrayList<ConsortiumObserver>();
        this.consortiumMap = new HashMap<>();
        this.listConsortium = new ArrayList<>();
    }

    void updateConsortium(ListReasoningChain listReasoningChain) {
        this.agents = listReasoningChain.getAgents();
        this.consortiumMap.clear();
        this.listConsortium.clear();

        for (Map.Entry<Agent, ReasoningChain> entry : listReasoningChain.getListReasoningChain().entrySet()) {
            ReasoningChain rc = entry.getValue();
            Agent agent = entry.getKey();

            Proposition decisionOfAgent = rc.getDecision();
            boolean filter = false;
            for (Consortium consortium : this.listConsortium) {
                if (consortium.getReasoningChain().equals(rc) && decisionOfAgent.equals(consortium.getReasoningChain().getDecision())){
                    filter = true;
                    consortium.addAgent(agent);
                }
            }
            if (!filter) {
                Consortium consortium = new Consortium(rc);
                consortium.addAgent(agent);
                this.listConsortium.add(consortium);
            }
        }

        for (Consortium consortium : this.listConsortium) {
            ReasoningChain rc = consortium.getReasoningChain();
            Set<Agent> agentSet = consortium.getAgents();

            ConsortiumType type = determineConsortiumType(consortium, consortiumMap, this.decisionVoting);
            consortiumMap.put(consortium, type);
        }
        notifyObservers();
    }

    private ConsortiumType determineConsortiumType(Consortium consortium, Map<Consortium, ConsortiumType> consortiumMap, DecisionVoting decisionClass) {
        ReasoningChain rc = consortium.getReasoningChain();

        if (rc.getDecision() == null) {
            return ConsortiumType.DISSENTING;
        }

        if (isMajority(rc, consortium, decisionClass)) {
            return ConsortiumType.MAJORITY;
        }

        if (isPlurality(rc, consortium, consortiumMap)) {
            return ConsortiumType.PLURALITY;
        }

        if (isConcurring(rc, consortium, consortiumMap)) {
            return ConsortiumType.CONCURRING;
        }

        if (isDissenting(rc, decisionClass.getDecision())) {
            return ConsortiumType.DISSENTING;
        }

        throw new IllegalStateException("Could not determine consortium type");
    }

    private boolean isMajority(ReasoningChain rc, Consortium consortium, DecisionVoting decisionClass) {
        int numberOfAgentsInConsortium = consortium.getAgents().size();
        Proposition decision = decisionClass.getDecision();
        return rc.getDecision().equals(decision) && numberOfAgentsInConsortium > (decisionClass.getSumVotesAgentsWithDecision() / 2);
    }

    private boolean isPlurality(ReasoningChain rc, Consortium consortium, Map<Consortium, ConsortiumType> consortiumMap) {
        int numberOfAgentsInConsortium = consortium.getAgents().size();
        int numberOfAllAgentsWhoVoted = this.decisionVoting.getSumVotesAgentsWithDecision();
        Proposition winningDecision = decisionVoting.getDecision();
        Proposition decisionOfConsortium = rc.getDecision();

        boolean isAnyConsortiumBigger = consortiumMap.keySet().stream()
                .filter(c -> !c.equals(consortium))
                .filter(c -> Objects.nonNull(c.getReasoningChain().getDecision()) && c.getReasoningChain().getDecision().equals(winningDecision))
                .anyMatch(c -> c.getAgents().size() > numberOfAgentsInConsortium);

        boolean isMajorityAlreadyExists = consortiumMap.values().contains(ConsortiumType.MAJORITY);

        return decisionOfConsortium.equals(winningDecision) && !isAnyConsortiumBigger && numberOfAgentsInConsortium <= (numberOfAllAgentsWhoVoted / 2) && !isMajorityAlreadyExists;
    }

    private boolean isConcurring(ReasoningChain rc, Consortium consortium, Map<Consortium, ConsortiumType> consortiumMap) {
        int numberOfAgentsInConsortium = consortium.getAgents().size();
        Proposition decision = rc.getDecision();

        int maxNumberOfAgentsInOtherConsortiums = consortiumMap.keySet().stream()
                .filter(c -> !c.equals(consortium))
                .filter(c -> Objects.nonNull(c.getReasoningChain().getDecision()) && c.getReasoningChain().getDecision().equals(decision))
                .mapToInt(c -> c.getAgents().size())
                .max()
                .orElse(0);

        return rc.getDecision().equals(decision) && numberOfAgentsInConsortium < maxNumberOfAgentsInOtherConsortiums;
    }

    private boolean isDissenting(ReasoningChain rc, Proposition decision) {
        return rc.getDecision() == null || !rc.getDecision().equals(decision);
    }

    public Map<Consortium, ConsortiumType> getConsortiumMap() {
        return consortiumMap;
    }

    public ListReasoningChain getListReasoningChain() {
        return listReasoningChain;
    }

    public DecisionVoting getDecisionVoting() {
        return decisionVoting;
    }

    public void setDecisionVoting(DecisionVoting decision) {
        this.decisionVoting = decision;
    }

    public List<Agent> getAgents() {
        return agents;
    }

    public void setAgents(List<Agent> agents) {
        this.agents = agents;
    }

    @Override
    public String toString() {
        return "ListConsortium{" +
                "listReasoningChain=" + listReasoningChain +
                ", consortiumMap=" + this.listConsortium +
                '}';
    }

    public List<ConsortiumObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<ConsortiumObserver> observers) {
        this.observers = observers;
    }

    @Override
    public void update(DecisionVoting decisionVoting) {
        this.decisionVoting = decisionVoting;
        this.listReasoningChain = decisionVoting.getListReasoningChain();

        updateConsortium(this.listReasoningChain);
    }

    @Override
    public void addObserver(ConsortiumObserver observer) {
        this.observers.add(observer);
    }

    @Override
    public void removeObserver(ConsortiumObserver observer) {
        this.observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for (ConsortiumObserver observer : this.observers) {
            observer.updateCon(this);
        }
    }
}
