from typing import List, Dict, Tuple
import value, weight

class Agent:
    def __init__(self, name, weight):
        self.name = name
        self.weight = weight
        self.values = {}
        self.propBaseClean = []
        self.agentPropValuetoWeight = []
        #self.AgentValueToWeight = Dict[Tuple[Agent, Value], Weight]

    def getName(self):
        return self.name

    def setName(self, name):
        self.name = name

    def addValues(self, value, weight):
        self.values[value] = weight

    def addAgentPropValuetoWeight(self, value, prop, weight):
        self.agentPropValuetoWeight.append([self.name, value, prop, weight])

    def addPropBaseClean(self, proposition):
        self.propBaseClean.append(proposition)

    def getAgentPropValuetoWeight(self):
        return self.getAgentPropValuetoWeight()

    def getPropBaseClean(self):
        return self.propBaseClean

    def getValue(self, value):
        return self.values[value]

    def getWeight(self):
        return self.weight

    def __str__(self):
        return self.name

    def addPropBaseClean(self, propositions):
        pass




