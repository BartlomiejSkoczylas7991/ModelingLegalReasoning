package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp;

import com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures.Pair;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.IncompPropObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ListIncompPropTest {
    @Test
    void testAddingIncompatiblePropositions() {
        // Tworzymy mocka obserwatora
        IncompPropObserver mockObserver = Mockito.mock(IncompPropObserver.class);
        ListIncompProp list = new ListIncompProp();
        list.addObserver(mockObserver);

        // Dodajemy niezgodne propozycje
        Proposition prop1 = new Proposition("Prop1");
        Proposition prop2 = new Proposition("Prop2");
        list.addIncompatiblePropositions(new IncompProp(new Pair<>(prop1, prop2), false));

        // Sprawdzanie, czy niezgodne propozycje są poprawnie dodane
        assertTrue(list.containsPair(prop1, prop2));

        // Sprawdzanie, czy obserwator został powiadomiony
        verify(mockObserver, times(1)).updateIncomp(list); // używamy verify, aby upewnić się, że metoda została wywołana
    }

    @Test
    void testSettingDecision() {
        // Tworzymy mocka obserwatora
        IncompPropObserver mockObserver = Mockito.mock(IncompPropObserver.class);
        ListIncompProp list = new ListIncompProp();
        list.addObserver(mockObserver);

        // Tworzenie par niezgodnych propozycji
        Proposition prop1 = new Proposition("Prop1");
        Proposition prop2 = new Proposition("Prop2");
        IncompProp incompProp1 = new IncompProp(new Pair<>(prop1, prop2), false);
        list.addIncompatiblePropositions(incompProp1);

        // Ustawienie decyzji
        list.setDecision(incompProp1);

        // Sprawdzanie, czy decyzja została poprawnie ustawiona
        assertEquals(incompProp1, list.getDecision());

        // Ustawienie innej decyzji
        Proposition prop3 = new Proposition("Prop3");
        Proposition prop4 = new Proposition("Prop4");
        IncompProp incompProp2 = new IncompProp(new Pair<>(prop3, prop4), false);
        list.addIncompatiblePropositions(incompProp2);
        list.setDecision(incompProp2);

        // Sprawdzanie, czy nowa decyzja zastąpiła starą
        assertEquals(incompProp2, list.getDecision());
        assertNotEquals(incompProp1, list.getDecision());

        // Sprawdzanie, czy obserwator został powiadomiony
        verify(mockObserver, times(4)).updateIncomp(list); // używamy verify, aby upewnić się, że metoda została wywołana dwa razy
    }

    @Test
    void testRemovingIncompatiblePropositions() {
        ListIncompProp list = new ListIncompProp();

        Proposition prop1 = new Proposition("Prop1");
        Proposition prop2 = new Proposition("Prop2");
        IncompProp incompProp = new IncompProp(new Pair<>(prop1, prop2), false);
        list.addIncompatiblePropositions(incompProp); // Dodawanie pary do listy

        // Usuwanie niezgodnych propozycji
        list.removeIncompProp(incompProp);

        // Sprawdzanie, czy niezgodne propozycje zostały prawidłowo usunięte
        assertFalse(list.containsPair(prop1, prop2));
    }

    @Test
    void testContainsPair() {
        ListIncompProp list = new ListIncompProp();
        Proposition prop1 = new Proposition("Prop1");
        Proposition prop2 = new Proposition("Prop2");

        IncompProp incompProp = new IncompProp(new Pair<>(prop1, prop2), false);
        list.addIncompatiblePropositions(incompProp); // Dodawanie pary do listy

        // Sprawdzanie, czy para zawiera się na liście
        assertTrue(list.containsPair(prop1, prop2));
    }

    @Test
    void testDecisionsExist() {
        ListIncompProp list = new ListIncompProp();
        Proposition prop1 = new Proposition("Prop1");
        Proposition prop2 = new Proposition("Prop2");
        assertFalse(list.decisionsExist());
        IncompProp incompProp = new IncompProp(new Pair<>(prop1, prop2), false);
        list.addIncompatiblePropositions(incompProp); // Dodawanie pary do listy
        assertFalse(list.decisionsExist()); // Brak decyzji na liście

        // Ustawienie decyzji
        list.setDecision(incompProp);
        // Sprawdzanie, czy decyzja istnieje na liście
        assertTrue(list.decisionsExist());
    }

    @Test
    void testRemovingDecisionProp() {
        ListIncompProp list = new ListIncompProp();
        Proposition prop1 = new Proposition("Prop1");
        Proposition prop2 = new Proposition("Prop2");
        IncompProp incompProp = new IncompProp(new Pair<>(prop1, prop2), false);
        list.addIncompatiblePropositions(incompProp); // Dodawanie pary do listy

        // Usuwanie decyzji związanej z konkretną propozycją
        list.removeDecisionProp(prop1);

        // Sprawdzanie, czy decyzja została usunięta
        assertNull(list.getDecision());
    }

    @Test
    void testObserver() {
        IncompPropObserver mockObserver = Mockito.mock(IncompPropObserver.class);
        ListIncompProp list = new ListIncompProp();
        list.addObserver(mockObserver);

        Proposition prop1 = new Proposition("Prop1");
        Proposition prop2 = new Proposition("Prop2");
        IncompProp incompProp = new IncompProp(new Pair<>(prop1, prop2), false);
        list.addIncompatiblePropositions(incompProp); // Dodawanie pary do listy

        // Sprawdzanie, czy obserwator został powiadomiony
        verify(mockObserver, times(1)).updateIncomp(list);
        list.removeObserver(mockObserver);

        // Resetowanie mocka
        Mockito.reset(mockObserver);

        list.removeDecision(incompProp);

        // Sprawdzanie, czy obserwator nie został powiadomiony po usunięciu
        verify(mockObserver, times(0)).updateIncomp(list); // obserwator nie powinien zostać powiadomiony
    }
}