from typing import Tuple, List, Dict

import prop
import value
from Weight import Weight
from agent import Agent


def extract_agent_weights(agents: List[Agent]) -> Tuple[
    Dict[Tuple[Agent, value.Value], Weight], Dict[Tuple[Agent, value.Value, prop.Proposition], Weight]]:
    agent_value_to_weight = {}
    agent_value_prop_weight = {}

    for agent in agents:
        agent_value_to_weight.update(agent.AgentValueToWeight)
        agent_value_prop_weight.update(agent.agentPropValueToWeight)

    return agent_value_to_weight, agent_value_prop_weight


def get_agent_by_name(agent_list, agent_name):
    for agent in agent_list:
        if agent.getName() == agent_name:
            return agent
    return None
