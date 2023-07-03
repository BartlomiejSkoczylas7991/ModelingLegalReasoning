package com.bskoczylas.modelinglegalreasoning.models;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.AV.AgentValueToWeight;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.AVP.AgentValuePropWeight;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Consortium.Consortium;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Court.Court;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Decision.Decision;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.IncompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.KnowledgeBase.ListKnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.PropBaseClean.ListPropBaseClean;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.ReasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Scale_Weight.Scale;

public class Project {
    private static int nextId = 1;
    private final int id;
    private String name;
    private ListAgent listAgent; //lista agentów
    private ListProposition listProposition; // lista propozycji
    private ListValue listValue; // lista wartości
    private ListIncompProp listIncompProp;
    private AgentValueToWeight agentValueToWeight; // lista wag AV
    private AgentValuePropWeight agentValuePropWeight; // lista wag AVP
    private Scale scale; // skala wag
    private ListRules listRules;
    private ListPropBaseClean listPropBaseClean;
    private ListKnowledgeBase listKnowledgeBase;
    private ListReasoningChain listReasoningChain;
    private Court court;
    private Decision decision;
    private Consortium consortium;

    public Project(String name){
        if (name == null || name.trim().isEmpty()) {
            name = "Project" + nextId;
        }
        this.name = name;
        this.id = nextId;  // Ustawiamy id na wartość nextId
        nextId++;  // Zwiększamy nextId o 1, więc następny Project będzie miał większe id

        this.listAgent = new ListAgent();
        this.listProposition = new ListProposition();
        this.listValue = new ListValue();
        this.listRules = new ListRules();
        this.agentValuePropWeight = new AgentValuePropWeight();
        this.agentValueToWeight = new AgentValueToWeight();
        this.listIncompProp = new ListIncompProp();
        this.listKnowledgeBase = new ListKnowledgeBase();
        this.listPropBaseClean = new ListPropBaseClean();
        this.scale = new Scale();
        this.listReasoningChain = new ListReasoningChain();

        this.listAgent.addAgentObserver(this.agentValuePropWeight);
        this.listAgent.addAgentObserver(this.agentValueToWeight);
        this.listValue.addObserver(this.agentValuePropWeight);
        this.listValue.addObserver(this.agentValueToWeight);
        this.listProposition.addObserver(this.agentValuePropWeight);
        this.listProposition.addObserver(this.listIncompProp);
        this.scale.addObserver(this.agentValuePropWeight);
        this.scale.addObserver(this.agentValueToWeight);
        this.agentValuePropWeight.addObserver(this.listPropBaseClean);
        this.agentValueToWeight.addAVObserver(this.listPropBaseClean);
        this.listPropBaseClean.addObserver(this.listKnowledgeBase);
        this.listIncompProp.addObserver(this.listReasoningChain);
        this.listRules.addObserver(this.listKnowledgeBase);
        this.listKnowledgeBase.addObserver(this.listReasoningChain);
        this.listIncompProp.addObserver(this.listRules);
        this.listIncompProp.addObserver(this.listReasoningChain);

    }

    public int getId() {
        return id;
    }

}
