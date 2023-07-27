package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.consortium;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ConsortiumObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.DecisionObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.decision.Decision;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.ConsortiumObservable;

import java.util.*;

// Zadaniem ListConsortium
public class ListConsortium implements ConsortiumObservable, DecisionObserver {
    private ListReasoningChain listReasoningChain;
    private List<Consortium> listConsortium;
    Map<Consortium, ConsortiumType> consortiumMap;
    private Decision decision;
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

        // We will group agents by the decision
        for (Map.Entry<Agent, ReasoningChain> entry : listReasoningChain.getListReasoningChain().entrySet()) {
            ReasoningChain rc = entry.getValue();
            Agent agent = entry.getKey();

            Proposition decision = rc.getDecision();
            boolean filter = false;
            for (Consortium consortium : this.listConsortium) {
                if (consortium.getReasoningChain().equals(rc)){
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

            ConsortiumType type = determineConsortiumType(consortium, consortiumMap, this.decision);
            consortiumMap.put(consortium, type);
        }
        notifyObservers();
    }

    private ConsortiumType determineConsortiumType(Consortium consortium, Map<Consortium, ConsortiumType> consortiumMap, Decision decisionClass) {
        ReasoningChain rc = consortium.getReasoningChain();
        if (decision != null && rc.getDecision() != null) {
           int numberOfAgentsInConsortium = consortium.getAgents().size();
           int numberOfAllAgents = this.agents.size();
           Proposition decision = decisionClass.getDecision();
           // Majority
           if (decision != null && rc.getDecision().equals(decision) && numberOfAgentsInConsortium > (numberOfAllAgents / 2)) {
               return ConsortiumType.MAJORITY;
           }
            // Plurality
            boolean isAnyConsortiumBigger = consortiumMap.keySet().stream()
                    .filter(c -> !c.equals(consortium))
                    .filter(c -> Objects.nonNull(c.getReasoningChain().getDecision()) && c.getReasoningChain().getDecision().equals(decision))
                    .anyMatch(c -> c.getAgents().size() > numberOfAgentsInConsortium);

            if (rc.getDecision().equals(decision) && !isAnyConsortiumBigger && numberOfAgentsInConsortium <= (numberOfAllAgents / 2)) {
                return ConsortiumType.PLURALITY;
            }
            // Agree
            int maxNumberOfAgentsInOtherConsortiums = consortiumMap.keySet().stream()
                    .filter(c -> !c.equals(consortium))
                    .filter(c -> Objects.nonNull(c.getReasoningChain().getDecision()) && c.getReasoningChain().getDecision().equals(decision))
                    .mapToInt(c -> c.getAgents().size())
                    .max()
                    .orElse(0);

            if (rc.getDecision().equals(decision) && numberOfAgentsInConsortium < maxNumberOfAgentsInOtherConsortiums) {
                return ConsortiumType.CONCURRING;
            }

            // DISSENTING
            if(rc.getDecision() == null || !rc.getDecision().equals(decision)){
                return ConsortiumType.DISSENTING;
            }
       }
       return ConsortiumType.MAJORITY; // no matter what (user can't see it)
    }

    public Map<Consortium, ConsortiumType> getConsortiumMap() {
        return consortiumMap;
    }

    public ListReasoningChain getListReasoningChain() {
        return listReasoningChain;
    }

    public Decision getDecision() {
        return decision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
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
    public void update(Decision decision) {
        this.decision = decision;
        this.listReasoningChain = decision.getListReasoningChain();

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
