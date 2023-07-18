package com.bskoczylas.modelinglegalreasoning;

import com.bskoczylas.modelinglegalreasoning.adapters.JsonFileProjectRepository;
import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.controllers.StartWindowController;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.repositories.ProjectRepository;
import com.bskoczylas.modelinglegalreasoning.domain.models.FacadeProject;
import com.bskoczylas.modelinglegalreasoning.services.ProjectManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class App extends Application {
    private Stage primaryStage;
    private ProjectManager projectManager;
    private StartWindowController startWindowController;
    private JsonFileProjectRepository jsonFileProjectRepository;
    private FacadeProject facadeProject;


    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Modeling Legal Reasoning");
        this.jsonFileProjectRepository = new JsonFileProjectRepository();
        // initialize the FacadeProject here
        this.facadeProject = new FacadeProject(this.jsonFileProjectRepository);

        // initialize the ProjectManager here
        this.projectManager = new ProjectManager(facadeProject);

        showStartWindow();
    }

    public void showStartWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/start.fxml"));
            Parent root = loader.load();

            StartWindowController controller = loader.getController();
            controller.setProjectManager(this.projectManager);
            controller.setMainApp(this);

            // Initialize the controller data
            controller.initData();

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("IOException: " + e.getMessage());
        } catch (RuntimeException e) {
            e.printStackTrace();
            System.out.println("RuntimeException: " + e.getMessage());
        }
    }

    public void showProjectWindow(Project project) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/project.fxml"));
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