

class Value:
    def __init__(self, name):
        self.name = name
        self.propositionsWeight = {}

    def getName(self):
        return self.name

    def setName(self, name):
        self.name = name

    def setWeight(self, weight):
        self.weight = weight

    def getpropositionsWeight(self, proposition):
        return self.propositionsWeight[proposition]

    def addpropositionsWeight(self, proposition, weight):
        self.propositionsWeight[proposition] = weight


    def __str__(self):
        return "Value{" + \
            "name='" + self.name + '\''