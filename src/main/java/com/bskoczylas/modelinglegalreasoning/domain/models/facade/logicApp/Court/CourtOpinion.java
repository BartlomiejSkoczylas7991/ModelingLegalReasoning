package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Court;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Consortium.Consortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Consortium.ConsortiumType;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Consortium.ListConsortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.ReasoningChain.ReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.ConsortiumObserver;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CourtOpinion implements ConsortiumObserver, CourtOpinionObservable {
    private Proposition decision;
    private Map<ReasoningChain, Set<Agent>> majorityOpinions;
    private Map<ReasoningChain, Set<Agent>> pluralityOpinions;
    private Map<ReasoningChain, Set<Agent>> concurringOpinions;
    private Map<ReasoningChain, Set<Agent>> dissentingOpinions;
    private ListConsortium listConsortium;

    public CourtOpinion() {
        majorityOpinions = new HashMap<>();
        pluralityOpinions = new HashMap<>();
        concurringOpinions = new HashMap<>();
        dissentingOpinions = new HashMap<>();
    }

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
}
