package com.bskoczylas.modelinglegalreasoning.models.observables;

import com.bskoczylas.modelinglegalreasoning.models.observers.PropositionObserver;

public interface PropositionObservable {
    void addObserver(PropositionObserver observer);
    void removeObserver(PropositionObserver observer);
    void notifyObservers();
}
