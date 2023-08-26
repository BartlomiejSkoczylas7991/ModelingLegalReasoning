module com.bskoczylas.modelinglegalreasoning {
    requires javafx.controls;
    requires javafx.fxml;
        requires javafx.web;
            
        requires org.controlsfx.controls;
            requires com.dlsc.formsfx;
                requires org.kordamp.ikonli.javafx;
            requires org.kordamp.bootstrapfx.core;
    requires java.logging;
    requires org.apache.pdfbox;
    requires java.desktop;
    opens com.bskoczylas.modelinglegalreasoning to javafx.fxml;
    exports com.bskoczylas.modelinglegalreasoning;
    exports com.bskoczylas.modelinglegalreasoning.controllers;

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