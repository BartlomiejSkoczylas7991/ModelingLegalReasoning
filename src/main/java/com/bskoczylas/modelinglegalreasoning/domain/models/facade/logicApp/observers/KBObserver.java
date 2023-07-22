package com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase.ListKnowledgeBase;

public interface KBObserver {
    void updateKB(ListKnowledgeBase listknowledgeBase);
}
