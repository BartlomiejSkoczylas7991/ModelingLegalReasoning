package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.ListValue;

public interface ValueObserver {
    void updateValue(ListValue listValue);
}
