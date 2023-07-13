package com.bskoczylas.modelinglegalreasoning;

import com.bskoczylas.modelinglegalreasoning.adapters.JsonFileProjectRepository;
import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.controllers.StartWindowController;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.repositories.ProjectRepository;
import com.bskoczylas.modelinglegalreasoning.services.ProjectManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class App extends Application {
    ProjectRepository projectRepository = new JsonFileProjectRepository();
    ProjectManager projectManager = new ProjectManager(projectRepository);
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showStartWindow();
    }

    public void showStartWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/main/java/com/bskoczylas/modelinglegalreasoning/resources/fxml/start.fxml"));
            Parent root = loader.load();

            StartWindowController controller = loader.getController();
            controller.setMainApp(this);
            controller.setProjectManager(this.projectManager);

            // Initialize the controller data
            controller.initData();

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("An error occurred:");
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }

    public void showProjectWindow(Project project) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/main/java/com/bskoczylas/modelinglegalreasoning/resources/fxml/project.fxml"));
            Parent root = loader.load();

            ProjectController controller = loader.getController();
            controller.setProject(project);
            controller.setProjectManager(projectManager);

            // Close the current stage
            primaryStage.close();

            // Create and show the new stage
            primaryStage = new Stage();
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    public static void main(String[] args) {
        launch(args);
    }
}