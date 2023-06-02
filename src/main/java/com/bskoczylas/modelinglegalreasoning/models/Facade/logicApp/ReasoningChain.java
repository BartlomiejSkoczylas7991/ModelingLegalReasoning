package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import javafx.util.Pair;

import java.util.*;

public class ReasoningChain {

    public Pair<Set<Rule>, Proposition> subjectiveReasoningChain(Agent agent, KnowledgeBase subjectiveKB,
                                                                 Proposition prop, Set<Rule> rules,
                                                                 Map<Agent, PropBaseClean> propBaseCleanResult,
                                                                 IncompProp incompProp) {
        Set<Proposition> pi = subjectiveKB.getPropositions();
        Set<Rule> rj = (Set<Rule>) subjectiveKB.getRules();

        Set<Proposition> visited = new HashSet<>();
        Deque<Pair<Set<Proposition>, Iterator<Rule>>> stack = new ArrayDeque<>();
        stack.push(new Pair<>(pi, rj.iterator()));

//        while (!stack.isEmpty()) {
//            Pair<Proposition, Iterator<Rule>> nodeChildren = stack.peek();
//            Proposition node = nodeChildren.getKey();
//            Iterator<Rule> children = nodeChildren.getValue();
//
//            if (!propBaseCleanResult.get(agent).getPropositions(agent).contains(node)) {
//                return new Pair<>(Collections.emptySet(), null);
//            }
//
//            Rule child = children.hasNext() ? children.next() : null;
//
//            if (child == null) {
//                stack.pop();
//                visited.remove(node);
//            } else if (child.getConclusion().equals(node) && !visited.contains(child.getPremises())) {
//                if (child.getPremises().stream().anyMatch(p -> incompProp.containsPair(p, node))) {
//                    return new Pair<>(Collections.emptySet(), null);
//                }
//
//                stack.push(new Pair<>(child.getPremises(), rj.iterator()));
//                visited.add(child.getPremises());
//                //Rozpoznać problem
//            }
//        }

        Set<Rule> minimalKB = new HashSet<>();
        for (Rule rule : rj) {
            if (visited.contains(rule.getConclusion())) {
                minimalKB.add(rule);
            }
        }

        if (stack.size() != new HashSet<>(stack).size()) {
            return new Pair<>(Collections.emptySet(), null);
        }

        return new Pair<>(minimalKB, prop);
    }
}
