package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class ValueController {
    private TableView<Value> valueTable;
    private ProjectController projectController;
    private Project project;
    private TextField valueNameTextField;

    public ValueController(TableView<Value> valueTable, TextField valueNameTextField,
                           ProjectController projectController) {
        this.valueTable = valueTable;
        this.valueNameTextField = valueNameTextField;
        this.projectController = projectController;
        this.project = projectController.getProject();
    }

    public void addValue(Value value) {
        if (!project.getListValue().getValues().stream().anyMatch(existingValue -> existingValue.getName().equals(value.getName()))) {
            project.getListValue().addValue(value);
            updateValueTable();
        } else {
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
