package com.bskoczylas.modelinglegalreasoning.models.observers;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Decision;

public interface Decision_Observer {
    public void update(Decision decision);
}
