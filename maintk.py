import tkinter as tk
from tkinter import filedialog
import json

import agent
import proposition


class MainWindow(tk.Frame):
    def __init__(self, master):
        super().__init__(master)
        self.master = master
        self.master.geometry("600x600")  # ustawienie rozmiaru okna
        left_frame = tk.Frame(self.master, width=400, height=600)
        left_frame.pack(side=tk.LEFT)

        right_frame = tk.Frame(self.master, width=200, height=600)
        right_frame.pack(side=tk.RIGHT)
        self.listAgent = []
        self.listProposition = []
        self.listValues = []
        self.load_json_label = tk.Label(left_frame, text="Dodaj plik, który został wcześniej \nwygenerowany\n\n", font=("Arial", 13))
        self.load_json_label.pack()

        self.load_json_button = tk.Button(left_frame, text="json", command=self.load_json)
        self.load_json_button.pack()

        self.add_values_label = tk.Label(right_frame, text="Dodaj wartości dla sprawy sądowej\n\n", font=("Arial", 13))
        self.add_values_label.pack()

        self.add_values_button = tk.Button(right_frame, text="Dodaj wartości", command=self.manual_input)
        self.add_values_button.pack()


    def load_json(self):
        # kod wczytujący plik JSON i tworzący widok
        filename = tk.filedialog.askopenfilename(title="Wybierz plik JSON")
        if filename:
            with open(filename, "r") as f:
                data = json.load(f)
                new_window = tk.Toplevel(self.master)
                new_window.geometry("400x400")
                tk.Label(new_window, text=json.dumps(data, indent=4)).pack()

    def manual_input(self):
        new_window = tk.Toplevel(self.master)  # nowe okno
        new_window.geometry("600x600")  # ustawienie rozmiaru okna

        # Dodanie etykiet i pól tekstowych
        agent_label = tk.Label(new_window, text="Lista Agentów")
        agent_label.pack()
        agent_input = tk.Text(new_window, width=40, height=10)
        agent_input.pack()

        proposition_label = tk.Label(new_window, text="Lista Propozycji")
        proposition_label.pack()
        proposition_input = tk.Text(new_window, width=40, height=10)
        proposition_input.pack()

        value_label = tk.Label(new_window, text="Lista Wartości")
        value_label.pack()
        value_input = tk.Text(new_window, width=40, height=10)
        value_input.pack()

        # Dodanie przycisku zatwierdzającego wprowadzone dane
        confirm_button = tk.Button(new_window, text="Zatwierdź",
                                   command=lambda: self.confirm_input(agent_input, proposition_input, value_input,
                                                                      self.listAgent, new_window))
        confirm_button.pack()

        # Dodanie tablicy z listą agentów
        agents_frame = tk.Frame(new_window)
        agents_frame.pack()
        agents_label = tk.Label(agents_frame, text="Dodani Agenci")
        agents_label.pack()
        agents_listbox = tk.Listbox(agents_frame, width=40, height=10)
        agents_listbox.pack()
        for agent in self.listAgent:
            agents_listbox.insert(tk.END, f"{agent.getName()} - {agent.weight}")

        # Dodanie przycisku dodającego nowych agentów do tablicy
        add_agent_button = tk.Button(new_window, text="Dodaj Agenta",
                                     command=lambda: self.add_agent(agent_input, agents_listbox, self.listAgent))
        add_agent_button.pack()

    def confirm_input(self, agent_input, proposition_input, value_input, listAgent, new_window):
        # pobranie wartości z pól tekstowych i zapisanie ich w odpowiednich listach
        for line in agent_input.get("1.0", "end-1c").split("\n"):
            if line:
                name, weight = line.split(",")
                try:
                    weight = int(weight)
                except ValueError:
                    weight = "?"
                new_agent = agent.Agent(name.strip(), weight)
                listAgent.append(new_agent)

        for line in proposition_input.get("1.0", "end-1c").split("\n"):
            if line:
                statement, weight = line.split(",")
                try:
                    weight = int(weight)
                except ValueError:
                    weight = "?"
                new_proposition = proposition.Proposition(statement.strip(), weight)

    def add_agent(self, agent_input, agents_listbox, listAgent):
        name, weight = agent_input.get("1.0", "end-1c").split(",")
        try:
            weight = int(weight)
        except ValueError:
            weight = "?"
        new_agent = agent.Agent(name.strip(), weight)
        listAgent.append(new_agent)
        agents_listbox.insert(tk.END, f"{new_agent.getName()} - {new_agent.weight}")
        agent_input.delete("1.0", tk.END)

    def man_in_AgentValueToWeight(self):
        #3 okienko, wprowadzenie ValueToWeight do wartosci agenta
        pass

    def man_in_AgentPropValueToWeight(self):
        #4 Wprowadzenie danych  do AgentPropValueToWeight każdego agenta
        pass

    def man_in_PropBaseClean(self):
        #5 Wyliczenie PropBase Clean (schodzi sie tu i od json i od wlasnego, wiec tutaj takze mozna stworzyć od wprowadzonych danych json
        pass
if __name__ == "__main__":
    root = tk.Tk()
    app = MainWindow(root)
    root.mainloop()