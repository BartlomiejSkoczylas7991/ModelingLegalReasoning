from typing import Dict, Tuple

from prop import Proposition
from agent import Agent
from value import Value
from Weight import Weight


class AgentValuePropWeight:
    def __init__(self, agent_value_prop_weights: Dict[Tuple[Agent, Value, Proposition], Weight]):
        self.agent_value_prop_weights = agent_value_prop_weights
