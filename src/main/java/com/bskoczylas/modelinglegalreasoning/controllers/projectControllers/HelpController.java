package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class HelpController {
    @FXML
    private TextArea helpTextArea;

    public void initialize() {
        String helpText = "Wprowadzenie\n" +
                "Witaj w programie do modelowania wnioskowania prawniczego! Jest to narzędzie stworzone na podstawie pracy naukowej autorstwa dwóch naukowców: Wynera i Żurka. Dzięki niemu będziesz mógł analizować skomplikowane procesy wnioskowania prawniczego w przejrzysty i efektywny sposób.\n" +
                "\n" +
                "Struktura Programu\n" +
                "Projekt\n" +
                "Agenci: Wprowadź nazwę agenta.\n" +
                "Propozycje: Wprowadź nazwę oraz zdecyduj, czy jest to decyzja ostateczna.\n" +
                "Wartości: Wprowadź nazwę wartości.\n" +
                "Wagi: Zdefiniuj wagi dla różnych kombinacji Agent-Wartość i Agent-Wartość-Propozycja.\n" +
                "Zasady: Utwórz zasady bazujące na zestawach propozycji (premises) oraz wybranych propozycjach (decisions).\n" +
                "Niepasujące pary propozycji: Zdefiniuj niekompatybilne ze sobą pary propozycji.\n" +
                "Jak to działa?\n" +
                "Definiowanie PropBaseClean: Na podstawie wprowadzonych wag, tworzymy zestaw PropBaseClean dla każdego agenta.\n" +
                "Tworzenie Bazy Wiedzy: Na podstawie PropBaseClean oraz stworzonych zasad tworzymy bazy wiedzy dla agentów.\n" +
                "Łańcuch Rozumowania: Dla każdego agenta tworzymy łańcuch rozumowania, korzystając z nowo utworzonej bazy wiedzy.\n" +
                "Głosowanie: Po analizie decyzji każdego agenta, przeprowadzane jest głosowanie wśród wszystkich agentów.\n" +
                "Tworzenie Konsorcjów: Na podstawie wyników głosowania, tworzone są różne konsorcja agentów.\n" +
                "Wymagania i Zasady\n" +
                "Możesz wygenerować raport tylko wtedy, gdy spełnione są następujące wymagania:\n" +
                "\n" +
                "1. Minimum 2 agenci.\n" +
                "2. Minimum 2 wartości.\n" +
                "3. Minimum 4 propozycje.\n" +
                "4. Minimum 2 zasady.\n" +
                "5. Dwie propozycje będące decyzjami zdefiniowane w okienku IncompProp.\n" +
                "\nDodatkowe Zasady:\n" +
                "1. Ustalając wagi dla AgentValuePropWeight pamiętaj, że wystarczy jedna waga większa do odpowiadającej wartości w AgentValueToWeight by dana propozycaj przeszła przez filter.\n" +
                "2. Propozycje zdecydowane jako 'decyzje ostateczne' nie mogą być używane przy premises w zasadach.\n" +
                "7. Propozycje, które nie są używane przy ustalaniu zasad (będąc w premises) nie będą brane pod uwagę w wartościach.\n" +
                "8. Unikaj tworzenia zbyt skomplikowanych łańcuchów rozumowania, które mogą sprawić, że analiza stanie się nieczytelna i trudna do zrozumienia.\n" +
                "9. Regularne zapisywanie postępów jest kluczem do uniknięcia potencjalnych problemów z utratą danych." +
                "\n" +
                "Nadawaj unikalne nazwy dla agentów, wartości i propozycji.\n" +
                "Nie możesz dodawać sprzecznych zasad.\n" +
                "I wiele innych zasad, które zostały opisane w wyższej części tego tekstu (od punktu 4 do 9).\n" +
                "Wskazówki i Porady\n" +
                "Użyj funkcji 'Help' w dowolnym momencie, jeśli masz pytania lub wątpliwości.\n" +
                "Zacznij od zdefiniowania podstawowych elementów, takich jak agenci, propozycje i wartości, zanim przejdziesz do bardziej skomplikowanych zadań, takich jak tworzenie zasad czy ustalanie wag.\n";

        helpTextArea.setText(helpText);
    }
}
