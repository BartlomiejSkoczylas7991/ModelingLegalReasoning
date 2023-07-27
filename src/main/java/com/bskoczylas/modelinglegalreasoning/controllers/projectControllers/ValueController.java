package com.bskoczylas.modelinglegalreasoning.controllers.projectControllers;

import com.bskoczylas.modelinglegalreasoning.controllers.ProjectController;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observable.ValueControllerObservable;
import com.bskoczylas.modelinglegalreasoning.controllers.projectControllers.Observer.observer.ValueControllerObserver;
import com.bskoczylas.modelinglegalreasoning.domain.models.Project;
import com.bskoczylas.modelinglegalreasoning.domain.models.facade.logicApp.value.Value;
import javafx.collections.FXCollections;
import javafx.scene.control.Alert;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.util.ArrayList;
import java.util.List;

public class ValueController implements ValueControllerObservable {
    private TableView<Value> valueTable;
    private Project project;
    private TextField valueNameTextField;
    private final List<ValueControllerObserver> observers = new ArrayList<>();

    public ValueController(ProjectController projectController) {
        this.valueTable = projectController.getValueTable();
        this.valueNameTextField = projectController.getValueNameTextField();
        this.project = projectController.getProject();
    }

    public Project getProject() {
        return project;
    }

    public void addValue(Value value) {
        if (!project.getListValue().getValues().stream().anyMatch(existingValue -> existingValue.getName().equals(value.getName()))) {
            project.getListValue().addValue(value);
            updateValueTable();
            notifyValueContrObservers();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Value with this name already exists!");

            alert.showAndWait();
        }
    }

    public void removeValue(Value value) {
        if (project.getListValue().removeValue(value)) {
            updateValueTable();
            notifyValueContrObservers();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Value doesn't exist!");

            alert.showAndWait();
        }
    }

    public void handleAddValue() {
        String valueName = valueNameTextField.getText().trim(); // pobierz nazwę wartości z pola tekstowego

        if (!valueName.isEmpty()) {
            Value newValue = new Value(valueName); // utwórz nową instancję wartości
            addValue(newValue); // dodaj nowego wartość do projektu
            valueNameTextField.clear(); // wyczyść pole tekstowe
            updateValueTable(); // zaktualizuj tabelę po dodaniu wartości
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


    @Override
    public void addValueContrObserver(ValueControllerObserver observer) {
        observers.add(observer);
    }

    @Override
    public void removeValueContrObserver(ValueControllerObserver observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyValueContrObservers() {
        for (ValueControllerObserver observer : this.observers) {
            observer.updateValueController(this);
        }
    }
}
