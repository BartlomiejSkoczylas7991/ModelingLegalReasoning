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
    private List<Consortium> majorityOpinions = new ArrayList<>();
    private List<Consortium> pluralityOpinions = new ArrayList<>();
    private List<Consortium> concurringOpinions = new ArrayList<>();
    private List<Consortium> dissentingOpinions = new ArrayList<>();
    private ListConsortium listConsortium;
    private final List<CourtOpinionObserver> observers = new ArrayList<>();

    public CourtOpinion() {}

    @Override
    public void updateCon(ListConsortium listConsortium) {
        this.listConsortium = listConsortium;
        // We go through each consortium and check its type
        majorityOpinions.clear();
        pluralityOpinions.clear();
        concurringOpinions.clear();
        dissentingOpinions.clear();
        for (Consortium consortium : listConsortium.getConsortiumMap().keySet()) {
            ConsortiumType type = listConsortium.getConsortiumMap().get(consortium);
            switch (type) {
                case MAJORITY:
                    majorityOpinions.add(consortium);
                    break;
                case PLURALITY:
                    pluralityOpinions.add(consortium);
                    break;
                case CONCURRING:
                    concurringOpinions.add(consortium);
                    break;
                case DISSENTING:
                    dissentingOpinions.add(consortium);
                    break;
            }
        }

        // Finally, we frame the court's decision based on the largest majority or plurality opinion
        Consortium largestMajorityOpinion = findLargestOpinion(majorityOpinions);
        Consortium largestPluralityOpinion = findLargestOpinion(pluralityOpinions);

        if (largestMajorityOpinion != null) {
            this.decision = largestMajorityOpinion.getReasoningChain().getDecision();
        } else if (largestPluralityOpinion != null) {
            this.decision = largestPluralityOpinion.getReasoningChain().getDecision();
        }
        notifyObservers();
    }

    private Consortium findLargestOpinion(List<Consortium> opinions) {
        return opinions.stream()
                .max(Comparator.comparingInt(e -> e.getAgents().size()))
                .orElse(null);
    }

    public ListConsortium getListConsortium() {
        return listConsortium;
    }

    public Proposition getDecision() {
        return this.decision;
    }

    public List<Consortium> getMajorityOpinions() {
        return this.majorityOpinions;
    }

    public List<Consortium> getPluralityOpinions() {
        return this.pluralityOpinions;
    }

    public List<Consortium> getConcurringOpinions() {
        return this.concurringOpinions;
    }

    public List<Consortium> getDissentingOpinions() {
        return this.dissentingOpinions;
    }

    public void setDecision(Proposition decision) {
        this.decision = decision;
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

    @Override
    public String toString() {
        return "CourtOpinion{" +
                "decision=" + decision +
                ", majorityOpinions=" + majorityOpinions +
                ", pluralityOpinions=" + pluralityOpinions +
                ", concurringOpinions=" + concurringOpinions +
                ", dissentingOpinions=" + dissentingOpinions +
                ", listConsortium=" + listConsortium +
                ", observers=" + observers +
                '}';
    }
}
