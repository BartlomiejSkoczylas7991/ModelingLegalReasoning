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

public class ListConsortium implements ConsortiumObservable, DecisionObserver {
    private ListReasoningChain listReasoningChain;
    Map<Consortium, ConsortiumType> consortiumMap;
    private Decision decision;
    private List<Agent> agents;
    private List<ConsortiumObserver> observers;

    public ListConsortium() {
        this.observers = new ArrayList<ConsortiumObserver>();
        this.consortiumMap = new HashMap<>();
    }

    private void updateConsortium(ListReasoningChain listReasoningChain) {
        this.agents = listReasoningChain.getAgents();
        this.consortiumMap = new HashMap<>();

        for (Map.Entry<Agent, ReasoningChain> entry : listReasoningChain.getListReasoningChain().entrySet()) {
            ReasoningChain rc = entry.getValue();
            Agent agent = entry.getKey();
            System.out.println("updateConsortium");
            // Go through all the syndications and try to find the right one for the agent
            boolean consortiumFound = false;
            for (Consortium consortium : consortiumMap.keySet()) {
                if (consortium.getReasoningChain().equals(rc)) {
                    consortium.getAgents().add(agent);
                    consortiumFound = true;
                    break;
                }
            }

            // If no matching consortium is found, create a new one
            if (!consortiumFound) {
                Consortium newConsortium = new Consortium(rc);
                Set<Agent> newConsortiumAgents = new HashSet<>();
                newConsortiumAgents.add(agent);
                newConsortium.setAgents(newConsortiumAgents);
                consortiumMap.put(newConsortium, null);
            }
        }
    }

    private ConsortiumType determineConsortiumType(Consortium consortium, List<Consortium> allConsortiums, Decision decisionClass) {
        ReasoningChain rc = consortium.getReasoningChain();
        int numberOfAgentsInConsortium = consortium.getAgents().size();
        int numberOfAllAgents = this.agents.size(); // "this.agents" is assumed to contain all agents.
        Proposition decision = decisionClass.getDecision(); // I fetch the current decision from the Decision class

        // Majority
        if (rc.getDecision().equals(decision) && numberOfAgentsInConsortium > (numberOfAllAgents / 2)) {
            return ConsortiumType.MAJORITY;
        }
        System.out.println("determineConsortiumType");
        // Plurality
        int maxNumberOfAgentsInOtherConsortiums = allConsortiums.stream()
                .filter(c -> !c.equals(consortium))
                .filter(c -> c.getReasoningChain().getDecision().equals(decision))
                .mapToInt(c -> c.getAgents().size())
                .max()
                .orElse(0);

        if (rc.getDecision().equals(decision) && numberOfAgentsInConsortium > maxNumberOfAgentsInOtherConsortiums) {
            return ConsortiumType.PLURALITY;
        }

        // Agree
        if (rc.getDecision().equals(decision) && numberOfAgentsInConsortium < maxNumberOfAgentsInOtherConsortiums) {
            return ConsortiumType.CONCURRING;
        }

        // Oposition
        return ConsortiumType.DISSENTING;
    }

    public Map<Consortium, ConsortiumType> getConsortiumMap() {
        return consortiumMap;
    }

    public ListReasoningChain getListReasoningChain() {
        return listReasoningChain;
    }

    public void setListReasoningChain(ListReasoningChain listReasoningChain) {
        this.listReasoningChain = listReasoningChain;
    }

    public void setConsortiumMap(Map<Consortium, ConsortiumType> consortiumMap) {
        this.consortiumMap = consortiumMap;
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
                ", consortiumMap=" + consortiumMap +
                ", decision=" + decision +
                ", agents=" + agents +
                ", observers=" + observers +
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
        System.out.println("Update decyzja w ListConsortium");
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
