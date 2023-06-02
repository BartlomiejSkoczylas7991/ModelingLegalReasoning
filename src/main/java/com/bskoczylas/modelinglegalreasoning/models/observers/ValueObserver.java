package com.bskoczylas.modelinglegalreasoning.models.observers;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.Value;

public interface ValueObserver {
    void updateValue(Value value);
}
