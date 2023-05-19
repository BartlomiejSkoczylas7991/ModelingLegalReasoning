import sys


class Proposition:
    def __init__(self, statement):
        self.statement = statement

    def getStatement(self):
        return self.statement

    def setStatement(self, statement):
        self.statement = statement

    def addValues(self, value, weight):
        self.values[value] = weight

    def __str__(self):
        return "Proposition{" + \
            "statement='" + self.statement + '\''