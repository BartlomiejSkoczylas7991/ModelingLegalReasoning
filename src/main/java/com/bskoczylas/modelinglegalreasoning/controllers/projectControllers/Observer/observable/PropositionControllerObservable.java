package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable;

import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.IncompPropController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.IncompControllerObserver;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.PropositionControllerObserver;

public interface PropositionControllerObservable {
    void addPropositionContrObserver(PropositionControllerObserver observer);
    void removePropositionContrObserver(PropositionControllerObserver observer);
    void notifyPropositionContrObservers();
}
