package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.observers.Consortium_Observer;
import javafx.util.Pair;

import java.util.Set;

public class Court implements Consortium_Observer {
    private Decision decision;
    private Set<Agent> majorityJudges;
    private Set<Agent> dissentingJudges;
    private Pair<KnowledgeBase, Proposition> majorityOpinion;
    private Pair<KnowledgeBase, Proposition> dissentingOpinion;

    public Court(Consortium consortium) {
        this.decision = new Decision(consortium.getAgents());
        this.majorityJudges = consortium.getMajorityAgents(decision);
        this.dissentingJudges = consortium.getDissentingAgents(decision);

        for (Pair<KnowledgeBase, Proposition> key : consortium.getConsortiumMap().keySet()) {
            if (key.getSecond().equals(decision.getDecision()) && consortium.getConsortiumMap().get(key).equals(majorityJudges)) {
                this.majorityOpinion = key;
            } else if (consortium.getConsortiumMap().get(key).equals(dissentingJudges)) {
                this.dissentingOpinion = key;
            }
        }
    }

    @Override
    public void updateCon(Consortium consortium) {

    }
}
