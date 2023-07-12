package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Court;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Consortium.Consortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Consortium.ListConsortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.PropBaseClean.ListPropBaseClean;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.ReasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.ReasoningChain.ReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule.Rule;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.ListValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

// Propositions
// we need: IncompProp/decisions
// PropBaseClean for each Jugde
// Rules
// Subjective reasoning chains of all judges
// Observations
// The Court's ruling
public class Report implements CourtOpinionObserver, AgentObserver, RuleObserver, ValueObserver, PropositionObserver, PBCObserver, IncompPropObserver {
    private ListAgent listAgent = new ListAgent();
    private ListValue listValue = new ListValue();
    private ListProposition listProposition = new ListProposition();
    private ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
    private ListRules rules = new ListRules();
    private ListReasoningChain listReasoningChains = new ListReasoningChain();
    private ListConsortium observations = new ListConsortium();
    private CourtOpinion courtOpinion = new CourtOpinion();
    private ListIncompProp listIncompProp;

    private StringBuilder strPropositions;

    public Report() {}

    public void setPropositionBase(ListPropBaseClean propBase) {
        this.listPropBaseClean = propBase;
    }

    @Override
    public void updateCourtOpinion(CourtOpinion courtOpinion) {
        this.courtOpinion = courtOpinion;
    }

    public List<ReportSection> generateReport() {
        List<ReportSection> report = new ArrayList<>();
        report.add(new ReportSection("Agents", ))
        report.add(new ReportSection("Propositions", listPropBaseClean.toString()));
        report.add(new ReportSection("Incompatible Propositions", listIncompProp.toString()));
        report.add(new ReportSection("PropBaseClean for Each Agent", generateAgentsPropBaseClean()));
        report.add(new ReportSection("Rules", rules.toString()));
        report.add(new ReportSection("Reasoning Chains of All Agents", listReasoningChains.toString()));
        report.add(new ReportSection("Observations", observations.toString()));
        report.add(new ReportSection("The Court's Ruling", courtOpinion.toString()));

        return report;
    }

    private String generateAgentsPropBaseClean() {
        StringBuilder content = new StringBuilder();
        for (Agent agent : listPropBaseClean.getAgents()) {
            content.append(agent.toString()).append(": ").append(agent.getPropBaseClean().toString()).append("\n");
        }
        return content.toString();
    }

    public String formatForJavaFX(List<ReportSection> report) {
        StringBuilder formattedReport = new StringBuilder();
        for (ReportSection section : report) {
            addTitle(formattedReport, section.getTitle());
            formattedReport.append(section.getContent()).append("\n");
        }
        return formattedReport.toString();
    }

    private void addTitle(StringBuilder report, String title) {
        report.append("\n").append(title).append("\n");
        report.append("=".repeat(title.length())).append("\n");
    }

    @Override
    public void updateAgent(Agent agent) {
        this.listAgent.addAgent(agent);
    }

    @Override
    public void updatePBC(ListPropBaseClean propBaseClean) {
        this.listPropBaseClean = propBaseClean;
    }

    @Override
    public void updateProposition(Proposition proposition) {
        this.listProposition.addProposition(proposition);
    }

    @Override
    public void updateRule(ListRules listRules) {

    }

    @Override
    public void updateValue(Value value) {

    }

    @Override
    public void updateIncomp(ListIncompProp listIncompProp) {
        this.listIncompProp = listIncompProp;
        this.listProposition.updateIncomp(this.listIncompProp);
    }

    public class ReportSection {
        private String title;
        private String content;

        public ReportSection(String title, String content) {
            this.title = title;
            this.content = content;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }
    }
}
