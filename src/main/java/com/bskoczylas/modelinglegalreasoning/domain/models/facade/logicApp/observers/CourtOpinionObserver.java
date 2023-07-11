package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Court.CourtOpinion;

public interface CourtOpinionObserver {
    public void updateCourtOpinion(CourtOpinion courtOpinion);
}
