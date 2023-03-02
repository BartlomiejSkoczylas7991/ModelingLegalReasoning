import tkinter as tk
from tkinter import ttk
import json


class App:
    def __init__(self, root):
        self.root = root
        self.root.title("PropBaseClean")
        self.root.geometry("600x500")

        # variables for user input
        self.file_path = tk.StringVar()
        self.agent_name = tk.StringVar()
        self.prop_name = tk.StringVar()
        self.prop_value = tk.StringVar()
        self.prop_weight = tk.StringVar()
        self.agent_values = {}
        self.agent_prop_values = {}

        # frame for user input
        input_frame = ttk.Frame(root, padding=(20, 20, 20, 0))
        input_frame.pack(fill=tk.BOTH, expand=True)

        # label and button to select json file
        json_label = ttk.Label(input_frame, text="Select JSON file:")
        json_label.grid(row=0, column=0, sticky=tk.W, pady=10)
        json_entry = ttk.Entry(input_frame, textvariable=self.file_path, state="readonly")
        json_entry.grid(row=0, column=1, sticky=tk.W, pady=10)
        json_button = ttk.Button(input_frame, text="Browse", command=self.browse_json)
        json_button.grid(row=0, column=2, pady=10)

        # frame for adding agents
        add_agent_frame = ttk.Frame(input_frame)
        add_agent_frame.grid(row=1, column=0, columnspan=3, pady=20)
        add_agent_label = ttk.Label(add_agent_frame, text="Add Agent")
        add_agent_label.pack()

        # table for adding agents
        add_agent_table_frame = ttk.Frame(add_agent_frame)
        add_agent_table_frame.pack(pady=10)
        add_agent_table = ttk.Treeview(add_agent_table_frame, columns=("name", "value", "proposition"))
        add_agent_table.heading("#0", text="Agent")
        add_agent_table.heading("#1", text="Value")
        add_agent_table.heading("#2", text="Proposition")
        add_agent_table.pack()

        # buttons to add agent, proposition, and value
        agent_name_entry = ttk.Entry(add_agent_frame, textvariable=self.agent_name)
        agent_name_entry.pack(pady=5)
        add_agent_button = ttk.Button(add_agent_frame, text="Add Agent", command=self.add_agent)
        add_agent_button.pack(pady=5)
        prop_name_entry = ttk.Entry(add_agent_frame, textvariable=self.prop_name)
        prop_name_entry.pack(pady=5)
        prop_value_entry = ttk.Entry(add_agent_frame, textvariable=self.prop_value)
        prop_value_entry.pack(pady=5)
        prop_weight_entry = ttk.Entry(add_agent_frame, textvariable=self.prop_weight)
        prop_weight_entry.pack(pady=5)
        add_prop_button = ttk.Button(add_agent_frame, text="Add Proposition", command=self.add_proposition)
        add_prop_button.pack(pady=5)
        add_value_button = ttk.Button(add_agent_frame, text="Add Value", command=self.add_value)
        add_value_button.pack(pady=5)

        # frame for submitting data
        submit_frame = ttk.Frame(input_frame)
        submit_frame.grid(row=2, column=0, columnspan=3, pady=20)
        submit_button = ttk.Button(submit_frame, text="Submit Data", command=self.submit_data)
        submit_button.pack()

        # frame for displaying output
        output_frame = ttk.Frame(root, padding=(20, 0, 20, 20))
        output_frame.pack(fill=tk.BOTH, expand=True)

        # label for displaying PropBaseClean
        result_label = ttk.Label(output_frame, text="PropBaseClean:")
        result_label.pack(pady=10)

        # table for displaying PropBaseClean
        result_table_frame = ttk.Frame(output_frame)
        result_table_frame.pack(pady=10)
        result_table = ttk.Treeview(result_table_frame, columns=("name", "value", "proposition", "weight"))
        result_table.heading("#0", text="Agent")
        result_table.heading("#1", text="Value")
        result_table.heading("#2", text="Proposition")
        result_table.heading("#3", text="Weight")
        result_table.pack()

    def browse_json(self):
        # function to browse and select json file
        file_path = tk.filedialog.askopenfilename(filetypes=[("JSON Files", "*.json")])
        if file_path:
            self.file_path.set(file_path)
            with open(file_path, "r") as f:
                data = json.load(f)
                self.agent_prop_values = data["agent_prop_values"]
                self.agent_values = data["agent_values"]

    def add_agent(self):
        # function to add agent to table
        agent_name = self.agent_name.get()
        if agent_name:
            self.agent_name.set("")
            self.agent_prop_values[agent_name] = {}
            self.agent_values[agent_name] = {}
            add_agent_table.insert("", "end", text=agent_name)

    def add_proposition(self):
        # function to add proposition to agent
        agent_name = add_agent_table.selection()[0]
        prop_name = self.prop_name.get()
        prop_value = self.prop_value.get()
        if agent_name and prop_name and prop_value:
            self.prop_name.set("")
            self.prop_value.set("")
            self.agent_prop_values[agent_name][prop_name] = float(prop_value)
            add_agent_table.insert(agent_name, "end", text=prop_name, values=(prop_value,))

    def add_value(self):
        # function to add value to agent
        agent_name = add_agent_table.selection()[0]
        prop_name = add_agent_table.selection()[1]
        value_name = self.prop_name.get()
        value_weight = self.prop_weight.get()
        if agent_name and prop_name and value_name and value_weight:
            self.prop_name.set("")
            self.prop_weight.set("")
            self.agent_values[agent_name][prop_name][value_name] = float(value_weight)
            add_agent_table.insert(prop_name, "end", text=value_name, values=(value_weight,))

    def submit_data(self):
        # function to calculate PropBaseClean
        prop_base_clean = {}

        for agent_name, agent_prop_values in self.agent_prop_values.items():
            agent_values = self.agent_values[agent_name]
            agent_prop_weight = {}

            for prop_name, prop_value in agent_prop_values.items():
                prop_weight = {}
                for value_name, value_weight in agent_values[prop_name].items():
                    prop_weight[value_name] = value_weight
                total_weight = sum(prop_weight.values())
                prop_weight = {k: v / total_weight for k, v in prop_weight.items()}
                agent_prop_weight[prop_name] = prop_weight

            prop_base_clean[agent_name] = agent_prop_weight

        result_table.delete(*result_table.get_children())

        for agent_name, agent_prop_weight in prop_base_clean.items():
            for prop_name, prop_weight in agent_prop_weight.items():


root = tk.Tk()
app = PropBaseCleanGUI(root)
root.mainloop()