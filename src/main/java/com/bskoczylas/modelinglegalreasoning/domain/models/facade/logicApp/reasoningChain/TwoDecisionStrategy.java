package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase.KnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;

import java.util.*;

public class TwoDecisionStrategy implements ReasoningChainStrategy{
    @Override
    public ReasoningChain calculate(KnowledgeBase KB) {
        ReasoningChain result;
        List<Proposition> possibleDecisions = new ArrayList<>();
        Set<Proposition> propBaseClean = KB.getPi();
        Set<Rule> listRules = KB.getRj();

        for (Proposition proposition : propBaseClean){
            if(proposition.isDecision()){
                possibleDecisions.add(proposition);
            }
        }
        int votes1 = 0;
        int votes2 = 0;
        Set<Proposition> newPropBaseClean1 = new HashSet<>();
        Set<Rule> newListRules1  = new HashSet<>();
        Set<Proposition> newPropBaseClean2 = new HashSet<>();
        Set<Rule> newListRules2  = new HashSet<>();

        Proposition finalDecision;
        for (Rule rule : listRules) {
            if(rule.getConclusion().equals(possibleDecisions.get(0))) {
                boolean allPremisesContains = true;
                int count = 0;
                for (Proposition premise : rule.getPremises()) {
                    count++;
                    if (!propBaseClean.contains(premise)){
                        allPremisesContains = false;
                        break;
                    }
                }
                if (allPremisesContains) {
                    votes1 = votes1 + count;
                    newPropBaseClean1.addAll(rule.getPremises());
                    newListRules1.add(rule);
                }
            } else {
                boolean allPremisesContains = true;
                int count = 0;
                for (Proposition premise : rule.getPremises()) {
                    count++;
                    if (!propBaseClean.contains(premise)){
                        allPremisesContains = false;
                        break;
                    }
                }
                if (allPremisesContains) {
                    votes2 = votes2 + count;
                    newPropBaseClean2.addAll(rule.getPremises());
                    newListRules2.add(rule);
                }
            }
        }

        if (votes1 > votes2) {
            finalDecision = possibleDecisions.get(0);
            KnowledgeBase newKB1 = new KnowledgeBase(newPropBaseClean1, newListRules1);
            result = new ReasoningChain(newKB1, finalDecision);
        }
        else if (votes2 > votes1) {
            finalDecision = possibleDecisions.get(1);
            KnowledgeBase newKB2 = new KnowledgeBase(newPropBaseClean2, newListRules2);
            result = new ReasoningChain(newKB2, finalDecision);
        } else {
            Random random = new Random();
            int randomDecisionIndex = random.nextInt(2);
            finalDecision = possibleDecisions.get(randomDecisionIndex);

            if (randomDecisionIndex == 0) {
                KnowledgeBase newKB1 = new KnowledgeBase(newPropBaseClean1, newListRules1);
                result = new ReasoningChain(newKB1, finalDecision);
            } else {
                KnowledgeBase newKB2 = new KnowledgeBase(newPropBaseClean2, newListRules2);
                result = new ReasoningChain(newKB2, finalDecision);
            }
        }

        return result;
    }
}
