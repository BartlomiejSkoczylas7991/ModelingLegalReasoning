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

import java.util.List;


public class App extends Application {
    private ProjectManager projectManager = new ProjectManager();
    private Stage primaryStage;
    ProjectRepository projectRepository = new JsonFileProjectRepository();
    List<Project> projects = projectRepository.findAll();

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