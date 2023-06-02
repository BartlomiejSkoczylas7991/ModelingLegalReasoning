package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import javafx.util.Pair;
import java.util.*;

public class KnowledgeBase {

    private Set<Proposition> propBaseClean;
    private Set<Rule> rules;

    public KnowledgeBase(Set<Proposition> propBaseClean, Set<Rule> rules) {
        this.propBaseClean = propBaseClean;
        this.rules = rules;
    }

    public Set<Proposition> getPropositions() {
        return this.propBaseClean;
    }

    public Set<Rule> getRules() {
        return this.rules;
    }

    public Pair<Set<Proposition>, Set<Rule>> calculate() {
        Set<Proposition> pi = propBaseClean;
        Set<Rule> rj = new HashSet<>();
        for (Rule rule : rules) {
            for (Proposition prop : rule.getPremises()) {
                if (propBaseClean.contains(prop)) {
                    rj.add(rule);
                    break;
                }
            }
        }
        return new Pair<>(pi, rj);
    }
}