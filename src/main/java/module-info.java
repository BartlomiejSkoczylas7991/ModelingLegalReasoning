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

    opens com.bskoczylas.modelinglegalreasoning to javafx.fxml;
    exports com.bskoczylas.modelinglegalreasoning;
    exports com.bskoczylas.modelinglegalreasoning.controllers;
    opens com.bskoczylas.modelinglegalreasoning.controllers to javafx.fxml;
    exports com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;
    opens com.bskoczylas.modelinglegalreasoning.controllers.projectControllers to javafx.fxml;
}