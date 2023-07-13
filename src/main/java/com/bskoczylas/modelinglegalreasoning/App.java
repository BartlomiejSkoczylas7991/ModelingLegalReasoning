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
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;


public class App extends Application {
    private ProjectManager projectManager = new ProjectManager();
    private Stage primaryStage;
    ProjectRepository projectRepository = new JsonFileProjectRepository();
    List<Project> projects = projectRepository.findAll();

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        // Loading projects from the json file
        try {
            File file = new File("src/main/resources/com/bskoczylas/modelinglegalreasoning/projectData.json");
            this.projectManager.loadProjectFromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        showStartWindow();
    }

    public void showStartWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/main/java/com/bskoczylas/modelinglegalreasoning/resources/fxml/start.fxml"));
            Parent root = loader.load();

            StartWindowController controller = loader.getController();
            controller.setMainApp(this);
            controller.setProjectManager(this.projectManager); // mam tu błąd

            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProjectWindow(Project project) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("src/main/java/com/bskoczylas/modelinglegalreasoning/resources/fxml/project.fxml"));
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

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
        loadProjects(); // Load projects after setting the project manager
    }

    public static void main(String[] args) {
        launch(args);
    }
}