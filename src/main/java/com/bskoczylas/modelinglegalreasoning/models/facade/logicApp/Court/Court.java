package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Court;

import com.bskoczylas.modelinglegalreasoning.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Consortium.Consortium;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Consortium.ListConsortium;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Decision.Decision;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.KnowledgeBase.KnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers.Consortium_Observer;

import java.util.Set;

public class Court implements Consortium_Observer {
    private Decision decision;
    private Set<Agent> majorityJudges;
    private Set<Agent> dissentingJudges;
    private Pair<KnowledgeBase, Proposition> majorityOpinion;
    private Pair<KnowledgeBase, Proposition> dissentingOpinion;

    public Court(Consortium consortium) {
    }

    @Override
    public void updateCon(ListConsortium listConsortium) {

    }
}
