package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Consortium;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Decision.Decision;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.ReasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.ReasoningChain.ReasoningChain;
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

    private void updateConsortium(ListReasoningChain listReasoningChain){
        this.agents = listReasoningChain.getAgents();
        this.consortiumMap = new HashMap<>();

        for (Map.Entry<Agent, ReasoningChain> entry : listReasoningChain.getListReasoningChain().entrySet()) {
            ReasoningChain rc = entry.getValue();
            if (this.consortiumMap.containsKey(rc)) {
                // If we have a consortium for this reasoning chain, we just add the agent
                this.consortiumMap.get(rc).add(entry.getKey());
            } else {
                // If not, we create a new consortium
                Consortium consortium = new Consortium(rc);
                Set<Agent> agreeingAgents = new HashSet<>();
                agreeingAgents.add(entry.getKey());
                consortium.setAgents(agreeingAgents);
                consortiumMap.put(consortium, null);
            }
        }

        // Now that we have created all the consortiums, we can assign their types
        for (Consortium consortium : consortiumMap.keySet()) {
            ConsortiumType consortiumType = determineConsortiumType(consortium); // This would be a method that determines the type of the consortium based on the definitions you provided
            consortiumMap.put(consortium, consortiumType);
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
