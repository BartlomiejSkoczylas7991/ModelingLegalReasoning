package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observables;

public interface CourtOpinionObservable {
    void addObserver(CourtOpinionObservable observer);
    void removeObserver(CourtOpinionObservable observer);
    void notifyObservers();
}
