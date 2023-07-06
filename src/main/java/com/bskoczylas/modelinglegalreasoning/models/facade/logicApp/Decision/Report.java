package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Decision;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Agent.Agent;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Consortium.Consortium;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Consortium.ConsortiumType;
import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Court.Court;

import java.util.Map;

public class Report {
    public String generateReport(Court court) {
        StringBuilder report = new StringBuilder();

        // Lista konsorcjów i ich typów
        report.append("Consortiums and their types:\n");
        for (Map.Entry<Consortium, ConsortiumType> entry : court.getConsortiumMap().entrySet()) {
            Consortium consortium = entry.getKey();
            ConsortiumType consortiumType = entry.getValue();
            report.append("Consortium with reasoning chain ").append(consortium.getReasoningChain().toString())
                    .append(" is of type ").append(consortiumType.name()).append("\n");
        }

        // Większość sędziów i ich zdanie
        report.append("Majority Judges:\n");
        for (Agent agent : court.getMajorityJudges()) {
            report.append(agent.getName()).append("\n");
        }

        report.append("Majority opinion: ").append(court.getMajorityOpinion().toString()).append("\n");

        // Sędziowie dysydenccy i ich zdanie
        report.append("Dissenting Judges:\n");
        for (Agent agent : court.getDissentingJudges()) {
            report.append(agent.getName()).append("\n");
        }

        report.append("Dissenting opinion: ").append(court.getDissentingOpinion().toString()).append("\n");

        return report.toString();
    }
}
