from typing import List, Dict, Tuple
import value, Weight


class Agent:
    def __init__(self, name):
        self.name = name
        self.values = {}
        self.propBaseClean = {}
        self.agentPropValueToWeight = {}
        self.AgentValueToWeight = Dict[Tuple[Agent, value.Value], Weight]

    def getName(self):
        return self.name

    def setName(self, name):
        self.name = name

    def getValues(self):
        return self.values

    def addValues(self, value, weight):
        self.values[value] = weight

    def addAgentPropValueToWeight(self, value_name, prop_name, weight):
        self.agentPropValueToWeight[(self, value_name, prop_name)] = weight

    def addPropBaseClean(self, proposition):
        self.propBaseClean.append(proposition)

    def getAgentPropValuetoWeight(self):
        return self.getAgentPropValuetoWeight()

    def getPropBaseClean(self):
        return self.propBaseClean

    def getValuesDict(self):
        return self.values.values()

    def getWeightFromValue(self, value, weight):
        self.values[value] = weight

    def addagentPropValuetoWeight(self, proposition, value, weight):
        propDict = {}
        propDict[(proposition, value)] = weight
        self.agentPropValuetoWeight.update(propDict)

    def __str__(self):
        return self.name

    def addPropBaseClean(self, propositions):
        pass
