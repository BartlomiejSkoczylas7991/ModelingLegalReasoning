package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.Value.Value;

public interface ValueObserver {
    void updateValue(Value value);
}
