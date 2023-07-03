package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.ReasoningChain;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.KnowledgeBase.KnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Proposition.Proposition;

import java.util.Objects;

public class ReasoningChain {
    private KnowledgeBase knowledgeBase;
    private Proposition decision;

    public ReasoningChain(KnowledgeBase knowledgeBase, Proposition decision) {
        this.knowledgeBase = knowledgeBase;
        this.decision = decision;
    }
    public ReasoningChain(Proposition decision) {
        this.knowledgeBase = new KnowledgeBase();
        this.decision = decision;
    }

    public KnowledgeBase getKnowledgeBase() {
        return knowledgeBase;
    }

    public Proposition getDecision() {
        return decision;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReasoningChain that = (ReasoningChain) o;
        return Objects.equals(knowledgeBase, that.knowledgeBase) && Objects.equals(decision, that.decision);
    }

    @Override
    public int hashCode() {
        return Objects.hash(knowledgeBase, decision);
    }
}
