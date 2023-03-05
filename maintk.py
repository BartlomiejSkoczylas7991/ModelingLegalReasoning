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
        self.incompProp = []
        self.agents_tableAVW = ttk.Treeview(self.master, columns=('Name', 'Value', 'Weight'), height=20)
        self.agents_tableAPVW = ttk.Treeview(self.master, columns=('Name', 'Value', 'Weight'), height=20)
        self.load_json_label = tk.Label(left_frame, text="Dodaj plik, który został wcześniej \nwygenerowany\n\n", font=("Arial", 13))
        self.load_json_label.pack()
        self.load_json_button = tk.Button(left_frame, text="json", command=self.load_json)
        self.load_json_button.pack()
        self.add_values_label = tk.Label(right_frame, text="Dodaj wartości dla sprawy sądowej\n\n", font=("Arial", 13))
        self.add_values_label.pack()
        self.add_values_button = tk.Button(right_frame, text="Dodaj wartości", command=self.manual_input)
        self.add_values_button.pack()

        self.master.title("Modelowanie wnioskowania prawniczego")




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
        self.master.geometry("1150x600")  # ustawienie rozmiaru okna

        # Dodanie etykiet i pól tekstowych
        #tu dodam tablice dodanych juz obiektów klasy Agent z self.listAgent
        # Dodanie tabeli z agentami
        main_frame = tk.Frame(self.master, height= 200, width=1150)
        main_frame.pack(side=tk.TOP, padx=50)
        main_label = tk.Label(main_frame, text="Dodaj agentów, wartości i propozycje. ", font=("Arial", 22), justify='center')
        main_label.pack(pady=(50, 20))
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

        value_frame = tk.Frame(self.master, height=400, width=300)
        value_frame.pack(side=tk.LEFT, padx=20)
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

        proposition_frame = tk.Frame(self.master, height=400, width=300)
        proposition_frame.pack(side=tk.LEFT, padx=20)
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
        proposition_label = tk.Label(proposition_frame, text="Dodaj wagę propozycji")
        proposition_label.pack()
        proposition_weight = tk.Text(proposition_frame, width=12, height=1)
        proposition_weight.pack()
        add_proposition_button = tk.Button(proposition_frame, text="Dodaj Propozycję",
                                     command=lambda: self.add_proposition(proposition_name, proposition_weight, proposition_table))
        add_proposition_button.pack()

        # Dodanie przycisku zatwierdzającego wprowadzone dane
        button_frame = tk.Frame(self.master, height=300)
        button_frame.pack(side=tk.BOTTOM, pady=10, fill=tk.X)

        confirm_button = tk.Button(button_frame, text="Zatwierdź", command=lambda: self.confirm_input())
        confirm_button.pack(side=tk.RIGHT, padx=10)

    def confirm_input(self):
        for widget in self.master.winfo_children():
            widget.destroy()
        self.master.geometry("1150x600")  # ustawienie rozmiaru okna

        if not self.listAgent or not self.listProposition or not self.listValues:
            agent_label = tk.Label(self.master, text="\n\n\nNie możesz przejść dalej. Twoja tablica jest pusta. Uzupełnij tablice.", font=("Arial", 13))
            agent_label.pack()

            button_frame = tk.Frame(self.master, height=300)
            button_frame.pack(side=tk.BOTTOM, pady=10, fill=tk.X)

            confirm_button = tk.Button(button_frame, text="Zatwierdź", command=lambda: self.manual_input())
            confirm_button.pack(side=tk.RIGHT, padx=10)
        else:
            agent_label = tk.Label(self.master,
                                   text="\n\n\nUzupełniłeś wartości. Przechodzimy do uzupełniania wag dla wartosci agentów, a "
                                        "następnie do uzupełniania wag dla propozycji dla wartosci agentów. \n\n\n Czy są jakieś sprzeczne propozycje?", font=("Arial", 13))
            agent_label.pack()

            button_frame = tk.Frame(self.master, height=300)
            button_frame.pack(side=tk.BOTTOM, pady=10, fill=tk.X)

            confirm_button = tk.Button(button_frame, text="Tak", command=lambda: self.incompPropositions())
            confirm_button.pack(side=tk.RIGHT, padx=10)

            confirm_button = tk.Button(button_frame, text="Nie", command=lambda: self.man_in_AgentValueToWeight())
            confirm_button.pack(side=tk.RIGHT, padx=10)

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
        self.listValues.append(new_value)
        values_table.insert('', tk.END, text=str(len(self.listValues)),
                            values=(new_value.getName(), new_value.getWeight()))

    def incompPropositions(self):
        for widget in self.master.winfo_children():
            widget.destroy()
        self.master.geometry("400x300")
        label = tk.Label(self.master,
                         text="\n Podaj dwa sprzeczne propozycje.",
                         font=("Arial", 13))
        label.pack()
        # Dodaj listę rozwijaną dla wyboru pierwszej propozycji
        prop1_var = tk.StringVar(self.master)
        prop1_var.set(self.listProposition[0].getStatement())  # ustaw wartość domyślną
        labelp1 = tk.Label(self.master,
                         text="\n Propozycja 1: ")
        labelp1.pack()
        prop1_dropdown = tk.OptionMenu(self.master, prop1_var, *[prop.getStatement() for prop in self.listProposition])
        prop1_dropdown.pack()
        prop2_var = tk.StringVar(self.master)
        prop2_var.set(self.listProposition[0].getStatement())  # ustaw wartość domyślną
        labelp2 = tk.Label(self.master,
                         text="\n Propozycja 2: ")
        labelp2.pack()
        prop2_dropdown = tk.OptionMenu(self.master, prop2_var, *[prop.getStatement() for prop in self.listProposition])
        prop2_dropdown.pack()
        submit_button_incomp = tk.Button(self.master, text="Dodaj",
                                         command=lambda: self.submitIncomp(prop1_var.get(), prop2_var.get()))
        submit_button_incomp.pack(side=tk.BOTTOM, padx=10)
        submit_button_incomp_compl = tk.Button(self.master, text="Kontynuuj", command=lambda: self.man_in_AgentValueToWeight())
        submit_button_incomp_compl.pack(side=tk.BOTTOM, padx=10)

    def submitIncomp(self, prop1, prop2):
        p1 = next((prop for prop in self.listProposition if prop.getStatement() == prop1), None)
        p2 = next((prop for prop in self.listProposition if prop.getStatement() == prop2), None)

        # Dodaj do listy nieadekwatnych propozycji
        incomp_props = self.getIncompPropositions()
        if any(([p1, p2] == pair or [p2, p1] == pair) for pair in incomp_props):
            tk.messagebox.showerror("Błąd", "Te propozycje są już oznaczone jako nieadekwatne!")
            #self.incompPropositions();
        else:
            self.incompProp.append([p1, p2])
            #self.incompPropositions();

    def getIncompPropositions(self):
        return self.incompProp

    def getAgentByName(self, agentName):
        for i in self.listAgent:
            if str(i.getName) == str(agentName):
                return i

    def getValueByName(self, valueName):
        for i in self.listValues:
            if i.getName == valueName:
                return i

    def getPropositionByName(self, propositionName):
        for i in self.listProposition:
            if i.getStatement == propositionName:
                return i

    def man_in_AgentValueToWeight(self):
        for widget in self.master.winfo_children():
            widget.destroy()
        self.master.geometry("850x950")  # ustawienie rozmiaru okna

        mainAV = tk.Frame(self.master, height=700, width=400)
        mainAV.pack(side=tk.BOTTOM, padx=50, pady=10)
        main_label = tk.Label(mainAV,
                              text="Dodaj wagi dla wartości agentów. \nJest to waga do jakiej dany agent przywiązuje się\n z daną wartością. ",
                              font=("Arial", 19),
                              justify='center')
        main_label.pack(side=tk.TOP, padx=50, pady=20)
        agents_table_label = tk.Frame(mainAV)
        agents_table_label.pack(side=tk.TOP)
        self.agents_tableAVW = ttk.Treeview(agents_table_label, columns=('Name', 'Value', 'Weight'), height=20)
        self.agents_tableAVW.heading('#0', text='ID')
        self.agents_tableAVW.heading('Name', text='Nazwa')
        self.agents_tableAVW.heading('Value', text='Wartosc')
        self.agents_tableAVW.heading('Weight', text='Waga', anchor='center')
        self.agents_tableAVW.column('#0', width=30)
        self.agents_tableAVW.column('Name', width=130)
        self.agents_tableAVW.column('Value', width=100)
        self.agents_tableAVW.column('Weight', width=40)

        self.agents_tableAVW.grid(row=0, column=0, sticky='nsew')
        table_scroll = ttk.Scrollbar(agents_table_label, orient="vertical", command=self.agents_tableAVW.yview)
        table_scroll.grid(row=0, column=1, sticky='ns')
        self.agents_tableAVW.configure(yscrollcommand=table_scroll.set)

        for agent in self.listAgent:
            for value in self.listValues:
                agent.addValues(value.getName(), "?")
                self.agents_tableAVW.insert('', tk.END, text=str(len(self.listAgent)),
                                    values=(agent.getName(), value.getName(),"?"))

        def update_weight():
            iid = self.agents_tableAVW.focus()
            new_weight = edit.get()
            self.agents_tableAVW.set(iid, 'Weight', new_weight)
            agent_name = self.agents_tableAVW.item(iid, 'values')[0]
            value_name = self.agents_tableAVW.item(iid, 'values')[1]
            weight = int(new_weight)
            agent = self.getAgentByName(agent_name)
            if agent:
                agent.addValues(value_name, weight)
                #ogarnąć metodę getAgentByName

        edit_frame = tk.Frame(mainAV, height=200)
        edit_frame.pack(side=tk.TOP, padx=50, pady=10)
        label = tk.Label(edit_frame, text="Wciśnij wartość w kolumnie\n 'Waga' i edytuj wagę dla wartości\n wśród agentów:\n", font=("Arial", 13))
        label.pack(side=tk.BOTTOM, padx=50, pady=10)
        edit = tk.Entry(edit_frame)
        edit.pack(side=tk.BOTTOM, padx=50, pady=10)
        apply_button = tk.Button(edit_frame, text="Edytuj", command=update_weight, font=("Arial", 13))
        apply_button.pack(side=tk.BOTTOM, padx=50, pady=10)

        submit_button_ = tk.Button(mainAV, text="Kontynuuj",
                                               command=lambda: self.man_in_AgentPropValueToWeight())
        submit_button_.pack(side=tk.BOTTOM, padx=10, pady=10)

    def man_in_AgentPropValueToWeight(self):
        for widget in self.master.winfo_children():
            widget.destroy()
        self.master.geometry("850x900")  # ustawienie rozmiaru okna

        mainAPV = tk.Frame(self.master, height=700, width=450)
        mainAPV.pack(side=tk.BOTTOM, padx=50, pady=10)
        main_label = tk.Label(mainAPV,
                              text="Dodaj wagi dla relacji propozycji i wartości agentów. \nJest to waga do tego jak odnosi się dana propozycja \ndo danej wartości agenta.",
                              font=("Arial", 19),
                              justify='center')
        main_label.pack(side=tk.TOP, padx=50, pady=20)
        agents_table_label = tk.Frame(mainAPV)
        agents_table_label.pack(side=tk.TOP)
        self.agents_tableAPVW = ttk.Treeview(agents_table_label, columns=('Name', 'Value', 'Proposition', 'Weight'), height=20)
        self.agents_tableAPVW.heading('#0', text='ID')
        self.agents_tableAPVW.heading('Name', text='Nazwa')
        self.agents_tableAPVW.heading('Value', text='Wartosc')
        self.agents_tableAPVW.heading('Proposition', text='Propozycja')
        self.agents_tableAPVW.heading('Weight', text='Waga', anchor='center')
        self.agents_tableAPVW.column('#0', width=30)
        self.agents_tableAPVW.column('Name', width=200)
        self.agents_tableAPVW.column('Value', width=200)
        self.agents_tableAPVW.column('Proposition', width=170)
        self.agents_tableAPVW.column('Weight', width=40)

        self.agents_tableAPVW.grid(row=0, column=0, sticky='nsew')
        table_scroll = ttk.Scrollbar(agents_table_label, orient="vertical", command=self.agents_tableAVW.yview)
        table_scroll.grid(row=0, column=1, sticky='ns')
        self.agents_tableAPVW.configure(yscrollcommand=table_scroll.set)
        iter = 1
        for agent in self.listAgent:
            for prop in self.listProposition:
                waga_prop = 0
                warun = False
                for value in self.listValues:
                        agent.addagentPropValuetoWeight(prop.getStatement(), value.getName(), "?")
                        self.agents_tableAPVW.insert('', tk.END, text=str(len(self.listAgent)),
                                           values=(agent.getName(), value.getName(), prop.getStatement(), "?"))

        def update_weight():
            iid = self.agents_tableAPVW.focus()
            new_weight = edit.get()
            self.agents_tableAPVW.set(iid, 'Weight', new_weight)
            for iid in self.agents_tableAVW.get_children():
                agent_name = self.agents_tableAVW.item(iid, 'values')[0]
                agent = self.getAgentByName(agent_name)
                weight = self.agents_tableAVW.item(iid, 'values')[2]
                agent.Values = {value_name: float(weight) for value_name in agent.Values}
        #do ogarnięcia dalej
        edit_frame = tk.Frame(mainAPV, height=200)
        edit_frame.pack(side=tk.TOP, padx=50, pady=10)
        label = tk.Label(edit_frame,
                         text="Wciśnij wartość w kolumnie\n 'Waga' i edytuj wagę dla ważności wartości\n w propozycjach wśród agentów:\n",
                         font=("Arial", 13))
        label.pack(side=tk.BOTTOM, padx=50, pady=10)
        edit = tk.Entry(edit_frame)
        edit.pack(side=tk.BOTTOM, padx=50, pady=10)
        apply_button = tk.Button(edit_frame, text="Edytuj", command=update_weight, font=("Arial", 13))
        apply_button.pack(side=tk.BOTTOM, padx=50, pady=10)

        submit_button_ = tk.Button(mainAPV, text="Kontynuuj",
                                   command=lambda: self.man_in_PropBaseClean())
        submit_button_.pack(side=tk.BOTTOM, padx=10, pady=10)
        pass

    def man_in_PropBaseClean(self):
        #5 Wyliczenie PropBase Clean (schodzi sie tu i od json i od wlasnego, wiec tutaj takze mozna stworzyć od wprowadzonych danych json
        pass
if __name__ == "__main__":
    root = tk.Tk()
    app = MainWindow(root)
    root.mainloop()
