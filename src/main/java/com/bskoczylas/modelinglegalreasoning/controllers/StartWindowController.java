package com.bskoczylas.modelinglegalreasoning.controllers;

import com.bskoczylas.modelinglegalreasoning.App;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.services.ProjectManager;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import java.util.logging.Level;
import java.util.logging.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// Można dodać projekt od nowa (1), można wczytać projekt (2), można usunąć projekt z
// listy (3), można edytować nazwę, trzeba wyświetlić spis projektów (4), zamknąć program
// przy wciśnięciu "Exit" (5)
public class StartWindowController {
    // ZMIENNE
    private ProjectManager projectManager;
    private App mainApp;
    private Project currentProject;

    @FXML
    private TableView<Project> projectsTable;

    @FXML
    private TableColumn<Project, Integer> idstartColumn;

    @FXML
    private TableColumn<Project, String> createdstartColumn;

    @FXML
    private TableColumn<Project, String> modifiedstartColumn;

    @FXML
    private TableColumn<Project, String> namestartColumn;

    @FXML
    private Button AddProject;

    @FXML
    private Button loadProjectButton;

    @FXML
    private Button deleteProjectButton;

    @FXML
    private Button exitButton;

    @FXML
    private Button editNameButton;

    @FXML
    private TextField nameTextField;
    private static System Logger;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(StartWindowController.class.getName());

    public StartWindowController() {}

