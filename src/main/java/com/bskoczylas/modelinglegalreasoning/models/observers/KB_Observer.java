package com.bskoczylas.modelinglegalreasoning.models.observers;

import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.KnowledgeBase;
import com.bskoczylas.modelinglegalreasoning.models.Facade.logicApp.ListKnowledgeBase;

public interface KB_Observer {
    void updateKB(ListKnowledgeBase listknowledgeBase);
}
