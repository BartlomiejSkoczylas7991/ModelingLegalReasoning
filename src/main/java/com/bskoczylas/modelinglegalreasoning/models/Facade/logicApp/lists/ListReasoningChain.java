package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.lists;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.*;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observables.RC_Observable;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.IncompProp_Observer;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.KB_Observer;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.observers.RC_Observer;
import javafx.util.Pair;

import java.util.*;

public class ListReasoningChain implements KB_Observer, IncompProp_Observer, RC_Observable {
    private Set<Agent> agents;
    private HashMap<Agent, ReasoningChain> listReasoningChain;
    private ListKnowledgeBase listKnowledgeBase;
    private Set<Pair<Proposition, Proposition>> incompProp; //dwie sprzeczne ze sobą propozycji;
    private Pair<Proposition, Proposition> decisions;
    private List<RC_Observer> observers;

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
                    // Jeżeli jest propozycja, której nie ma w propBaseClean,
                    // usuń regułę z minimalKB i usuń propozycję z visited
                    ruleIterator.remove();
                    visited.remove(premise);
                    break;
                }
            }
        }
    }

    private void resolveIncompProp(Set<Rule> minimalKB, Set<Proposition> visited) {
        for (Pair<Proposition, Proposition> incompPair : incompProp) {
            if (visited.contains(incompPair.getKey()) && visited.contains(incompPair.getValue())) {
                // Jeżeli obie sprzeczne propozycje są w zbiorze odwiedzonych, usuń jedną z nich
                if (minimalKB.size() > 1) {
                    // Usuń zasadę, która prowadzi do mniej licznej propozycji
                    Rule toRemove = null;
                    for (Rule rule : minimalKB) {
                        if (rule.getConclusion().equals(incompPair.getKey()) || rule.getConclusion().equals(incompPair.getValue())) {
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
        Queue<Proposition> queue = new LinkedList<>();
        queue.add(finalProposition);
        while (!queue.isEmpty()) {
            // ... (rest of code for finding minimalKB)
        }
        return minimalKB;
    }



    private ReasoningChain calculateWithVoting(Agent agent) {
        // Pobierz subiektywną bazę wiedzy dla danego agenta
        KnowledgeBase subjectiveKB = listKnowledgeBase.getKnowledgeBase(agent);
        Set<Proposition> propBaseClean = listKnowledgeBase.getPropBaseClean().getAgentPropBaseClean(agent);

        // Zainicjalizuj zbiór odwiedzonych propozycji i minimalną bazę wiedzy
        Set<Proposition> visited = new HashSet<>();
        Set<Rule> minimalKB = new HashSet<>();

        // Calculate votes
        Map<Proposition, Integer> votes = calculateVotes(agent, subjectiveKB, propBaseClean);

        // Jeżeli nie ma żadnych końcowych propozycji, zwróć pusty łańcuch rozumowania
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

        // Zwróć łańcuch rozumowania
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
                // Dodaj propozycje z antecedentu reguły do zbioru odwiedzonych propozycji
                visited.addAll(rule.getPremises());
            }
        }

        // Usuń reguły, których premises nie są obecne w propBaseClean
        minimalKB.removeIf(rule -> !propBaseClean.containsAll(rule.getPremises()));
        return minimalKB;
    }

    public Set<Agent> getAgents() {
        return agents;
    }

    @Override
    public void updateIncomp(ListIncompProp listIncompProp) {
        incompProp = listIncompProp.getIncompProp();
        decisions = listIncompProp.getDecisions();
        calculateReasoningChain();
    }

    @Override
    public void updateKB(ListKnowledgeBase knowledgeBase) {
        this.listKnowledgeBase = knowledgeBase;
        this.agents = knowledgeBase.getAgents();

        calculateReasoningChain();
        //dodawanie agentów

    }

    @Override
    public void addObserver(RC_Observer observer) {
        observers.add(observer);
    }

    @Override
    public void removeObserver(RC_Observer observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers() {
        for(RC_Observer observer : observers){
            observer.updateRC(this);
        }
    }

    public ReasoningChain getRC_ByAgent(Agent agent) {
        return listReasoningChain.get(agent);
    }
}
