package com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp;

import com.bskoczylas.modelinglegalreasoning.models.observables.RC_Observable;
import com.bskoczylas.modelinglegalreasoning.models.observers.IncompProp_Observer;
import com.bskoczylas.modelinglegalreasoning.models.observers.KB_Observer;
import com.bskoczylas.modelinglegalreasoning.models.observers.RC_Observer;
import javafx.util.Pair;

import java.util.*;

public class ListReasoningChain implements KB_Observer, IncompProp_Observer, RC_Observable {
    private Set<Agent> agents;

    private HashMap<Agent, ReasoningChain> listReasoningChain;
    private ListKnowledgeBase listKnowledgeBase;
    private Set<Pair<Proposition, Proposition>> incompProp; //dwie sprzeczne ze sobą propozycje
    private List<RC_Observer> observers;

    private ReasoningChain calculateWithVoting(Agent agent) {
        // Pobierz subiektywną bazę wiedzy dla danego agenta
        KnowledgeBase subjectiveKB = listKnowledgeBase.getKnowledgeBase(agent);
        Set<Proposition> propBaseClean = listKnowledgeBase.getPropBaseClean().getAgentPropBaseClean(agent);

        // Zainicjalizuj zbiór odwiedzonych propozycji i minimalną bazę wiedzy
        Set<Proposition> visited = new HashSet<>();
        Set<Rule> minimalKB = new HashSet<>();

        // Stwórz mapę do zliczania głosów dla każdej propozycji
        Map<Proposition, Integer> votes = new HashMap<>();

        // Przejdź przez wszystkie zasady i zliczaj głosy dla każdej końcowej propozycji
        for (Rule rule : subjectiveKB.getRj()) {
            Proposition conclusion = rule.getConclusion();
            if (propBaseClean.contains(conclusion) && conclusion.isDecision()) {
                votes.put(conclusion, votes.getOrDefault(conclusion, 0) + 1);
            }
        }

        // Jeżeli nie ma żadnych końcowych propozycji, zwróć pusty łańcuch rozumowania
        if (votes.isEmpty()) {
            return new ReasoningChain(new KnowledgeBase(new HashSet<>(), new HashSet<>()), null);
        }

        // Znajdź propozycję z największą liczbą głosów
        Proposition finalProposition = Collections.max(votes.entrySet(), Map.Entry.comparingByValue()).getKey();

        // Dodaj końcową propozycję do zbioru odwiedzonych propozycji
        visited.add(finalProposition);

        // Znajdź minimalną bazę wiedzy potrzebną do wywnioskowania końcowej propozycji
        // Dodaj końcową propozycję do zbioru odwiedzonych propozycji
        visited.add(finalProposition);

        // Znajdź minimalną bazę wiedzy potrzebną do wywnioskowania końcowej propozycji
        Queue<Proposition> queue = new LinkedList<>();
        queue.add(finalProposition);

        while (!queue.isEmpty()) {
            Proposition prop = queue.poll();
            for (Rule rule : subjectiveKB.getRj()) {
                if (rule.getConclusion().equals(prop)) {
                    minimalKB.add(rule);
                    for (Proposition premise : rule.getPremises()) {
                        if (propBaseClean.contains(premise) && !visited.contains(premise)) {
                            visited.add(premise);
                            queue.add(premise);
                        }
                    }
                }
            }
        }

        Iterator<Rule> ruleIterator = minimalKB.iterator();
        while(ruleIterator.hasNext()) {
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

        // Sprawdź, czy istnieje sprzeczność między propozycjami
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

        // Twórz łańcuch rozumowania, dodając do niego reguły z minimalnej bazy wiedzy
        ReasoningChain reasoningChain = new ReasoningChain(new KnowledgeBase(visited, minimalKB), finalProposition);

        // Zwróć łańcuch rozumowania
        return reasoningChain;
    }


    //private ReasoningChain calculate(Agent agent) {//// Pobierz subiektywną bazę wiedzy dla danego agenta//KnowledgeBase subjectiveKB = listKnowledgeBase.getKnowledgeBase(agent);//Set<Proposition> propBaseClean = listKnowledgeBase.getPropBaseClean().getAgentPropBaseClean(agent)//// Przygotuj struktury danych//Set<Proposition> visited = new HashSet<>();//Deque<Pair<Set<Proposition>, Iterator<Rule>>> stack = new ArrayDeque<>()//Set<Proposition> propositions = propBaseClean;//if (propositions.isEmpty()) {
        //throw new IllegalStateException("No propositions available for agent.");
    //}
    //// Zainicjuj struktury danych - zaczynamy od początkowej propozycji
    //Proposition initialProp = propositions.iterator().next();
    //stack.push(new Pair<>(Collections.singleton(initialProp), subjectiveKB.getRj().iterator()))//// Główna pętla - kontynuuj, dopóki masz coś do zbadania//while (!stack.isEmpty()) {
    //Pair<Set<Proposition>, Iterator<Rule>> nodeChildren = stack.peek(); // błąd wymagający zastąpienia Pair<Proposition, Iterator<Rule>> na Pair<<Set<Proposition>, Uterator<Rule>>
    //Set<Proposition> node = nodeChildren.getKey();
    //Iterator<Rule> children = nodeChildren.getValue()//
    //// Sprawdzamy, czy propozycja w węźle należy do PropBaseClean_AgentA
    //if (!propBaseClean.contains(node)) {
        //return new ReasoningChain(null); // wymagany KnowledgeBase, a zawiera Set<>
    //
        //// Sprawdzamy, czy węzeł zawiera sprzeczne propozycje
        //for (Pair<Proposition, Proposition> pair : incompProp) {
            //if (node.contains(pair.getKey()) && node.contains(pair.getValue())) {
                //return new ReasoningChain(null);
            //}
        //    //// Iteruj przez reguły, dodaj wynikową propozycję do stosu i zaktualizuj listę odwiedzonych propozycji//while (children.hasNext()) {
            //Rule rule = children.next();
            //// Sprawdzamy, czy wszystkie propozycje dla tej reguły zostały już odwiedzone
            //// Jeżeli tak, pomijamy tę regułę
            //if (visited.containsAll(rule.getPremises())) {
                //continue;
            //}
            //// Dodajemy do odwiedzonych wszystkie propozycje z premises tej reguły
            //visited.addAll(rule.getPremises());
            //// Dodajemy do stosu wniosek (conclusion) z tej reguły
            //Set<Proposition> nextNode = new HashSet<>(node);
            //nextNode.add(rule.getConclusion());
            //stack.push(new Pair<>(nextNode, subjectiveKB.getRj().iterator()));
        //    //// Sprawdzamy, czy mamy cykle//// Jeżeli mamy, to zwracamy pustą parę//if (containsCycles(stack)) { //jeszcze nie mam containsCycles
            //return new ReasoningChain(Collections.emptySet(), null);
        //}
        //// TODO: Tutaj potrzebujesz kodu do iterowania przez `children` i dodawania kolejnych propozycji do `stack`,
        //// a także aktualizowania `visited` na podstawie reguł, które prowadzą do tych propozycji
    //    //// Sprawdzamy, czy mamy cykle//// Jeżeli mamy, to zwracamy pustą parę//if (containsCycles(stack)) {
        //return new Pair<>(Collections.emptySet(), null);
    //    //// Tworzymy minimalny zestaw propozycji i reguł//Set<Rule> minimalKB = createMinimalKB(subjectiveKB.getRules(), visited)//// Zwracamy wynik//return new Pair<>(minimalKB, /* TODO: Tu powinna być końcowa propozycja */);
    //}


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
        this.incompProp = listIncompProp.getIncompProp();
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
