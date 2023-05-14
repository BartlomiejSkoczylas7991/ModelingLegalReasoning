from typing import Dict, Tuple

from agent import Agent
from value import Value
from Weight import Weight


class AgentValueToWeight:
    def __init__(self, agent_value_weights: Dict[Tuple[Agent, Value], Weight]):
        self.agent_value_weights = agent_value_weights