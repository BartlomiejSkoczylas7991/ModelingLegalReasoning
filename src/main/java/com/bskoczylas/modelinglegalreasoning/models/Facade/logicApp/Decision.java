package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.observables.Decision_Observable;
import com.bskoczylas.modelinglegalreasoning.models.observers.RC_Observer;
import javafx.util.Pair;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Decision implements Decision_Observable, RC_Observer {
    private HashMap<Proposition, Iterator> pp;
    private HashMap<Proposition, Iterator> pd;
    private int sum_votes;
    private Proposition decision;

    public Decision(Set<Agent> agents) {
        int ppCount = 0;
        int pdCount = 0;

        for (Agent agent : agents) {
            Proposition conclusion = agent.getReasoningChain().getConclusion();
            if (conclusion.equals(Proposition.PP)) {
                ppCount++;
            } else if (conclusion.equals(Proposition.PD)) {
                pdCount++;
            }
        }

        if (ppCount > pdCount) {
            decision = Proposition.PP;
        } else {
            decision = Proposition.PD;
        }
    }

    public Proposition getDecision() {
        return this.decision;
    }

    @Override
    public void updateRC(ListReasoningChain listReasoningChain) {
        for (Map.Entry<Agent, ReasoningChain> entry : listReasoningChain.getListReasoningChain().entrySet()){

        }
    }
}
