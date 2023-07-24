module com.bskoczylas.modelinglegalreasoning {
    requires javafx.controls;
    requires javafx.fxml;
        requires javafx.web;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
                requires org.kordamp.ikonli.javafx;
            requires org.kordamp.bootstrapfx.core;
            requires eu.hansolo.medusa;
    requires com.fasterxml.jackson.databind;
    requires java.logging;

    opens com.bskoczylas.modelinglegalreasoning to javafx.fxml;
    exports com.bskoczylas.modelinglegalreasoning;
    exports com.bskoczylas.modelinglegalreasoning.controllers;

    exports com.bskoczylas.modelinglegalreasoning.domain.models to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.dataStructures to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.consortium to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.court to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.decision to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.knowledgeBase to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.propBaseClean to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.reasoningChain to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight to com.fasterxml.jackson.databind;
    opens com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.agent to javafx.base;
    opens com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value to javafx.base;
    opens com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.proposition to javafx.base;
    opens com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.incompProp to javafx.base;
    opens com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.rule to javafx.base;
    opens com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.av to javafx.base;
    opens com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.avp to javafx.base;
    opens com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.weights.scale_Weight to javafx.base;

    opens com.bskoczylas.modelinglegalreasoning.controllers to javafx.fxml;
    exports com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;
    opens com.bskoczylas.modelinglegalreasoning.controllers.projectControllers to javafx.fxml;
}