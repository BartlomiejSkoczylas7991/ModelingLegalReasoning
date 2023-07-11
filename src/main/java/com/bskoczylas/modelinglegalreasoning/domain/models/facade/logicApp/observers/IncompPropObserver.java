package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp.ListIncompProp;

public interface IncompPropObserver {
    void updateIncomp(ListIncompProp listIncompProp);
}
