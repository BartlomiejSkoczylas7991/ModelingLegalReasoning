import agent, proposition, value
import json
from typing import List

if __name__ == "__main__":
    load_json = input("Czy chcesz wczytać plik JSON? (tak/nie): ")
    if load_json == "tak":
        filename = input("Podaj nazwę pliku JSON: ")
        with open(filename, "r") as f:
            data = json.load(f)
        listAgent = [agent.Agent(agent["name"], agent["weight"]) for agent in data["agents"]]
        listProposition = [proposition.Proposition(prop["statement"], prop["weight"]) for prop in data["propositions"]]
        listValues = [value.Value(val["statement"], val["weight"]) for val in data["values"]]
        for agent, agent_data in zip(listAgent, data["agents"]):
            for val, val_weight in agent_data["values"].items():
                agent.addValues(val, val_weight)
            for prop, prop_data in agent_data["propositions"].items():
                for val, val_weight in prop_data.items():
                    value = next((v for v in listValues if v.getName() == val), None)
                    proposition = next((p for p in listProposition if p.getStatement() == prop), None)
                    agent.addpropositionsWeight(value, proposition, val_weight)
        incompPropositions = [[next((prop for prop in listProposition if prop.getStatement() == p[0]), None),
                               next((prop for prop in listProposition if prop.getStatement() == p[1]), None)]
                              for p in data["incompatible_propositions"]]
    else:
        listAgent = []
        while True:
            response = input("Czy chcesz dodać nowego agenta? (tak/nie): ")
            if response == "nie":
                break

            name = input("Podaj imię agenta:")
            try:
                weight = input("Podaj wagę agenta: ")
                if weight is not "?":
                    weight = int(weight)
                break
            except ValueError:
                print("Wprowadzono nieprawidłową wartość. Spróbuj ponownie.")
            new_agent = agent.Agent(name, weight)
            listAgent.append(new_agent)
        print("Lista Agentów: ", [str(agent) for agent in listAgent])
        listProposition = []
        while True:
            response = input("Czy chcesz dodać nową propozycję? (tak/nie): ")
            if response == "nie":
                break

            statement = input("Podaj nazwę propozycji:")
            try:
                weight = input("Podaj wagę propozycji:")
                if weight is not "?":
                    weight = int(weight)
                break
            except ValueError:
                print("Wprowadzono nieprawidłową wartość. Spróbuj ponownie.")
            new_proposition = proposition.Proposition(statement, weight)
            listProposition.append(new_proposition)

        print("Lista Propozycji: ", [str(prop) for prop in listProposition])

        listValues = []
        while True:
            response = input("Czy chcesz dodać nową wartość? (tak/nie): ")
            if response == "nie":
                break
            statement = input("Podaj nazwę wartości:")
            try:
                weight = input("Podaj wagę wartości:")
                if weight is not "?":
                    weight = int(weight)
                break
            except ValueError:
                print("Wprowadzono nieprawidłową wartość. Spróbuj ponownie.")
            new_value = value.Value(statement, weight)
            listValues.append(new_value)
        print("Lista wartości: ", [str(value) for value in listValues])

        # Dodanie wartości do agentów wraz z wagami:
        # Iterujemy
        print(
            "Teraz iterujemy po Agentach dodając wagi do ich wartości, które wcześniej zdefiniowaliśmy. [AgentValuetoWeight]")
        for agent in listAgent:
            print("Dostosuj wagi dla agenta:", agent.name)
            for value in listValues:
                weight = int(input(f"Podaj wagę dla wartości {value.getName()} (liczba całkowita lub ?): "))
                agent.addValues(value.getName(), weight)

        # Dodanie wartości do propozycji wraz z wagami:
        print("Teraz iterujemy po wartościach dodając wagi do ich propozycji, które wcześniej zdefiniowaliśmy.")
        print("Jeśli wartość sprzyja stwierdzeniu, to dajemy liczbę dodatnią, jeśli nie, to ujemną.")
        IDagpropval = []
        iter = 1
        for agent in listAgent:
            for proposition in listProposition:
                for value in listValues:
                    try:
                        weight = input(
                            f"Podaj wagę dla propozycji {proposition.getStatement()} odnośnie wartości {value.getName()} agenta {agent.name} (liczba całkowita lub ?): ")
                        weight = int(weight)
                        break
                    except ValueError:
                        print("Wprowadzono nieprawidłową wartość. Spróbuj ponownie.")
                    IDagpropval.append([iter, weight])
                    iter += 1
                    agent.addpropositionsWeight(value, proposition, weight)
        # napisać o stosunku propozycji do

        print("Napisz które wartości nie przystają do siebie:")
        incompPropositions = []
        while True:
            input_str = input(
                "Wybierz propozycje, ktore maja byc nieadekwatne (nazwa1, nazwa2), lub wpisz 'q' aby zakonczyc: ")
            if input_str == "q":
                break
            prop_names = input_str.split(",")
            p1 = next((prop for prop in listProposition if prop.getStatement() == prop_names[0]), None)
            p2 = next((prop for prop in listProposition if prop.getStatement() == prop_names[1]), None)
            if [p2, p1] not in incompPropositions and [p1, p2] not in incompPropositions:
                incompPropositions.append([p1, p2])

        # Napisać prop BaseClean dla każdego ag
        # enta pisząc tabelę AgentValuePropWeight
        # już mamy dane AgentValuetoWeight, jak również i
        iter = 1
        for agent in listAgent:
            for prop in listProposition:
                waga_prop = 0
                warun = False
                for value in listValues:
                    if agent.getValue(value) != "?" or type(
                            agent.getValue(value)) is int or value.getpropositionsWeight(value) != "?" or type(
                            value.getpropositionsWeight(value)) is int:
                        waga_prop = 0
                        roz = 0
                        roz = agent.getAgentPropValuetoWeight()[iter][3] - agent.getValue(value)
                        waga_prop += roz
                        warun = True
                    iter = + 1
                if waga_prop >= 0 and warun:
                    agent.addPropBaseClean(prop)
                    warun = False

        print("PropBaseClean dla każdego agenta - czyli pasujące argumenty według wag wartosci agentów: ")
        for agent in listAgent:
            print(agent.getName(), ": ", agent.getPropBaseClean())
