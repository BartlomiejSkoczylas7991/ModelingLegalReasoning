package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Court;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Consortium.ListConsortium;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.ListIncompProp;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.PropBaseClean.ListPropBaseClean;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.ReasoningChain.ListReasoningChain;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule.ListRules;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.CourtOpinionObserver;

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
public class Report implements CourtOpinionObserver {
    private ListPropBaseClean listPropBaseClean = new ListPropBaseClean();
    private ListRules rules = new ListRules();
    private ListReasoningChain listReasoningChains = new ListReasoningChain();
    private ListConsortium observations = new ListConsortium();
    private CourtOpinion courtOpinion = new CourtOpinion();
    private ListIncompProp incompProp;

    public Report() {}

    public void setPropositionBase(ListPropBaseClean propBase) {
        this.listPropBaseClean = propBase;
    }

    @Override
    public void updateCourtOpinion(CourtOpinion courtOpinion) {
        this.courtOpinion = courtOpinion;
    }

    public void generateReport() {
        // Tutaj logika generowania raportu, która korzysta z zgromadzonych danych.
    }
}
