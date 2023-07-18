package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables.RCObservable;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.KBObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.RCObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase.KnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.Rule;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase.ListKnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.IncompProp;

import java.util.*;
import java.util.stream.Collectors;

public class ListReasoningChain implements KBObserver, RCObservable {
    private Set<Agent> agents;
    private HashMap<Agent, ReasoningChain> listReasoningChain;
    private ListKnowledgeBase listKnowledgeBase;
    private Set<Pair<Proposition, Proposition>> incompProp; // two inconsistent propositions;
    private Pair<Proposition, Proposition> decisions;
    private List<RCObserver> observers = new ArrayList<>();

    public ListReasoningChain() {}

    private Map<Proposition, Integer> calculateVotes(Agent agent, KnowledgeBase subjectiveKB, Set<Proposition> propBaseClean) {
        Map<Proposition, Integer> votes = new HashMap<>();
        for (Rule rule : subjectiveKB.getRj()) {
            Proposition conclusion = rule.getConclusion();
            if (propBaseClean.contains(conclusion) && conclusion.isDecision()) {
                votes.put(conclusion, votes.getOrDefault(conclusion, 0) + 1);
            }
        }
        return votes;
    }

    private Proposition findFinalProposition(Map<Proposition, Integer> votes) {
        return Collections.max(votes.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    private void removeInconsistentRules(Set<Rule> minimalKB, Set<Proposition> visited, Set<Proposition> propBaseClean) {
        Iterator<Rule> ruleIterator = minimalKB.iterator();
        while (ruleIterator.hasNext()) {
            Rule rule = ruleIterator.next();
            for (Proposition premise : rule.getPremises()) {
                if (!propBaseClean.contains(premise)) {
                    // if there is a proposition that is not in propBaseClean
                    // remove rule from minimalKnowledgeBase and remove proposition from visited
                    ruleIterator.remove();
                    visited.remove(premise);
                    break;
                }
            }
        }
    }

    private void resolveIncompProp(Set<Rule> minimalKB, Set<Proposition> visited) {
        for (Pair<Proposition, Proposition> incompPair : incompProp) {
            if (visited.contains(incompPair.getFirst()) && visited.contains(incompPair.getSecond())) {
                // If both conflicting suggestions are in the visited list, delete one of them
                if (minimalKB.size() > 1) {
                    // Remove the rule that leads to fewer proposals
                    Rule toRemove = null;
                    for (Rule rule : minimalKB) {
                        if (rule.getConclusion().equals(incompPair.getFirst()) || rule.getConclusion().equals(incompPair.getSecond())) {
                            toRemove = rule;
                            break;
                        }
                    }
                    minimalKB.remove(toRemove);
                    visited.remove(toRemove.getConclusion());
                } else {
                    throw new RuntimeException("Inconsistent propositions in reasoning chain");
                }
            }
        }
    }

    private Set<Rule> findMinimalKB(Proposition finalProposition, KnowledgeBase subjectiveKB, Set<Proposition> propBaseClean, Set<Proposition> visited) {
        Set<Rule> minimalKB = new HashSet<>();
        Stack<Proposition> stack = new Stack<>();
        stack.push(finalProposition);
        while (!stack.isEmpty()) {
            Proposition currentProp = stack.pop();
            for (Rule rule : subjectiveKB.getRj()) {
                // If this rule concludes the currentProp and all premises are in the clean prop base, add to minimalKB
                if (rule.getConclusion().equals(currentProp) && propBaseClean.containsAll(rule.getPremises())) {
                    minimalKB.add(rule);
                    visited.addAll(rule.getPremises());
                    // Add all the premises to the stack for further investigation
                    for (Proposition premise : rule.getPremises()) {
                        if (!visited.contains(premise)) {
                            stack.push(premise);
                        }
                    }
                }
            }
        }
        return minimalKB;
    }

    private ReasoningChain calculateWithVoting(Agent agent) {
        // Get the subjective knowledge base for a given agent
        KnowledgeBase subjectiveKB = listKnowledgeBase.getKnowledgeBase(agent);
        Set<Proposition> propBaseClean = listKnowledgeBase.getPropBaseClean().getAgentPropBaseClean(agent);

        // Initialize a set of visited propositions and the minimum knowledge base
        Set<Proposition> visited = new HashSet<>();
        Set<Rule> minimalKB = new HashSet<>();

        // Calculate votes
        Map<Proposition, Integer> votes = calculateVotes(agent, subjectiveKB, propBaseClean);

        // if there are no final propositions, return an empty reasoning string
        if (votes.isEmpty()) {
            return new ReasoningChain(new KnowledgeBase(new HashSet<>(), new HashSet<>()), null);
        }

        // Find the final proposition
        Proposition finalProposition = findFinalProposition(votes);

        // Dodaj końcową propozycję do zbioru odwiedzonych propozycji
        visited.add(finalProposition);

        // Znajdź minimalną bazę wiedzy potrzebną do wywnioskowania końcowej propozycji
        minimalKB = findMinimalKB(finalProposition, subjectiveKB, propBaseClean, visited);

        // Remove inconsistent rules
        removeInconsistentRules(minimalKB, visited, propBaseClean);

        // Resolve incompProp
        resolveIncompProp(minimalKB, visited);

        // Twórz łańcuch rozumowania, dodając do niego reguły z minimalnej bazy wiedzy
        ReasoningChain reasoningChain = new ReasoningChain(new KnowledgeBase(visited, minimalKB), finalProposition);

        // check if finalProposition is in Pair of decisions
        if(!decisions.getFirst().equals(finalProposition) && !decisions.getSecond().equals(finalProposition)) {
            throw new RuntimeException("Final proposition is not a valid decision");
        }

        // Return the chain of reasoning
        return reasoningChain;
    }

    public HashMap<Agent, ReasoningChain> getListReasoningChain() {
        return listReasoningChain;
    }

    private void calculateReasoningChain() {
        if(!agents.isEmpty()) {
            for (Agent agent : agents) {
                listReasoningChain.put(agent, calculateWithVoting(agent));
            }
        }
    }

    private Set<Rule> createMinimalKB(Set<Rule> rules, Set<Proposition> visited, Set<Proposition> propBaseClean) {
        Set<Rule> minimalKB = new HashSet<>();
        for (Rule rule : rules) {
            if (visited.contains(rule.getConclusion()) && propBaseClean.contains(rule.getConclusion())) {
                minimalKB.add(rule);
                // Add suggestions from the antecedent of the rule to the set of visited proposals
                visited.addAll(rule.getPremises());
            }
        }

        // Remove rules whose premises are not present in propBaseClean
        minimalKB.removeIf(rule -> !propBaseClean.containsAll(rule.getPremises()));

        return minimalKB;
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    public void setAgents(Set<Agent> agents) {
        this.agents = agents;
    }

    public void setListReasoningChain(HashMap<Agent, ReasoningChain> listReasoningChain) {
        this.listReasoningChain = listReasoningChain;
    }

    public ListKnowledgeBase getListKnowledgeBase() {
        return listKnowledgeBase;
    }

    public void setListKnowledgeBase(ListKnowledgeBase listKnowledgeBase) {
        this.listKnowledgeBase = listKnowledgeBase;
    }

    public Set<Pair<Proposition, Proposition>> getIncompProp() {
        return incompProp;
    }

    public void setIncompProp(Set<Pair<Proposition, Proposition>> incompProp) {
        this.incompProp = incompProp;
    }

    public Pair<Proposition, Proposition> getDecisions() {
        return decisions;
    }

    public void setDecisions(Pair<Proposition, Proposition> decisions) {
        this.decisions = decisions;
    }

    public List<RCObserver> getObservers() {
        return observers;
    }

    public void setObservers(List<RCObserver> observers) {
        this.observers = observers;
    }

    @Override
    public void updateKB(ListKnowledgeBase knowledgeBase) {
        this.listKnowledgeBase = knowledgeBase;
        this.agents = knowledgeBase.getAgents();
        this.incompProp = knowledgeBase.getRules().getListIncompProp()
                .getIncompatiblePropositions()
                .stream()
                .map(IncompProp::getPropositionsPair)
                .collect(Collectors.toSet());
        this.decisions = knowledgeBase.getRules().getListIncompProp().getDecisions();

        calculateReasoningChain();
    }

    @Override
    public void addObserver(RCObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(RCObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(RCObserver observer : observers){
            observer.updateRC(this);
        }
    }

    public ReasoningChain getReasoningChainByAgent(Agent agent) {
        return listReasoningChain.get(agent);
    }

    @Override
    public String toString() {
        return listReasoningChain.entrySet().stream()
                .map(entry -> entry.getKey().getName() + " = " + entry.getValue().toString())
                .collect(Collectors.joining("\n"));
    }

}
