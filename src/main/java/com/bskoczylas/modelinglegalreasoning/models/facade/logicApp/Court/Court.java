package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Court;

import com.bskoczylas.modelinglegalreasoning.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Consortium.Consortium;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Consortium.ConsortiumType;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Consortium.ListConsortium;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Decision.Decision;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Decision.Report;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.ReasoningChain.ReasoningChain;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.Consortium_Observer;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Court implements Consortium_Observer {
    private Proposition decision;
    private Set<Agent> majorityJudges;
    private Set<Agent> dissentingJudges;
    private Pair<ReasoningChain, Proposition> majorityOpinion;
    private Pair<ReasoningChain, Proposition> dissentingOpinion;
    private ListConsortium listConsortium;
    private Report report;

    public Court() {}

    public Proposition getDecision() {
        return decision;
    }

    public void setDecision(Proposition decision) {
        this.decision = decision;
    }

    public Set<Agent> getMajorityJudges() {
        return majorityJudges;
    }

    public void setMajorityJudges(Set<Agent> majorityJudges) {
        this.majorityJudges = majorityJudges;
    }

    public Set<Agent> getDissentingJudges() {
        return dissentingJudges;
    }

    public void setDissentingJudges(Set<Agent> dissentingJudges) {
        this.dissentingJudges = dissentingJudges;
    }

    public Pair<ReasoningChain, Proposition> getMajorityOpinion() {
        return majorityOpinion;
    }

    public void setMajorityOpinion(Pair<ReasoningChain, Proposition> majorityOpinion) {
        this.majorityOpinion = majorityOpinion;
    }

    public Pair<ReasoningChain, Proposition> getDissentingOpinion() {
        return dissentingOpinion;
    }

    public void setDissentingOpinion(Pair<ReasoningChain, Proposition> dissentingOpinion) {
        this.dissentingOpinion = dissentingOpinion;
    }

    public ListConsortium getListConsortium() {
        return listConsortium;
    }

    public Map<Consortium, ConsortiumType> getConsortiumMap() {
        return listConsortium.getConsortiumMap();
    }

    public void setListConsortium(ListConsortium listConsortium) {
        this.listConsortium = listConsortium;
    }

    @Override
    public void updateCon(ListConsortium listConsortium) {
        this.listConsortium = listConsortium;
        // We go through each consortium and check its type
        for (Consortium consortium : listConsortium.getConsortiumMap().keySet()) {
            ConsortiumType type = listConsortium.getConsortiumMap().get(consortium);

            switch (type) {
                case MAJORITY:
                    // In the case of a MAJORITY consortium, we line up consensual judges and majority opinion
                    this.majorityJudges = consortium.getAgents();
                    this.majorityOpinion = new Pair<>(consortium.getReasoningChain(), consortium.getReasoningChain().getDecision());
                    break;
                case DISSENTING:
                    // In the case of a DISSENTING consortium, we line up the opposing judges and the opposing opinion
                    this.dissentingJudges = consortium.getAgents();
                    this.dissentingOpinion = new Pair<>(consortium.getReasoningChain(), consortium.getReasoningChain().getDecision());
                    break;
                // For PLURALITY and CONCURRING we can decide what we want to do in our system
                case PLURALITY:
                case CONCURRING:
                    break;
            }
        }

        // Finally, we frame the court's decision based on the majority opinion
        if (this.majorityOpinion != null) {
            this.decision = this.majorityOpinion.getValue();
        }
    }
}
