package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Decision.Decision;

public interface Decision_Observer {
    public void update(Decision decision);
}
