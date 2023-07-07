package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.ReasoningChain.ListReasoningChain;

public interface RC_Observer {
    void updateRC(ListReasoningChain listReasoningChain);
}
