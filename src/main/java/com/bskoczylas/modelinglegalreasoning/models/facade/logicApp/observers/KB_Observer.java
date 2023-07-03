package com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.observers;

import com.bskoczylas.modelinglegalreasoning.models.facade.logicApp.KnowledgeBase.ListKnowledgeBase;

public interface KB_Observer {
    void updateKB(ListKnowledgeBase listknowledgeBase);
}
