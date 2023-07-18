package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp.ListIncompProp;

public interface IncompPropObserver {
    void updateIncomp(ListIncompProp listIncompProp);
}
