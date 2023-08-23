package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;

import java.util.Set;

public class ReasoningChainStrategyFactory {
    public ReasoningChainStrategy createStrategy(Set<Proposition> propBaseClean) {
        int decisionsCount = countDecisions(propBaseClean);
        switch (decisionsCount) {
            case 0: return new ZeroDecisionStrategy();
            case 1: return new OneDecisionStrategy();
            case 2: return new TwoDecisionStrategy();
            default: throw new IllegalArgumentException("Nieobsługiwana liczba decyzji: " + decisionsCount);
        }
    }

    private int countDecisions(Set<Proposition> propBaseClean){
        int count = 0;
        for (Proposition proposition : propBaseClean){
            if (proposition.isDecision()){
                count++;
            }
        }
        return count;
    }
}
