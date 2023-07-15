package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.Value.Value;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;

public class ValueController {
    private TableView<Value> valueTable;
    private ProjectController projectController;
    private Project project;

    public ValueController(TableView<Value> valueTable, ProjectController projectController) {
        this.valueTable = valueTable;
        this.projectController = projectController;
        this.project = projectController.getProject();
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

    public void updateValueTable() {
        valueTable.setItems(FXCollections.observableArrayList(project.getListValue().getValues()));
    }
}
