#!/usr/bin/python3

import tkinter as tk
from tkinter import filedialog, ttk, simpledialog
import json
import agent as ag
import prop
import value
import functions


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
        self.doesJSON = False
        self.agents_tableAVW = ttk.Treeview(self.master, columns=('Name', 'Value', 'Weight'), height=20)
        self.agents_tableAPVW = ttk.Treeview(self.master, columns=('Name', 'Value', 'Weight'), height=20)
        self.load_json_label = tk.Label(left_frame, text="Dodaj plik, który został wcześniej \nwygenerowany\n\n",
                                        font=("Arial", 13))
        self.load_json_label.pack()
        self.load_json_button = tk.Button(left_frame, text="json", command=self.load_data_from_json)
        self.load_json_button.pack()
        self.add_values_label = tk.Label(right_frame, text="Dodaj wartości dla sprawy sądowej\n\n", font=("Arial", 13))
        self.add_values_label.pack()
        self.add_values_button = tk.Button(right_frame, text="Dodaj wartości", command=self.manual_input)
        self.add_values_button.pack()

        self.master.title("Modelowanie wnioskowania prawniczego")

    def reset_window(self, width: int, height: int):
        for widget in self.master.winfo_children():
            widget.destroy()
        self.master.geometry(f"{width}x{height}")

    def save_data_to_json(self):
        data = {
            "agents": [],
            "propositions": [],
            "values": [],
            "agent_values_weights": [],
            "agent_proposition_values_weights": []
        }

        for agent in self.listAgent:
            data["agents"].append(agent.getName())

        for prop in self.listProposition:
            data["propositions"].append(prop.getStatement())

        for value in self.listValues:
            data["values"].append(value.getName())

        for row in self.agents_tableAVW.get_children():
            row_data = self.agents_tableAVW.item(row)["values"]
            data["agent_values_weights"].append({
                "agent": row_data[0],
                "value": row_data[1],
                "weight": row_data[2]
            })

        for row in self.agents_tableAPVW.get_children():
            row_data = self.agents_tableAPVW.item(row)["values"]
            data["agent_proposition_values_weights"].append({
                "agent": row_data[0],
                "value": row_data[1],
                "proposition": row_data[2],
                "weight": row_data[3]
            })

        with open("output.json", "w") as outfile:
            json.dump(data, outfile, indent=4)

    def load_data_from_json(self):
        filename = tk.filedialog.askopenfilename(title="Wybierz plik JSON")
        if filename:
            with open(filename, "r") as infile:
                data = json.load(infile)

            self.listAgent = [ag.Agent(agent_name) for agent_name in data["agents"]]
            self.listProposition = [prop.Proposition(prop_name) for prop_name in data["propositions"]]
            self.listValues = [value.Value(value_name) for value_name in data["values"]]

            self.agents_tableAVW.delete(*self.agents_tableAVW.get_children())
            for agent_value_weight in data["agent_values_weights"]:
                agent = functions.get_agent_by_name(self.listAgent, agent_value_weight["agent"])
                if agent:
                    agent.addValues(agent_value_weight["value"], agent_value_weight["weight"])
                    self.agents_tableAVW.insert('', tk.END, values=(agent_value_weight["agent"],
                                                                    agent_value_weight["value"],
                                                                    agent_value_weight["weight"]))

            self.agents_tableAPVW.delete(*self.agents_tableAPVW.get_children())
            for agent_prop_val_weight in data["agent_proposition_values_weights"]:
                agent = self.get_agent_by_name(self.listAgent, agent_prop_val_weight["agent"])
                if agent:
                    agent.addagentPropValueToWeight(agent_prop_val_weight["proposition"],
                                                    agent_prop_val_weight["value"],agent_prop_val_weight["weight"])
            self.agents_tableAPVW.insert('', tk.END, values=(agent_prop_val_weight["agent"],
                                                          agent_prop_val_weight["value"],
                                                          agent_prop_val_weight["proposition"],
                                                          agent_prop_val_weight["weight"]))


    def create_input_frame(self, title, add_callback):
        input_frame = tk.Frame(self.master)
        input_frame.pack(side=tk.LEFT, padx=20)

        input_table = ttk.Treeview(input_frame, columns='Name')
        input_table.heading('#0', text='ID')
        input_table.heading('Name', text='Nazwa')
        input_table.column('#0', width=20)
        input_table.column('Name', width=100)
        input_table.pack(side=tk.LEFT, padx=10)

        input_label = tk.Label(input_frame, text=title)
        input_label.pack()

        input_name = tk.Text(input_frame, width=12, height=1)
        input_name.pack()

        add_input_button = tk.Button(input_frame, text=f"Dodaj {title.split()[-1]}",
                                     command=lambda: add_callback(input_name, input_table))
        add_input_button.pack()

        return input_frame

    def manual_input(self):
        self.reset_window(1150, 400)

        main_frame = tk.Frame(self.master, height=200, width=1150)
        main_frame.pack(side=tk.TOP, padx=50)
        main_label = tk.Label(main_frame, text="Dodaj agentów, wartości i propozycje. ", font=("Arial", 22),
                              justify='center')
        main_label.pack(pady=(50, 20))

        agent_frame = self.create_input_frame("Dodaj nazwę agenta", self.add_agent)
        value_frame = self.create_input_frame("Dodaj nazwę Wartości", self.add_value)
        proposition_frame = self.create_input_frame("Dodaj nazwę Propozycji", self.add_proposition)

        # Dodanie przycisku zatwierdzającego wprowadzone dane
        button_frame = tk.Frame(self.master, height=300)
        button_frame.pack(side=tk.BOTTOM, pady=10, fill=tk.X)

        confirm_button = tk.Button(button_frame, text="Zatwierdź", command=lambda: self.confirm_input())
        confirm_button.pack(side=tk.LEFT, padx=60, pady=10)

    def confirm_input(self):
        self.reset_window(1150, 600)

        if not self.listAgent or not self.listProposition or not self.listValues:
            agent_label = tk.Label(self.master,
                                   text="\n\n\nNie możesz przejść dalej. Twoja tablica jest pusta. Uzupełnij tablice.",
                                   font=("Arial", 13))
            agent_label.pack()

            button_frame = tk.Frame(self.master, height=300)
            button_frame.pack(side=tk.BOTTOM, pady=10, fill=tk.X)

            confirm_button = tk.Button(button_frame, text="Zatwierdź", command=lambda: self.manual_input())
            confirm_button.pack(side=tk.RIGHT, padx=10)
        else:
            agent_label = tk.Label(self.master,
                                   text="\n\n\nUzupełniłeś wartości. Przechodzimy do uzupełniania wag dla wartosci agentów, a "
                                        "następnie do uzupełniania wag dla propozycji dla wartosci agentów. \n\n\n Czy są jakieś sprzeczne propozycje?",
                                   font=("Arial", 13))
            agent_label.pack()

            button_frame = tk.Frame(self.master, height=300)
            button_frame.pack(side=tk.BOTTOM, pady=10, fill=tk.X)

            confirm_button = tk.Button(button_frame, text="Tak", command=lambda: self.incompPropositions())
            confirm_button.pack(side=tk.RIGHT, padx=10)

            confirm_button = tk.Button(button_frame, text="Nie", command=lambda: self.man_in_AgentValueToWeight())
            confirm_button.pack(side=tk.RIGHT, padx=10)

    def add_agent(self, agent_name, agents_table):
        name = agent_name.get("1.0", tk.END).strip()
        if name and not any(i.getName() == name for i in self.listAgent):
            new_agent = ag.Agent(name)
            self.listAgent.append(new_agent)
            agents_table.insert('', tk.END, text=str(len(self.listAgent)), values=(name,))
        agent_name.delete("1.0", tk.END)

    def add_value(self, value_name, value_table):
        name = value_name.get("1.0", tk.END).strip()
        if name and not any(i.getName() == name for i in self.listValues):
            new_value = value.Value(name)
            self.listValues.append(new_value)
            value_table.insert('', tk.END, text=str(len(self.listValues)), values=(name,))
        value_name.delete("1.0", tk.END)

    def add_proposition(self, proposition_name, proposition_table):
        name = proposition_name.get("1.0", tk.END).strip()
        if name and not any(i.getStatement() == name for i in self.listProposition):
            new_proposition = prop.Proposition(name)
            self.listProposition.append(new_proposition)
            proposition_table.insert('', tk.END, text=str(len(self.listProposition)), values=(name,))
        proposition_name.delete("1.0", tk.END)

    def incompPropositions(self):
        self.reset_window(400, 300)
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
        submit_button_incomp_compl = tk.Button(self.master, text="Kontynuuj",
                                               command=lambda: self.man_in_AgentValueToWeight())
        submit_button_incomp_compl.pack(side=tk.BOTTOM, padx=10)

    def submitIncomp(self, prop1, prop2):
        if prop1 != prop2:
            prop1 = next((prop for prop in self.listProposition if prop.getStatement() == prop1), None)
            prop2 = next((prop for prop in self.listProposition if prop.getStatement() == prop2), None)
        else:
            tk.messagebox.showerror("Błąd", "Wybrane propozycje są takie same!")
        # Dodaj do listy nieadekwatnych propozycji
        incomp_props = self.getIncompPropositions()
        if any(([prop1, prop2] == pair or [prop2, prop1] == pair) for pair in incomp_props):
            tk.messagebox.showerror("Błąd", "Te propozycje są już oznaczone jako nieadekwatne!")
            # self.incompPropositions();
        else:
            self.incompProp.append([prop1, prop2])
            # self.incompPropositions();

    def getIncompPropositions(self):
        return self.incompProp

    def getAgentByName(self, agentName):
        for i in self.listAgent:
            if str(i.getName()) == str(agentName):
                return i

    def getValueByName(self, value_name):
        for value in self.listValues:
            if value.getName() == value_name:
                return value
        return None

    def getPropositionByName(self, prop_name):
        for prop in self.listProposition:
            if prop.getStatement() == prop_name:
                return prop
        return None

    def man_in_AgentValueToWeight(self):
        self.reset_window(950, 750)

        left_frame = tk.Frame(self.master)
        left_frame.pack(side=tk.LEFT, padx=10, pady=10)

        right_frame = tk.Frame(self.master)
        right_frame.pack(side=tk.RIGHT, padx=10, pady=10)

        main_label = tk.Label(left_frame,
                              text="Dodaj wagi dla wartości agentów. \nJest to waga do jakiej dany agent przywiązuje się\n z daną wartością. ",
                              font=("Arial", 18),
                              justify='center')
        main_label.pack(side=tk.TOP, padx=50, pady=20)
        agents_table_label = tk.Frame(left_frame)
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

        current_id = 0
        for agent in self.listAgent:
            for value in self.listValues:
                agent.addValues(value.getName(), "?")
                self.agents_tableAVW.insert('', tk.END, text=str(current_id),
                                            values=(agent.getName(), value.getName(), "?"))
                current_id += 1

        def edit_cell(event):
            def save_weight(event):
                new_weight = entry.get()
                self.agents_tableAVW.set(row_id, 'Weight', new_weight)
                agent_name = self.agents_tableAVW.item(row_id, 'values')[0]
                value_name = self.agents_tableAVW.item(row_id, 'values')[1]
                agent = self.getAgentByName(agent_name)
                if agent:
                    agent.addValues(value_name, new_weight)
                entry.destroy()

            row_id = self.agents_tableAVW.focus()
            column = self.agents_tableAVW.identify_column(event.x)
            if column == '#3':  # jeśli to kolumna "Waga"
                # Pobierz współrzędne komórki
                x, y, _, _ = self.agents_tableAVW.bbox(row_id, column)

                # Utwórz pole wprowadzania (Entry) i umieść je na komórce
                entry = tk.Entry(self.agents_tableAVW)
                entry.place(x=x, y=y)

                # Wprowadź bieżącą wagę do pola wprowadzania
                current_weight = self.agents_tableAVW.item(row_id, 'values')[2]
                entry.insert(0, current_weight)

                # Związanie zdarzenia zapisywania nowej wagi po naciśnięciu klawisza Enter
                entry.bind('<Return>', save_weight)

        self.agents_tableAVW.bind("<Double-1>", edit_cell)

        submit_button_frame = tk.Frame(right_frame)
        submit_button_frame.pack(side=tk.BOTTOM, padx=50, pady=10)
        submit_button_ = tk.Button(submit_button_frame, text="Kontynuuj",
                                   command=lambda: self.man_in_AgentPropValueToWeight())
        submit_button_.pack(side=tk.BOTTOM, padx=50, pady=10)


    def man_in_AgentPropValueToWeight(self):
        for widget in self.master.winfo_children():
            widget.destroy()
        self.master.geometry("850x500")  # ustawienie rozmiaru okna
        for agent in self.listAgent:
            print(agent.getValues())

        mainAPV = tk.Frame(self.master, height=450, width=450)
        mainAPV.pack(side=tk.BOTTOM, padx=50, pady=10)
        main_label = tk.Label(mainAPV,
                              text="Dodaj wagi dla relacji propozycji i wartości agentów. \nJest to waga do tego jak odnosi się dana propozycja \ndo danej wartości agenta.",
                              font=("Arial", 19),
                              justify='center')
        main_label.pack(side=tk.TOP, padx=50, pady=20)
        agents_table_label = tk.Frame(mainAPV)
        agents_table_label.pack(side=tk.TOP)
        self.agents_tableAPVW = ttk.Treeview(agents_table_label, columns=('Name', 'Value', 'Proposition', 'Weight'),
                                             height=20)
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
        table_scroll = ttk.Scrollbar(agents_table_label, orient="vertical", command=self.agents_tableAPVW.yview)
        table_scroll.grid(row=0, column=1, sticky='ns')
        self.agents_tableAPVW.configure(yscrollcommand=table_scroll.set)
        iter = 1
        for agent in self.listAgent:
            for prop in self.listProposition:
                waga_prop = 0
                warun = False
                for value in self.listValues:
                    agent.addAgentPropValueToWeight(value.getName(), prop.getStatement(), "?")
                    self.agents_tableAPVW.insert('', tk.END, text=str(len(self.listAgent)),
                                                 values=(agent.getName(), value.getName(), prop.getStatement(), "?"))

        def edit_cell(event):
            def save_weight(event):
                new_weight = entry.get()
                self.agents_tableAPVW.set(row_id, 'Weight', new_weight)
                agent_name = self.agents_tableAPVW.item(row_id, 'values')[0]
                value_name = self.agents_tableAPVW.item(row_id, 'values')[1]
                prop_name = self.agents_tableAPVW.item(row_id, 'values')[2]
                agent = self.getAgentByName(agent_name)
                value_obj = self.getValueByName(value_name)
                prop_obj = self.getPropositionByName(prop_name)
                if agent and value_obj and prop_obj:
                    agent.addAgentPropValueToWeight(value_obj, prop_obj, new_weight)
                entry.destroy()

            row_id = self.agents_tableAPVW.focus()
            column = self.agents_tableAPVW.identify_column(event.x)
            if column == '#4':  # jeśli to kolumna "Waga"
                # Pobierz współrzędne komórki
                x, y, _, _ = self.agents_tableAPVW.bbox(row_id, column)

                # Utwórz pole wprowadzania (Entry) i umieść je na komórce
                entry = tk.Entry(self.agents_tableAPVW)
                entry.place(x=x, y=y)

                # Wprowadź bieżącą wagę do pola wprowadzania
                current_weight = self.agents_tableAPVW.item(row_id, 'values')[3]
                entry.insert(0, current_weight)

                # Związanie zdarzenia zapisywania nowej wagi po naciśnięciu klawisza Enter
                entry.bind('<Return>', save_weight)

        self.agents_tableAPVW.bind("<Double-1>", edit_cell)

        # do ogarnięcia dalej

        save_button = tk.Button(mainAPV, text="Zapisz dane", command=self.save_data_to_json)
        save_button.pack(side=tk.BOTTOM, padx=60, pady=10)

        submit_button_ = tk.Button(mainAPV, text="Kontynuuj",
                                   command=lambda: self.man_in_PropBaseClean())

        submit_button_.pack(side=tk.BOTTOM, padx=60, pady=10)
        pass

    def man_in_PropBaseClean(self):
        if not self.doesJSON:
            pass
        else:
            for widget in self.master.winfo_children():
                widget.destroy()
            self.master.geometry("850x900")

            # Dodaj tutaj kod, który tworzy listy agents, values, props,
            # agent_value_to_weight, agent_value_prop_weight.

            prop_base_clean_obj = propBaseClean.PropBaseClean(self.listAgent, self.listValues, self.listProposition, agent_value_to_weight, agent_value_prop_weight)
            prop_base_clean_result = prop_base_clean_obj.prop_base_clean

            for agent in self.listAgent:
                agent.addPropBaseClean(prop_base_clean_result[agent])


if __name__ == "__main__":
    root = tk.Tk()
    app = MainWindow(root)
    root.mainloop()
