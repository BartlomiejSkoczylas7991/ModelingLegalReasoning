package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.lists.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observables.Consortium_Observable;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.Consortium_Observer;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.Decision_Observer;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.RC_Observer;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


// Set Agentów, w całkowiciej zgodzie co do następującej konkluzji (to samo RC agentów)
public class Consortium implements RC_Observer, Consortium_Observable, Decision_Observer {
    private Map<ReasoningChain, Set<Agent>> consortiumMap;
    private ListReasoningChain listReasoningChain;
    private Set<Agent> agents;

    private Consortium() {
        consortiumMap = new HashMap<>();

        for (Agent agent : agents) {
            ReasoningChain key = listReasoningChain.getRC_ByAgent(agent);
            if (consortiumMap.containsKey(key)) {
                consortiumMap.get(key).add(agent);
            } else {
                Set<Agent> agentSet = new HashSet<>();
                agentSet.add(agent);
                consortiumMap.put(key, agentSet);
            }
        }
    }

    public Set<Agent> getMajorityAgents(Decision decision) {
        Set<Agent> majorityAgents = new HashSet<>();

        for (Map.Entry<ReasoningChain, Set<Agent>> entry : consortiumMap.entrySet()) {
            if (entry.getKey().getDecision().equals(decision.getDecision())) {
                if (entry.getValue().size() > majorityAgents.size()) {
                    majorityAgents = entry.getValue();
                }
            }
        }

        return majorityAgents;
    }

    public Set<Agent> getDissentingAgents(Decision decision) {
        Set<Agent> dissentingAgents = new HashSet<>();

        for (Map.Entry<Pair<KnowledgeBase, Proposition>, Set<Agent>> entry : consortiumMap.entrySet()) {
            if (!entry.getKey().getSecond().equals(decision.getDecision())) {
                dissentingAgents.addAll(entry.getValue());
            }
        }

        return dissentingAgents;
    }

    public Map<Pair<KnowledgeBase, Proposition>, Set<Agent>> getConsortiumMap() {
        return this.consortiumMap;
    }

    @Override
    public void updateRC(ListReasoningChain listReasoningChain) {
        this.listReasoningChain = listReasoningChain;
        this.agents = listReasoningChain.getAgents();
    }

    @Override
    public void addObserver(Consortium_Observer observer) {

    }

    @Override
    public void removeObserver(Consortium_Observer observer) {

    }

    @Override
    public void notifyObservers() {

    }

    @Override
    public void update(Decision decision) {

    }
}
