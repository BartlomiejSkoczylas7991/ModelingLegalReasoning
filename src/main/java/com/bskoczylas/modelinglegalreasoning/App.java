package com.bskoczylas.modelinglegalreasoning;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.controllers.StartWindowController;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.ProjectManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application {
    private ProjectManager projectManager = new ProjectManager();
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showStartWindow();
    }

    public void showStartWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/your/startWindow.fxml"));
            Parent root = loader.load();

            StartWindowController controller = loader.getController();
            controller.setMainApp(this);
            controller.setProjectManager(projectManager);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProjectWindow(Project project) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/path/to/your/projectWindow.fxml"));
            Parent root = loader.load();

            ProjectController controller = loader.getController();
            controller.setProject(project);
            controller.setProjectManager(projectManager);

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}