package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase.KnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;

import java.util.HashSet;
import java.util.Set;

public class ZeroDecisionStrategy implements ReasoningChainStrategy{
    @Override
    public ReasoningChain calculate(KnowledgeBase KB) {
        Proposition noDecision = null;
        Set<Proposition> propositions = new HashSet<>();
        Set<Rule> rules = new HashSet<>();
        KnowledgeBase minimalKB = new KnowledgeBase(propositions, rules);
        ReasoningChain emptyReasoningChain = new ReasoningChain(minimalKB, noDecision);

        return emptyReasoningChain;
    }
}
