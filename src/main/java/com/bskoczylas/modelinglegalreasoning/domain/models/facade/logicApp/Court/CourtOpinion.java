package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Court;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Consortium.Consortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Consortium.ConsortiumType;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Consortium.ListConsortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.ReasoningChain.ReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.CourtOpinionObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ConsortiumObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.CourtOpinionObserver;

import java.util.*;

public class CourtOpinion implements ConsortiumObserver, CourtOpinionObservable {
    private Proposition decision;
    private Map<ReasoningChain, Set<Agent>> majorityOpinions = new HashMap<>();
    private Map<ReasoningChain, Set<Agent>> pluralityOpinions = new HashMap<>();
    private Map<ReasoningChain, Set<Agent>> concurringOpinions = new HashMap<>();
    private Map<ReasoningChain, Set<Agent>> dissentingOpinions = new HashMap<>();
    private ListConsortium listConsortium;
    private final List<CourtOpinionObserver> observers = new ArrayList<>();

    public CourtOpinion() {}

    // gettery i settery

    @Override
    public void updateCon(ListConsortium listConsortium) {
        this.listConsortium = listConsortium;
        // We go through each consortium and check its type
        for (Consortium consortium : listConsortium.getConsortiumMap().keySet()) {
            ConsortiumType type = listConsortium.getConsortiumMap().get(consortium);
            ReasoningChain rc = consortium.getReasoningChain();
            Set<Agent> agents = consortium.getAgents();

            switch (type) {
                case MAJORITY:
                    majorityOpinions.put(rc, agents);
                    break;
                case PLURALITY:
                    pluralityOpinions.put(rc, agents);
                    break;
                case CONCURRING:
                    concurringOpinions.put(rc, agents);
                    break;
                case DISSENTING:
                    dissentingOpinions.put(rc, agents);
                    break;
            }
        }

        // Finally, we frame the court's decision based on the largest majority or plurality opinion
        Map.Entry<ReasoningChain, Set<Agent>> largestMajorityOpinion = findLargestOpinion(majorityOpinions);
        Map.Entry<ReasoningChain, Set<Agent>> largestPluralityOpinion = findLargestOpinion(pluralityOpinions);

        if (largestMajorityOpinion != null) {
            this.decision = largestMajorityOpinion.getKey().getDecision();
        } else if (largestPluralityOpinion != null) {
            this.decision = largestPluralityOpinion.getKey().getDecision();
        }
    }

    private Map.Entry<ReasoningChain, Set<Agent>> findLargestOpinion(Map<ReasoningChain, Set<Agent>> opinions) {
        return opinions.entrySet().stream()
                .max(Comparator.comparingInt(e -> e.getValue().size()))
                .orElse(null);
    }

    @Override
    public void addObserver(CourtOpinionObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(CourtOpinionObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(CourtOpinionObserver observer : observers) {
            observer.updateCourtOpinion(this);
        }
    }
}
