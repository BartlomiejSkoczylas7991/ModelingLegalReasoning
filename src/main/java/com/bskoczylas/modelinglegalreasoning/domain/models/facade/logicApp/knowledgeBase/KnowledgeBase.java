package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;

import java.util.*;
import java.util.stream.Collectors;

public class KnowledgeBase {
    private Set<Proposition> pi;
    private List<Rule> rj;;

    public KnowledgeBase(Set<Proposition> pi, List<Rule> rj) {
        this.pi = pi;
        this.rj = rj;
    }

    public KnowledgeBase(){
        this.pi = new HashSet<Proposition>();
        this.rj = new ArrayList<>();
    }

    public Set<Proposition> getPi() {
        return pi;
    }

    public List<Rule> getRj() {
        return rj;
    }

    public void setPi(Set<Proposition> pi) {
        this.pi = pi;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        KnowledgeBase that = (KnowledgeBase) o;
        return Objects.equals(pi, that.pi) && Objects.equals(rj, that.rj);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pi, rj);
    }

    @Override
    public String toString() {
        String propositions = pi.stream()
                .map(Proposition::getStatement)
                .collect(Collectors.joining(", "));

        String rules = rj.stream()
                .map(Rule::toString) // Assuming toString() is properly defined in Rule
                .collect(Collectors.joining(", "));

        return "{" + propositions + ", " + rules + "}";
    }
}
