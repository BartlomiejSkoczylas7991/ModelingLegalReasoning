package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.court;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.consortium.Consortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.consortium.ConsortiumType;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.consortium.ListConsortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ReasoningChain;
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

    public ListConsortium getListConsortium() {
        return listConsortium;
    }

    public Proposition getDecision() {
        return this.decision;
    }

    public Map<ReasoningChain, Set<Agent>> getMajorityOpinions() {
        return this.majorityOpinions;
    }

    public Map<ReasoningChain, Set<Agent>> getPluralityOpinions() {
        return this.pluralityOpinions;
    }

    public Map<ReasoningChain, Set<Agent>> getConcurringOpinions() {
        return this.concurringOpinions;
    }

    public Map<ReasoningChain, Set<Agent>> getDissentingOpinions() {
        return this.dissentingOpinions;
    }

    public void setDecision(Proposition decision) {
        this.decision = decision;
    }

    public void setMajorityOpinions(Map<ReasoningChain, Set<Agent>> majorityOpinions) {
        this.majorityOpinions = majorityOpinions;
    }

    public void setPluralityOpinions(Map<ReasoningChain, Set<Agent>> pluralityOpinions) {
        this.pluralityOpinions = pluralityOpinions;
    }

    public void setConcurringOpinions(Map<ReasoningChain, Set<Agent>> concurringOpinions) {
        this.concurringOpinions = concurringOpinions;
    }

    public void setDissentingOpinions(Map<ReasoningChain, Set<Agent>> dissentingOpinions) {
        this.dissentingOpinions = dissentingOpinions;
    }

    public void setListConsortium(ListConsortium listConsortium) {
        this.listConsortium = listConsortium;
    }

    public List<CourtOpinionObserver> getObservers() {
        return observers;
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
