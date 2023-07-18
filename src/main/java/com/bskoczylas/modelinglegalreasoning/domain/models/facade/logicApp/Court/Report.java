package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Court;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Consortium.Consortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Consortium.ConsortiumType;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Consortium.ListConsortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.PropBaseClean.ListPropBaseClean;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.ReasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.ReasoningChain.ReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.ListValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.Value;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

//Generowanie ostatecznego raportu, w którym wyświetlamy dane.
// To tutaj widzimy wynik wnioskowania i tu kończy się logika biznesowa.
//Generating the final report in which we display the data.
// //This is where we see the inference result and where the business logic ends.
// Propositions
// we need: IncompProp/decisions
// PropBaseClean for each Jugde
// Rules
// Subjective reasoning chains of all judges
// Observations
// The Court's ruling
public class Report implements CourtOpinionObserver, AgentObserver, RuleObserver, ValueObserver, PBCObserver, IncompPropObserver {
    private ListAgent listAgent = new ListAgent();
    private ListValue listValue = new ListValue();
    private ListProposition listProposition = new ListProposition();
    private ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
    private ListRules listRules = new ListRules();
    private ListReasoningChain listReasoningChains = new ListReasoningChain();
    private ListConsortium listConsortium = new ListConsortium();
    private CourtOpinion courtOpinion = new CourtOpinion();
    private ListIncompProp listIncompProp;

    public Report() {}

    public void setPropositionBase(ListPropBaseClean propBase) {
        this.listPropBaseClean = propBase;
    }

    public List<ReportSection> generateReport() {
        List<ReportSection> report = new ArrayList<>();
        report.add(new ReportSection("Agents", listAgent.toString()));
        report.add(new ReportSection("Propositions", listProposition.toString()));
        report.add(new ReportSection("Incompatible Propositions", listIncompProp.toString()));
        report.add(new ReportSection("PropBaseClean for Each Agent", listPropBaseClean.toString()));
        report.add(new ReportSection("Rules", listRules.toString()));
        report.add(new ReportSection("Reasoning Chains of All Agents", listReasoningChains.toString()));
        report.add(new ReportSection("Observations", generateObservations()));
        report.add(new ReportSection("The Court's Ruling", generateCourtRuling()));

        return report;
    }

    public String formatForJavaFX(List<ReportSection> report) {
        StringBuilder formattedReport = new StringBuilder();
        for (ReportSection section : report) {
            addTitle(formattedReport, section.getTitle());
            formattedReport.append(section.getContent()).append("\n");
        }
        return formattedReport.toString();
    }

    private String generateObservations() {
        StringBuilder observations = new StringBuilder();
        int consortiumCount = 1; // used for syndication naming

        for (Map.Entry<Consortium, ConsortiumType> entry : listConsortium.getConsortiumMap().entrySet()) {
            Consortium consortium = entry.getKey();
            ReasoningChain rc = consortium.getReasoningChain();
            Set<Agent> agents = consortium.getAgents();

            // Generate consortium name from counter
            String consortiumName = "Consortium" + consortiumCount;

            // Add consortium information
            observations.append("Chains of ");
            observations.append(agents.stream().map(Agent::getName).collect(Collectors.joining(", ")));
            observations.append(" are the same and they constitute ");
            observations.append(consortiumName);
            observations.append(", ");
            observations.append(rc.getDecision().toString());
            observations.append(" , where ");
            observations.append(consortiumName);
            observations.append(" =< ");
            observations.append(rc.getKnowledgeBase().getPi().stream().map(Proposition::toString).collect(Collectors.joining(", ")));
            observations.append(">, ");
            for (Proposition p : rc.getKnowledgeBase().getPi()) {
                observations.append(p.toString());
                observations.append(" → ");
                observations.append(rc.getDecision().toString());
                observations.append(", ");
            }
            observations.append(".\n");

            consortiumCount++; // increment the counter for the next consortium
        }

        return observations.toString();
    }

