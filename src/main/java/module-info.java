module com.example.modelinglegalreasoning {
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
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Agent to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Consortium to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Court to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Decision to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.IncompProp to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.KnowledgeBase to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.PropBaseClean to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Proposition to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.ReasoningChain to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Rule to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AV to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.AVP to com.fasterxml.jackson.databind;
    exports com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Weights.Scale_Weight to com.fasterxml.jackson.databind;

    opens com.bskoczylas.modelinglegalreasoning.controllers to javafx.fxml;
    exports com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;
    opens com.bskoczylas.modelinglegalreasoning.controllers.projectControllers to javafx.fxml;
}