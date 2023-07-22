package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer;

import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.AVPController;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp.AgentValuePropWeight;

public interface AVPObserverController {
    void updateAVP(AVPController avpController);
}
