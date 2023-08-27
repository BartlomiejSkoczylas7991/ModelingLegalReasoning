package com.bskoczylas.modelinglegalreasoning;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;


public class App extends Application {
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) throws IOException {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Modeling Legal Reasoning");
        this.primaryStage.setResizable(false);

        showProjectWindow();
    }

    public void showProjectWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/project.fxml"));
            ProjectController controller = new ProjectController(primaryStage);
            loader.setController(controller);
            Parent root = loader.load();

            controller.setApp(this);
            controller.setStage(primaryStage);

            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
