package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase.KnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class ReasoningChain {
    private KnowledgeBase knowledgeBase;
    private Proposition decision;

    public ReasoningChain(KnowledgeBase knowledgeBase, Proposition decision) {
        this.knowledgeBase = knowledgeBase;
        this.decision = decision;
    }

    public ReasoningChain(){
        this.knowledgeBase = new KnowledgeBase();
    }

    public KnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }

    public Proposition getDecision() {
        return decision;
    }

    public void setDecision(Proposition decision) {
        this.decision = decision;
    }

    public void addRule(Rule rule) {
        this.knowledgeBase.getRj().add(rule);
        updatePi();
    }

    public void removeRuleAndAllAfter(Rule rule) {
        int index = knowledgeBase.getRj().indexOf(rule);
        if (index != -1) {
            knowledgeBase.getRj().subList(index, knowledgeBase.getRj().size()).clear();
            updatePi();
        }
    }

    private void updatePi() {
        Set<Proposition> updatedPi = new HashSet<>();
        for (Rule rule : knowledgeBase.getRj()) {
            updatedPi.addAll(rule.getPremises());
            updatedPi.add(rule.getConclusion());
        }
        knowledgeBase.setPi(updatedPi);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReasoningChain that = (ReasoningChain) o;
        return Objects.equals(knowledgeBase, that.knowledgeBase) &&
                Objects.equals(decision, that.decision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(knowledgeBase, decision);
    }

    @Override
    public String toString() {
        return "<<" + knowledgeBase.toString() + ", " + (decision != null ? decision.toString() : "not decided") + ">>";
    }
}