    private String generateJudgesInformation(String judgeType, Map<ReasoningChain, Set<Agent>> opinions) {
        StringBuilder judgesInformation = new StringBuilder();
        for (Map.Entry<ReasoningChain, Set<Agent>> entry : opinions.entrySet()) {
            judgesInformation.append(judgeType);
            judgesInformation.append("<");
            judgesInformation.append(entry.getKey().toString());
            judgesInformation.append(",");
            judgesInformation.append(entry.getKey().getDecision().toString());
            judgesInformation.append("> = ");
            judgesInformation.append("{");
            judgesInformation.append(entry.getValue().stream().map(Agent::getName).collect(Collectors.joining(", ")));
            judgesInformation.append("}\n");
        }
        return judgesInformation.toString();
    }

    private void addTitle(StringBuilder report, String title) {
        report.append("\n").append(title).append("\n");
        report.append("=".repeat(title.length())).append("\n");
    }

    private String generateCourtRuling() {
        StringBuilder courtRuling = new StringBuilder();
        courtRuling.append("The Court's ruling:\n");
        courtRuling.append("Decision = ");
        courtRuling.append(courtOpinion.getDecision().toString());
        courtRuling.append("\n");

        courtRuling.append(generateJudgesInformation("MajorityJudges", courtOpinion.getMajorityOpinions()));
        courtRuling.append(generateJudgesInformation("DissentingJudges", courtOpinion.getDissentingOpinions()));

        if (courtOpinion.getPluralityOpinions().isEmpty() && courtOpinion.getConcurringOpinions().isEmpty()) {
            courtRuling.append("There are neither plurality nor concurring judges.\n");
        } else {
            courtRuling.append(generateJudgesInformation("PluralityJudges", courtOpinion.getPluralityOpinions()));
            courtRuling.append(generateJudgesInformation("ConcurringJudges", courtOpinion.getConcurringOpinions()));
        }

        return courtRuling.toString();
    }

    public ListAgent getListAgent() {
        return listAgent;
    }

    public void setListAgent(ListAgent listAgent) {
        this.listAgent = listAgent;
    }

    public ListValue getListValue() {
        return listValue;
    }

    public void setListValue(ListValue listValue) {
        this.listValue = listValue;
    }

    public ListProposition getListProposition() {
        return listProposition;
    }

    public void setListProposition(ListProposition listProposition) {
        this.listProposition = listProposition;
    }

    public ListPropBaseClean getListPropBaseClean() {
        return listPropBaseClean;
    }

    public void setListPropBaseClean(ListPropBaseClean listPropBaseClean) {
        this.listPropBaseClean = listPropBaseClean;
    }

    public ListRules getListRules() {
        return listRules;
    }

    public void setListRules(ListRules listRules) {
        this.listRules = listRules;
    }

    public ListReasoningChain getListReasoningChains() {
        return listReasoningChains;
    }

    public void setListReasoningChains(ListReasoningChain listReasoningChains) {
        this.listReasoningChains = listReasoningChains;
    }

    public ListConsortium getListConsortium() {
        return listConsortium;
    }

    public void setListConsortium(ListConsortium listConsortium) {
        this.listConsortium = listConsortium;
    }

    public CourtOpinion getCourtOpinion() {
        return courtOpinion;
    }

    public void setCourtOpinion(CourtOpinion courtOpinion) {
        this.courtOpinion = courtOpinion;
    }

    public ListIncompProp getListIncompProp() {
        return listIncompProp;
    }

    public void setListIncompProp(ListIncompProp listIncompProp) {
        this.listIncompProp = listIncompProp;
    }

    @Override
    public void updateAgent(ListAgent listAgent) {
        this.listAgent = listAgent;
    }

    @Override
    public void updatePBC(ListPropBaseClean propBaseClean) {
        this.listPropBaseClean = propBaseClean;
    }

    @Override
    public void updateRule(ListRules listRules) {
        this.listRules = listRules;
    }

    @Override
    public void updateCourtOpinion(CourtOpinion courtOpinion) {
        this.courtOpinion = courtOpinion;
        this.listConsortium = courtOpinion.getListConsortium();
        this.listReasoningChains = courtOpinion.getListConsortium().getListReasoningChain();
    }

    @Override
    public void updateValue(ListValue listValue) {
        this.listValue = listValue;
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
