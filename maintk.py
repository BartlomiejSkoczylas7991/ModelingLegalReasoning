import json
import tkinter as tk
from tkinter import filedialog, ttk
import agent as ag
import proposition
import value


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
                self.master = tk.Toplevel(self.master)
                self.master.geometry("400x400")
                tk.Label(self.master, text=json.dumps(data, indent=4)).pack()

    def manual_input(self):
        for widget in self.master.winfo_children():
            widget.destroy()
        self.master.geometry("1550x800")  # ustawienie rozmiaru okna

        # Dodanie etykiet i pól tekstowych
        #tu dodam tablice dodanych juz obiektów klasy Agent z self.listAgent
        # Dodanie tabeli z agentami

        agent_frame = tk.Frame(self.master, height=400, width=300)
        agent_frame.pack(side=tk.LEFT, padx=50)
        agents_table = ttk.Treeview(agent_frame, columns=('Name', 'Weight'))
        agents_table.heading('#0', text='ID',)
        agents_table.heading('Name', text='Nazwa')
        agents_table.heading('Weight', text='Waga')
        agents_table.column('#0', width=20)
        agents_table.column('Name', width=100)
        agents_table.column('Weight', width=30)
        agents_table.pack(side=tk.LEFT, padx=10)

        agent_label = tk.Label(agent_frame, text="Dodaj nazwę agenta")
        agent_label.pack()
        agent_name = tk.Text(agent_frame, width=12, height=1)
        agent_name.pack()
        agent_label = tk.Label(agent_frame, text="Dodaj wagę agenta")
        agent_label.pack()
        agent_weight = tk.Text(agent_frame, width=12, height=1)
        agent_weight.pack()
        #tabela dodanych agentów

        add_agent_button = tk.Button(agent_frame, text="Dodaj Agenta",
                                     command=lambda: self.add_agent(agent_name, agent_weight, agents_table))
        add_agent_button.pack()

        proposition_frame = tk.Frame(self.master, height=400, width=300)
        proposition_frame.pack(side=tk.LEFT, padx=50)
        proposition_table = ttk.Treeview(proposition_frame, columns=('Name', 'Weight'))
        proposition_table.heading('#0', text='ID')
        proposition_table.heading('Name', text='Nazwa')
        proposition_table.heading('Weight', text='Waga')
        proposition_table.column('#0', width=20)
        proposition_table.column('Name', width=100)
        proposition_table.column('Weight', width=30)
        proposition_table.pack(side=tk.LEFT, padx=10)

        proposition_label = tk.Label(proposition_frame, text="Dodaj nazwe Propozycji")
        proposition_label.pack()
        proposition_name = tk.Text(proposition_frame, width=12, height=1)
        proposition_name.pack()
        proposition_label = tk.Label(proposition_frame, text="Dodaj wagę agenta")
        proposition_label.pack()
        proposition_weight = tk.Text(proposition_frame, width=12, height=1)
        proposition_weight.pack()
        add_proposition_button = tk.Button(proposition_frame, text="Dodaj Propozycję",
                                     command=lambda: self.add_proposition(proposition_name, proposition_weight, proposition_table))
        add_proposition_button.pack()


        value_frame = tk.Frame(self.master, height=400, width=300)
        value_frame.pack(side=tk.RIGHT, padx=50)
        value_table = ttk.Treeview(value_frame, columns=('Name', 'Weight'))
        value_table.heading('#0', text='ID')
        value_table.heading('Name', text='Nazwa')
        value_table.heading('Weight', text='Waga')
        value_table.column('#0', width=20)
        value_table.column('Name', width=100)
        value_table.column('Weight', width=30)
        value_table.pack(side=tk.LEFT, padx=10)

        value_label = tk.Label(value_frame, text="Dodaj nazwe Wartosci")
        value_label.pack()
        value_name = tk.Text(value_frame, width=12, height=1)
        value_name.pack()
        value_label = tk.Label(value_frame, text="Dodaj wagę wartosci")
        value_label.pack()
        value_weight = tk.Text(value_frame, width=12, height=1)
        value_weight.pack()
        add_value_button = tk.Button(value_frame, text="Dodaj Wartosc",
                                           command=lambda: self.add_value(value_name, value_weight, value_table))
        add_value_button.pack()

        # Dodanie przycisku zatwierdzającego wprowadzone dane
        button_frame = tk.Frame(self.master, height=50)
        button_frame.pack(side=tk.BOTTOM, pady=10, fill=tk.X)

        confirm_button = tk.Button(button_frame, text="Zatwierdź", command=lambda: self.confirm_input())
        confirm_button.pack(side=tk.RIGHT, padx=10)

    def confirm_input(self):
        pass
        # pobranie wartości z pól tekstowych i zapisanie ich w odpowiednich listach
        #name, weight = line.split(",")
        #try:
        #    weight = int(weight)
        #except ValueError:
        #    weight = "?"
        #new_agent = ag.Agent(name.strip(), weight)
        #listAgent.append(new_agent)

        #statement, weight = line.split(",")
        #try:
        #    weight = int(weight)
        #except ValueError:
        #    weight = "?"
        #new_proposition = proposition.Proposition(statement.strip(), weight)

    def add_agent(self, agent_name, agent_weight, agents_table):
        if not agent_weight.get('1.0', 'end-1c').isdigit():
            agent_weight = "?"
        else:
            agent_weight = agent_weight.get('1.0', 'end-1c').strip()
        agent_name = agent_name.get('1.0', 'end-1c').strip()
        new_agent = ag.Agent(agent_name, agent_weight)
        self.listAgent.append(new_agent)
        agents_table.insert('', tk.END, text=str(len(self.listAgent)),
                            values=( new_agent.getName(), new_agent.getWeight()))

    def add_proposition(self, proposition_name, proposition_weight, propositions_table):
        if not proposition_weight.get('1.0', 'end-1c').isdigit():
            proposition_weight = "?"
        else:
            proposition_weight = proposition_weight.get('1.0', 'end-1c').strip()
        proposition_name = proposition_name.get('1.0', 'end-1c').strip()
        new_proposition = proposition.Proposition(proposition_name, proposition_weight)
        self.listProposition.append(new_proposition)
        propositions_table.insert('', tk.END, text=str(len(self.listProposition)),
                            values=(new_proposition.getStatement(), new_proposition.getWeight()))

    def add_value(self, value_name, value_weight, values_table):
        if not value_weight.get('1.0', 'end-1c').isdigit():
            value_weight = "?"
        else:
            value_weight = value_weight.get('1.0', 'end-1c').strip()
        value_name = value_name.get('1.0', 'end-1c').strip()
        new_value = value.Value(value_name, value_weight)
        self.listAgent.append(new_value)
        values_table.insert('', tk.END, text=str(len(self.listValues)),
                            values=(new_value.getName(), new_value.getWeight()))

        # wyświetlenie listy agentów w liście
        self.agents_listbox.delete(0, tk.END)
        for agent in self.listAgent:
            self.agents_listbox.insert(tk.END, agent.getName())



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