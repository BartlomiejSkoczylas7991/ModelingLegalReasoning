import sys


class Proposition:
    def __init__(self, statement, weight):
        self.statement = statement
        self.weight = weight

    def getStatement(self):
        return self.statement

    def setStatement(self, statement):
        self.statement = statement

    def getWeight(self):
        return self.weight

    def setWeight(self, weight):
        self.weight = weight

    def addValues(self, value, weight):
        self.values[value] = weight

    def __str__(self):
        return "Proposition{" + \
            "statement='" + self.statement + '\'' + \
            ", weight=" + str(self.weight) + \
            '}'