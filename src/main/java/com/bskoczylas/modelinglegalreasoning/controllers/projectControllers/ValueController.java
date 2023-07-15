package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.Value;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ValueController {
    private TableView<Value> valueTable;
    private ProjectController projectController;
    private Project project;
    private Button addValueButton;
    private Button editValueButton;
    private Button removeValueButton;
    private TextField valueNameTextField;

    public ValueController(TableView<Value> valueTable, TextField valueNameTextField,
                           Button addValueButton, Button editValueButton, Button removeValueButton,
                           ProjectController projectController) {
        this.valueTable = valueTable;
        this.valueNameTextField = valueNameTextField;
        this.addValueButton = addValueButton;
        this.editValueButton = editValueButton;
        this.removeValueButton = removeValueButton;
        this.projectController = projectController;
        this.project = projectController.getProject();
    }

    private void setupValueButtons() {
        addValueButton.setOnAction(e -> handleAddValue());
        editValueButton.setOnAction(e -> handleEditValue());
        removeValueButton.setOnAction(e -> handleRemoveValue());
    }

    public void addValue(Value value) {
        if (!project.getListValue().getValues().stream().anyMatch(existingValue -> existingValue.getName().equals(value.getName()))) {
            project.getListValue().addValue(value);
            updateValueTable();
        } else {
            // TODO: Show error to user, value with this name already exists
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Value with this name already exists!");

            alert.showAndWait();
        }
    }

    public void editValue(Value oldValue, Value newValue) {
        int index = project.getListValue().getValues().indexOf(oldValue);
        if (index != -1) {
            project.getListValue().getValues().set(index, newValue);
            updateValueTable();
        } else {
            // Show error to user, value does not exist
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Value doesn't exist!");

            alert.showAndWait();
        }
    }

    public void removeValue(Value value) {
        if (project.getListValue().getValues().remove(value)) {
            updateValueTable();
        } else {
            // TODO: Show error to user, value does not exist
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Value doesn't exist!");

            alert.showAndWait();
        }
    }

    public void handleAddValue() {
        String valueName = valueNameTextField.getText(); // pobierz nazwę wartości z pola tekstowego

        if (!valueName.isEmpty()) {
            Value newValue = new Value(valueName); // utwórz nową instancję wartości
            addValue(newValue); // dodaj nowego wartość do projektu
            valueNameTextField.clear(); // wyczyść pole tekstowe
            updateValueTable(); // zaktualizuj tabelę po dodaniu wartości
        }
    }

    public void handleEditValue() {
        Value selectedValue = valueTable.getSelectionModel().getSelectedItem();
        if (selectedValue != null) {
            String valueName = valueNameTextField.getText();
            if (!valueName.isEmpty()) {
                Value newValue = new Value(valueName);
                editValue(selectedValue, newValue);
                valueNameTextField.clear();
                updateValueTable(); // zaktualizuj tabelę po edycji wartości
            }
        } else {
            // TODO: Show error to user, no value selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No value selected!");

            alert.showAndWait();
        }
    }

    public void handleRemoveValue() {
        Value selectedValue = valueTable.getSelectionModel().getSelectedItem();
        if (selectedValue != null) {
            removeValue(selectedValue);
            updateValueTable(); // zaktualizuj tabelę po usunięciu wartości
        } else {
            // TODO: Show error to user, no value selected
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("No value selected!");

            alert.showAndWait();
        }
    }

    public void updateValueTable() {
        valueTable.setItems(FXCollections.observableArrayList(project.getListValue().getValues()));
    }
}
