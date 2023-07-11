package com.bskoczylas.modelinglegalreasoning.controllers;

import com.bskoczylas.modelinglegalreasoning.App;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.services.ProjectManager;
import com.fasterxml.jackson.annotation.JsonProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

// Można dodać projekt od nowa (1), można wczytać projekt (2), można usunąć projekt z
// listy (3), można edytować nazwę, trzeba wyświetlić spis projektów (4), zamknąć program
// przy wciśnięciu "Exit" (5)
public class StartWindowController {
    private ProjectManager projectManager;
    private App mainApp;

    @FXML
    private TableView<Project> projectsTable;

    @FXML
    private TableColumn<Project, Integer> idColumn;

    @FXML
    private TableColumn<Project, String> createdColumn;

    @FXML
    private TableColumn<Project, String> modifiedColumn;

    @FXML
    private TableColumn<Project, String> nameColumn;

    @FXML
    private Button AddProject;

    @FXML
    private Button loadStartProjectButton;

    @FXML
    private Button deleteStartProjectButton;

    @FXML
    private Button exitStartButton;

    @FXML
    private Button editProjectNameButton;

    @FXML
    private TextField nameTextField;

    private ObservableList<Project> projectsData = FXCollections.observableArrayList();

    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
        initProjectsTable();
    }

    // Creating table of projects
    private void initProjectsTable() {
        // Tutaj przypisujemy dane z ObservableList do TableView
        projectsTable.setItems(projectsData);

        // Teraz definiujemy, jak dane z obiektów Project mają być wyświetlane w kolumnach tabeli
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        createdColumn.setCellValueFactory(new PropertyValueFactory<>("created"));
        modifiedColumn.setCellValueFactory(new PropertyValueFactory<>("modified"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        // Wczytaj projekty z użyciem Jackson
        loadProjects();
    }

    private void loadProjects() {
        // Pobierz projekty z ProjectManager lub innego źródła danych
        List<Project> loadedProjects = projectManager.getProjects().collect(Collectors.toList());

        // Dodaj wczytane projekty do ObservableList
        projectsData.addAll(loadedProjects);
    }

    //public void setProjectManager(ProjectManager projectManager) {
     //   this.projectManager = projectManager;
    //}

    @FXML
    private void initialize() {
        // Kod inicjalizacji, jeśli jest potrzebny. Jest to miejsce, gdzie można zainicjować elementy interfejsu użytkownika,
        // takie jak przyciski, pola tekstowe itp., które są zdefiniowane w pliku FXML.

        // Na przykład, możesz chcieć dodać słuchacza do przycisku:
        AddProject.setOnAction(event -> handleAddProjectButton());


    }


    public void setProjectManager(ProjectManager projectManager){
        this.projectManager = projectManager;
    }

    private void handleOpenButton() {

    }

    // Adding a project
    @FXML
    private void handleAddProjectButton() {
        String projectName = nameTextField.getText();
        if (!projectName.isEmpty()) {
            // Tworzenie nowego projektu na podstawie wprowadzonej nazwy
            Project newProject = projectManager.createProject(projectName);

            // Dodanie nowego projektu do tabeli lub innego źródła danych
            projectsData.add(newProject);

            // Wyczyszczenie pola tekstowego
            nameTextField.clear();
        }
    }

    @FXML
    private void handleDeleteProject() throws IOException {
        // Pobierz zaznaczony projekt z tabelki
        Project selectedProject = projectsTable.getSelectionModel().getSelectedItem();

        if (selectedProject != null) {
            // Usuń projekt z tabelki
            this.projectsData.remove(selectedProject);

            // Zapisz zmodyfikowane projekty do pliku
            projectManager.saveProjectToFile(projectsData, new File("com/bskoczylas/modelinglegalreasoning/projectData.json"));
        }
    }
}