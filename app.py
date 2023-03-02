import tkinter as tk
import json
import agent
import proposition
import value


class MainApplication(tk.Frame):
    def __init__(self, master=None):
        super().__init__(master)
        self.master = master
        self.create_widgets()

        self.listAgent = []
        self.listProposition = []
        self.listValues = []
        self.incompPropositions = []

    def add_agent(self, agent):
        self.listAgent.append(agent)

    def add_value(self, name: str, weight: int):
        new_value = value.Value(name, weight)
        self.listValues.append(new_value)

    def add_proposition(self, statement: str, weight: int):
        new_proposition = proposition.Proposition(statement, weight)
        self.listProposition.append(new_proposition)

    def submit_data(self, data_type: str, data: dict):
        proposition = []
        value = []
        if data_type == 'agents':
            for agent_data in data:
                new_agent = agent.Agent(agent_data['name'], agent_data['weight'])
                for val, val_weight in agent_data["values"].items():
                    new_agent.addValues(val, val_weight)
                for prop, prop_data in agent_data["propositions"].items():
                    for val, val_weight in prop_data.items():
                        value = next((v for v in self.listValues if v.getName() == val), None)
                        proposition = next((p for p in self.listProposition if p.getStatement() == prop), None)
                        new_agent.addpropositionsWeight(value, proposition, val_weight)
                self.listAgent.append(new_agent)
        elif data_type == 'propositions':
            for prop_data in data:
                new_proposition = proposition.Proposition(prop_data["statement"], prop_data["weight"])
                self.listProposition.append(new_proposition)
        elif data_type == 'values':
            for value_data in data:
                new_value = value.Value(value_data["statement"], value_data["weight"])
                self.listValues.append(new_value)
        else:
            raise ValueError(f"Invalid data type: {data_type}")

    def create_widgets(self):
        self.input_frame = tk.LabelFrame(self.master, text="Input", padx=5, pady=5)
        self.input_frame.pack(padx=5, pady=5, fill=tk.BOTH, expand=True)

        self.json_frame = tk.LabelFrame(self.input_frame, text="Load from JSON", padx=5, pady=5)
        self.json_frame.pack(padx=5, pady=5, fill=tk.BOTH, expand=True)

        self.load_json_label = tk.Label(self.json_frame, text="Load from JSON?")
        self.load_json_label.pack(side=tk.LEFT)

        self.load_json_var = tk.StringVar(value="no")
        self.load_json_yes = tk.Radiobutton(self.json_frame, text="Yes", variable=self.load_json_var, value="yes")
        self.load_json_yes.pack(side=tk.LEFT)
        self.load_json_no = tk.Radiobutton(self.json_frame, text="No", variable=self.load_json_var, value="no")
        self.load_json_no.pack(side=tk.LEFT)

        self.filename_label = tk.Label(self.json_frame, text="Filename:")
        self.filename_entry = tk.Entry(self.json_frame)
        self.filename_label.pack(side=tk.LEFT)
        self.filename_entry.pack(side=tk.LEFT)

        self.new_data_frame = tk.LabelFrame(self.input_frame, text="New Data", padx=5, pady=5)
        self.new_data_frame.pack(padx=5, pady=5, fill=tk.BOTH, expand=True)

        self.add_agent_button = tk.Button(self.new_data_frame, text="Add Agent", command=self.add_agent)
        self.add_agent_button.pack()

        self.add_proposition_button = tk.Button(self.new_data_frame, text="Add Proposition",
                                                command=self.add_proposition)
        self.add_proposition_button.pack()

        self.add_value_button = tk.Button(self.new_data_frame, text="Add Value", command=self.add_value)
        self.add_value_button.pack()

        self.submit_button = tk.Button(self.input_frame, text="Submit", command=self.submit_data)
        self.submit_button.pack(pady=10)

        self.output_frame = tk.LabelFrame(self.master, text="Output", padx=5)
        self.output_frame = tk.LabelFrame(self.master, text="Output", padx=5, pady=5)
        self.output_frame.pack(padx=5, pady=5, fill=tk.BOTH, expand=True)

        self.agents_frame = tk.LabelFrame(self.output_frame, text="Agents", padx=5, pady=5)
        self.agents_frame.pack(padx=5, pady=5, fill=tk.BOTH, expand=True)

        self.agents_text = tk.Text(self.agents_frame)
        self.agents_text.pack(padx=5, pady=5, fill=tk.BOTH, expand=True)

        self.propositions_frame = tk.LabelFrame(self.output_frame, text="Propositions", padx=5, pady=5)
        self.propositions_frame.pack(padx=5, pady=5, fill=tk.BOTH, expand=True)

        self.propositions_text = tk.Text(self.propositions_frame)
        self.propositions_text.pack(padx=5, pady=5, fill=tk.BOTH, expand=True)

if __name__ == "__main__":
    main = MainApplication()
    main.mainloop()