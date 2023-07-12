package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers.CourtOpinionObserver;

public interface CourtOpinionObservable {
    void addObserver(CourtOpinionObserver observer);
    void removeObserver(CourtOpinionObserver observer);
    void notifyObservers();
}
