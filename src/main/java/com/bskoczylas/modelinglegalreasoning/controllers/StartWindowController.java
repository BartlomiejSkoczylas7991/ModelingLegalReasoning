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
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;
import java.util.stream.Collectors;

public class StartWindowController {
    private ProjectManager projectManager;
    private App mainApp;

    @FXML
    private Button openButton;

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
    private Button loadStartProjectButton;

    @FXML
    private Button deleteStartProjectButton;

    @FXML
    private Button exitStartButton;

    @FXML
    private Button editProjectNameButton;

    private ObservableList<Project> projectsData = FXCollections.observableArrayList();

    public void setMainApp(App mainApp) {
        this.mainApp = mainApp;
        initProjectsTable();
    }

    private void initProjectsTable() {
        // Tutaj przypisujemy dane z ObservableList do TableView
        projectsTable.setItems(projectsData);

        // Teraz definiujemy, jak dane z obiektów Project mają być wyświetlane w kolumnach tabeli
        TableColumn<Project, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Project, String> nameColumn = new TableColumn<>("Nazwa");
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        projectsTable.getColumns().setAll(idColumn, nameColumn);

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
        openButton.setOnAction(event -> handleStartButton());
    }

    // Metoda obsługująca kliknięcie przycisku start.
    private void handleStartButton() {
        // Tutaj możesz umieścić kod, który zostanie wykonany, gdy użytkownik kliknie przycisk start.
        // Na przykład, możesz chcieć przejść do innego widoku:
        //Project project = projectManager.createProject(); // lub cokolwiek innego, co tworzy nowy projekt
        //mainApp.showProjectWindow(project);
    }

    public void setProjectManager(ProjectManager projectManager){
        this.projectManager = projectManager;
    }

    private void handleOpenButton() {

    }

    private void handle
}