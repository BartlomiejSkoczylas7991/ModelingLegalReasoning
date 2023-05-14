from typing import List, Set, Dict

import prop
from agent import Agent
from agentValuePropWeight import AgentValuePropWeight
from agentValueToWeight import AgentValueToWeight
from value import Value


class PropBaseClean:
    def __init__(self, agents: List[Agent], values: List[Value], props: List[Prop],
                 agent_value_to_weight: AgentValueToWeight, agent_value_prop_weight: AgentValuePropWeight):
        self.prop_base_clean = self.calculate_prop_base_clean(agents, values, props,
                                                               agent_value_to_weight, agent_value_prop_weight)

    def calculate_prop_base_clean(self, agents: List[Agent], values: List[Value], props: List[Prop],
                                  agent_value_to_weight: AgentValueToWeight,
                                  agent_value_prop_weight: AgentValuePropWeight) -> Dict[Agent, Set[Prop]]:
        prop_base_clean = {}
        for agent in agents:
            prop_base_clean[agent] = set()
            for prop in props:
                pass_filter = True
                for value in values:
                    if (agent_value_prop_weight.agent_value_prop_weights[(agent, value, prop)].value <
                            agent_value_to_weight.agent_value_weights[(agent, value)].value):
                        pass_filter = False
                        break
                if pass_filter:
                    prop_base_clean[agent].add(prop)
        return prop_base_clean