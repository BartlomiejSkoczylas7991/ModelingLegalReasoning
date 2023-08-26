package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.court;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.Agent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent.ListAgent;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.consortium.Consortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.consortium.ConsortiumType;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.consortium.ListConsortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.propBaseClean.ListPropBaseClean;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.ListProposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition.Proposition;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain.ReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.ListValue;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Report implements CourtOpinionObserver, AgentObserver, RuleObserver, ValueObserver, PBCObserver, IncompPropObserver {
    private ListAgent listAgent = new ListAgent();
    private ListValue listValue = new ListValue();
    private ListProposition listProposition = new ListProposition();
    private ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
    private ListRules listRules = new ListRules();
    private ListReasoningChain listReasoningChains = new ListReasoningChain();
    private ListConsortium listConsortium = new ListConsortium();
    private CourtOpinion courtOpinion = new CourtOpinion();
    private ListIncompProp listIncompProp = new ListIncompProp();

    public Report() {}

    public void setPropositionBase(ListPropBaseClean propBase) {
        this.listPropBaseClean = propBase;
    }

    public List<ReportSection> generateReport() {
        List<ReportSection> report = new ArrayList<>();
        report.add(new ReportSection("Agents: ", listAgent.toString()));
        report.add(new ReportSection("Propositions: ", listProposition.toString()));
        report.add(new ReportSection("Incompatible Propositions: ", listIncompProp.toString()));
        report.add(new ReportSection("PropBaseClean for Each Agent: ", listPropBaseClean.toString()));
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
        int consortiumCount = 1;

        for (Map.Entry<Consortium, ConsortiumType> entry : this.listConsortium.getConsortiumMap().entrySet()) {
            Consortium consortium = entry.getKey();
            ReasoningChain rc = consortium.getReasoningChain();
            Set<Agent> agents = consortium.getAgents();

            String consortiumName = "Consortium" + consortiumCount;

            if (agents.size() > 1) {
                observations.append("Chains of ");
                observations.append(agents.stream().map(Agent::getName).collect(Collectors.joining(", ")));
                observations.append(" are the same and they constitute ");
            } else {
                observations.append("Only ");
                observations.append(agents.stream().findFirst().get().getName());
                observations.append(" has chain constituting ");
            }

            observations.append(consortiumName);
            observations.append(", ");
            Proposition decision = rc.getDecision();
            observations.append(decision == null ? "not decided" : decision.toString());
            observations.append(" , where ");
            observations.append(consortiumName);
            observations.append(" =< {");
            observations.append(rc.getKnowledgeBase().getPi().stream().map(Proposition::toString).collect(Collectors.joining(", ")));
            observations.append(decision == null ? "not decided" : decision.toString());
            observations.append("}, ");
            for (Proposition p : rc.getKnowledgeBase().getPi()) {
                observations.append(p.toString());
                observations.append(" → ");
                observations.append(rc.getDecision().toString());
                observations.append(", ");
            }
            observations.append(">.\n");

            consortiumCount++; // increment the counter for the next consortium
        }

        return observations.toString();
    }

    private String generateJudgesInformation(String judgeType, List<Consortium> opinions) {
        StringBuilder judgesInformation = new StringBuilder();

        // Grupowanie opinii według łańcucha rozumowania
        Map<ReasoningChain, Set<Agent>> groupedOpinions = opinions.stream()
                .collect(Collectors.groupingBy(
                        Consortium::getReasoningChain,
                        Collectors.flatMapping(e -> e.getAgents().stream(), Collectors.toSet())
                ));

        for (Map.Entry<ReasoningChain, Set<Agent>> entry : groupedOpinions.entrySet()) {
            judgesInformation.append(judgeType);
            judgesInformation.append("<");
            judgesInformation.append(entry.getKey().toString());
            judgesInformation.append(",");
            Proposition decision = entry.getKey().getDecision();
            judgesInformation.append(decision == null ? "not decided" : decision.toString());
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
        courtRuling.append("Decision = ");
        if (courtOpinion.getDecision() == null) {
            courtRuling.append("draw");
        }
        else {
            courtRuling.append(courtOpinion.getDecision().toString());
        }
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

    public void generateReportPDF(List<ReportSection> reportSections, String destinationPath) {
        try (PDDocument document = new PDDocument()) {
            PDType0Font font = PDType0Font.load(document, new File("src/main/resources/pdf/dejavu-fonts-ttf-2.37/ttf/DejaVuSans.ttf"));

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            float yPosition = page.getMediaBox().getHeight() - 50;
            float titleIndent = 70;
            float contentIndent = 150;
            float bottomMargin = 50;

            for (ReportSection section : reportSections) {
                PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
                contentStream.setFont(font, 14);

                // Add title
                contentStream.beginText();
                contentStream.newLineAtOffset(titleIndent, yPosition);
                contentStream.showText(section.getTitle());
                contentStream.endText();
                yPosition -= 20;  // Move down for content
                contentStream.setFont(font, 12);

                // Add content
                String[] lines = section.getContent().split("\n");

                for (String line : lines) {
                    int index = 0;
                    while (index < line.length()) {
                        if (yPosition < bottomMargin) {
                            if (contentStream != null) {
                                contentStream.close();
                            }
                            page = new PDPage(PDRectangle.A4);
                            document.addPage(page);
                            yPosition = page.getMediaBox().getHeight() - 50;
                            contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true, true);
                            contentStream.setFont(font, 12);
                        }

                        String subLine;
                        if (line.length() > index + 50) {
                            int end = line.lastIndexOf(" ", index + 50);
                            if (end == -1) {
                                end = index + 50;
                            }
                            subLine = line.substring(index, end);
                            index = end + 1;
                        } else {
                            subLine = line.substring(index);
                            index = line.length();
                        }
                        contentStream.beginText();
                        contentStream.newLineAtOffset(contentIndent, yPosition);
                        contentStream.setFont(font, 14);
                        contentStream.showText(subLine);
                        contentStream.endText();
                        yPosition -= 16;
                    }
                    yPosition -= 30;
                }

                if (contentStream != null) {
                    contentStream.close();
                }
            }

            document.save(destinationPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
        this.listProposition.setListProposition(listIncompProp.getPropositions());
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