    private ObservableList<Project> projectsData = FXCollections.observableArrayList();

    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
        initProjectsTable();
    }

    public void initData() {
        loadProjectsIntoTable();
    }

    // Creating table of projects
    private void initProjectsTable() {
        // Here we assign data from ObservableList to TableView
        projectsTable.setItems(projectsData);

        // Defining how data from Project objects should be displayed in table columns
        idstartColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        createdstartColumn.setCellValueFactory(new PropertyValueFactory<>("created"));
        modifiedstartColumn.setCellValueFactory(new PropertyValueFactory<>("modified"));
        namestartColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Load projects using Jackson
        loadProjectsIntoTable();
    }

    public void setProjectManager(ProjectManager projectManager) {
        this.projectManager = projectManager;
        loadProjectsIntoTable();
    }

    private void loadProjectsIntoTable() {
        List<Project> projects = projectManager.getAllProjects(); // Cannot resolve method 'getAllProjects' in 'ProjectManager'
        projectsData.setAll(projects);
        if (projectManager == null) {
            System.out.println("ProjectManager is null");
            return;
        }
        // Uzyskaj strumień projektów z ProjectManager
        Stream<Project> projectStream = projectManager.getProjects();

        // Przekształć strumień do listy
        List<Project> projectList = projectStream.collect(Collectors.toList());

        // Utwórz ObservableList z listy projektów
        ObservableList<Project> observableProjects = FXCollections.observableArrayList(projectList);

        // Załaduj ObservableList do TableView
        projectsTable.setItems(observableProjects);
    }

    // INICJOWANIE AKCJI
    @FXML
    private void initialize() {
        AddProject.setOnAction(event -> handleAddProjectButton());
        loadProjectButton.setOnAction(event -> handleLoadButton());
        deleteProjectButton.setOnAction(event -> {
            try {
                handleDeleteProject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        exitButton.setOnAction(event -> handleExit());
        editNameButton.setOnAction(event -> {
            try {
                handleEditNameProject();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        projectsTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> currentProject = newValue
        );

        projectsTable.setEditable(true);
        namestartColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        namestartColumn.setOnEditCommit(
                (TableColumn.CellEditEvent<Project, String> t) -> {
                    Project editedProject = (Project) t.getTableView().getItems().get(t.getTablePosition().getRow());
                    editedProject.setName(t.getNewValue());
                    try {
                        // Aktualizacja projectManager
                        projectManager.saveProject(editedProject); // Cannot resolve method 'saveProject' in 'ProjectManager'
                    } catch (IOException e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error Dialog");
                        alert.setHeaderText("An error occurred:");
                        alert.setContentText(e.getMessage());

                        alert.showAndWait();
                    }
                }
        );
    }

    private void handleExit() {
        System.exit(0);
    }

    // Adding a project
    @FXML
    private void handleAddProjectButton() {
        String projectName = nameTextField.getText();
        if (!projectName.isEmpty()) {
            // Tworzenie nowego projektu na podstawie wprowadzonej nazwy
            Project newProject = projectManager.createProject(projectName, ); // brakuje description, tylko nie wiem co dokładnie (według Fabryki)

            // Dodanie nowego projektu do tabeli lub innego źródła danych
            projectsData.add(newProject);

            // Wyczyszczenie pola tekstowego
            nameTextField.clear();

            try {
                // Załaduj wybrany projekt


                // Tworzenie nowego okna
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/fxml/project.fxml"));
                Parent root = loader.load();

                // Przekazanie nowego projektu do kontrolera
                ProjectController projectController = loader.getController();
                projectController.setProject(newProject);

                // Zapisz nowy projekt w ProjectManager
                projectManager.saveProject(newProject); // Cannot resolve method 'saveProject' in 'ProjectManager'

                // Zamknij obecne okno i otwórz nowe okno projektu
                Stage stage = (Stage) nameTextField.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            }  catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("An error occurred:");
                alert.setContentText("There was a problem loading the project view. Check if the /fxml/project.fxml file exists and the path is correct.\n\nDetailed error: " + e.getMessage());
                alert.showAndWait();

                logger.log(Level.SEVERE, "An IO error occurred: ", e);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("An unexpected error occurred:");
                alert.setContentText("An unexpected error occurred while loading the project view. This could be due to an unforeseen issue.\n\nDetailed error: " + e.getMessage());
                alert.showAndWait();

                logger.log(Level.SEVERE, "An unexpected error occurred: ", e);
            }
        }
    }

    //WCZYTYWANIE PROJEKTU
    @FXML
    private void handleLoadButton() {
        if (currentProject != null) {
            try {
                // Załaduj wybrany projekt
                projectManager.openProject(currentProject.getId());

                // Tworzenie nowego okna
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/project.fxml"));
                Parent root = loader.load();

                // Przekazanie wybranego projektu do kontrolera
                ProjectController projectController = loader.getController();
                projectController.setProject(currentProject);

                // Zamknij obecne okno i otwórz nowe okno projektu
                Stage stage = (Stage) loadProjectButton.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("An error occurred:");
                alert.setContentText(e.getMessage());

                alert.showAndWait();
            }
        }
    }

    //USUWANIE PROJEKTU
    @FXML
    private void handleDeleteProject() throws IOException {
        // Pobierz zaznaczony projekt z tabelki
        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();

        if (selectedProject != null) {
            // Usuń projekt z tabelki
            this.projectsData.remove(selectedProject);

            // Usuń projekt z repozytorium
            projectManager.deleteProject(selectedProject);
        }
    }

    //ZMIANA NAZWY PROJEKTU
    @FXML
    private void handleEditNameProject() throws IOException {
        // Pobierz zaznaczony projekt z tabelki
        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();

        // Upewnij się, że został wybrany jakiś projekt
        if (selectedProject != null) {
            // Pobierz nową nazwę z pola tekstowego
            String newName = nameTextField.getText();

            // Upewnij się, że nowa nazwa nie jest pusta
            if (!newName.isEmpty()) {
                // Zmień nazwę projektu
                selectedProject.setName(newName);

                // Aktualizuj projekt w managerze
                try {
                    projectManager.saveProject(selectedProject); // tu też błąd
                } catch (IOException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Dialog");
                    alert.setHeaderText("An error occurred:");
                    alert.setContentText(e.getMessage());

                    alert.showAndWait();
                }

                // Aktualizuj tabelę
                projectsTable.refresh();

                // Wyczyść pole tekstowe
                nameTextField.clear();
            }
        }
    }
}