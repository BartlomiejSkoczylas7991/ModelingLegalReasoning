package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.KnowledgeBase;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule.Rule;

import java.util.HashSet;
import java.util.Set;

public class KnowledgeBase {
    private Set<Proposition> pi;
    private Set<Rule> rj;

    public KnowledgeBase(Set<Proposition> pi, Set<Rule> rj) {
        this.pi = pi;
        this.rj = rj;
    }

    public KnowledgeBase(){
        this.pi = new HashSet<Proposition>();
        this.rj = new HashSet<Rule>();
    }

    public Set<Proposition> getPi() {
        return pi;
    }

    public Set<Rule> getRj() {
        return rj;
    }

    public void addPropToPBC(Proposition proposition){
        this.pi.add(proposition);
    }

    public void addRuleToPBC(Rule rule){
        this.rj.add(rule);
    }
}
