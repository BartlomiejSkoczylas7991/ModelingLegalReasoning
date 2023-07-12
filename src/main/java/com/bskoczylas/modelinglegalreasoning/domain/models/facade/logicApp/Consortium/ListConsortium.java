package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Consortium;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.ReasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.ReasoningChain.ReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ConsortiumObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.DecisionObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Decision.Decision;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.ConsortiumObservable;

import java.util.*;

public class ListConsortium implements ConsortiumObservable, DecisionObserver {
    private ListReasoningChain listReasoningChain;
    Map<Consortium, ConsortiumType> consortiumMap;
    private Decision decision;
    private Set<Agent> agents;
    private List<ConsortiumObserver> observers;

    public ListConsortium() {
        this.observers = new ArrayList<ConsortiumObserver>();
    }

    private void updateConsortium(ListReasoningChain listReasoningChain) {
        this.agents = listReasoningChain.getAgents();
        this.consortiumMap = new HashMap<>();

        for (Map.Entry<Agent, ReasoningChain> entry : listReasoningChain.getListReasoningChain().entrySet()) {
            ReasoningChain rc = entry.getValue();
            Agent agent = entry.getKey();

            // Przejdz przez wszystkie konsorcja i spróbuj znaleźć odpowiednie dla agenta
            boolean consortiumFound = false;
            for (Consortium consortium : consortiumMap.keySet()) {
                if (consortium.getReasoningChain().equals(rc)) {
                    consortium.getAgents().add(agent);
                    consortiumFound = true;
                    break;
                }
            }

            // Jeżeli nie znaleziono pasującego konsorcjum, utwórz nowe
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
        int numberOfAllAgents = this.agents.size(); // Zakładam, że "this.agents" zawiera wszystkich agentów.
        Proposition decision = decisionClass.getDecision(); // Pobieram aktualną decyzję z klasy Decision

        // Majority
        if (rc.getDecision().equals(decision) && numberOfAgentsInConsortium > (numberOfAllAgents / 2)) {
            return ConsortiumType.MAJORITY;
        }

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
