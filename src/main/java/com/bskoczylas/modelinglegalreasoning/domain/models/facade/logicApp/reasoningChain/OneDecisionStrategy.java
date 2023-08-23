package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase.KnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;

import java.util.HashSet;
import java.util.Set;

public class OneDecisionStrategy implements ReasoningChainStrategy{
    @Override
    public ReasoningChain calculate(KnowledgeBase KB) {
        ReasoningChain result;
        Set<Proposition> propBaseClean = KB.getPi();
        Set<Rule> listRules = KB.getRj();
        Proposition decision = new Proposition("");
        for (Proposition proposition : propBaseClean){
            if(proposition.isDecision()){
                decision = proposition;
            }
        }
        Set<Proposition> newPropBaseClean = new HashSet<>();
        Set<Rule> newListRules  = new HashSet<>();

        boolean rulesForDecisionExist = false;
        for (Rule rule : listRules) {
            if(rule.getConclusion().equals(decision)) {
                boolean allPremisesContains = true;
                for (Proposition premise : rule.getPremises()) {
                    if (!propBaseClean.contains(premise)){
                        allPremisesContains = false;
                        break;
                    }
                }
                if (allPremisesContains) {
                    newPropBaseClean.addAll(rule.getPremises());
                    newListRules.add(rule);
                }
                rulesForDecisionExist = true;
            }
        }
        if(rulesForDecisionExist) {
            KnowledgeBase newKB = new KnowledgeBase(newPropBaseClean, newListRules);
            result = new ReasoningChain(newKB, decision);

        } else {
            Proposition noDecision = null;
            Set<Proposition> propositions = new HashSet<>();
            Set<Rule> rules = new HashSet<>();
            KnowledgeBase minimalKB = new KnowledgeBase(propositions, rules);
            result = new ReasoningChain(minimalKB, noDecision);
        }
        return result;
    }
}
