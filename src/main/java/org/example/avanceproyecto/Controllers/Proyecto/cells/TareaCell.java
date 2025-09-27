package org.example.avanceproyecto.Controllers.Proyecto.cells;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Set;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import kotlin.collections.UArraySortingKt;
import org.example.avanceproyecto.ControllerUtils.*;
import org.example.avanceproyecto.ControllerUtils.Dialogs.TareaAsignacionProyectoDialog;
import org.example.avanceproyecto.Controllers.Proyecto.cells.EmpleadoCell;
import org.example.avanceproyecto.Controllers.Proyecto.objects.EmpleadoTarea;
import org.example.avanceproyecto.Controllers.Proyecto.objects.ProyectoObject;
import org.example.avanceproyecto.Controllers.Proyecto.objects.TareaObject;
import org.example.avanceproyecto.Controllers.SharedStates;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;

public class TareaCell extends TableCell<EmpleadoTarea, String> {
    private TextField textField;
    private static TareaAsignacionProyectoDialog tareaAsignacionProyectoDialog;
    ObjectProperty<ProyectoObject> current_project;
    AtomicInteger tareas_asignadas;


    public TareaCell(ObjectProperty<ProyectoObject> current_project, SharedStates sharedStates, AtomicInteger tareas_num) {
        this.tareas_asignadas = tareas_num;
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                if (!isEditing()) {
                    startEdit();
                }
            }
        });

        current_project.addListener(new ChangeListener<ProyectoObject>() {
            @Override
            public void changed(ObservableValue<? extends ProyectoObject> observableValue, ProyectoObject proyectoObject, ProyectoObject t1) {
                if (proyectoObject !=null) {
                    clearColumns();
                    tareaAsignacionProyectoDialog.populate_choicebox(t1.getTareas_proyecto());
                }
            }
        });

        this.current_project = current_project;
        tareaAsignacionProyectoDialog = new TareaAsignacionProyectoDialog(sharedStates.getStage());
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        TareaObject tareaObject = getValue();
        if (empty || tareaObject == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(tareaObject.getTitle());

        }
    }

    @Override
    public void startEdit() {
        super.startEdit();

        tareaAsignacionProyectoDialog.populate_choicebox(current_project.get().getTareas_proyecto());
        TareaObject currentValue = getValue();
        if (currentValue != null) {
            tareaAsignacionProyectoDialog.setChoiceBoxValue(currentValue); //Does
            setText(currentValue.getTitle());
        }

        if (tareaAsignacionProyectoDialog.show()) {
            TareaObject tareaObject = (TareaObject)tareaAsignacionProyectoDialog.getDialogResult();
            setValue(tareaObject);
            commitEdit(tareaObject.getTitle());
        }
        cancelEdit();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        if (getValue() != null)
            setText(getValue().getTitle());
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        setText(newValue);
//        changeColorofCells();
    }


    private TareaObject getValue() {
        TableRow<EmpleadoTarea> tableRow = getTableRow();
        if (tableRow != null && tableRow.getItem() != null) {
            EmpleadoTarea empleadoTarea = tableRow.getItem();
            return empleadoTarea.getTareaObject();
        }
        System.out.println("get value is null");
        return null;
    }

    private void setValue(TareaObject tareaObject) {
        TableRow<EmpleadoTarea> tableRow = getTableRow();
        if (tableRow != null && tableRow.getItem() != null) {
            EmpleadoTarea empleadoTarea = tableRow.getItem();
            empleadoTarea.setTareaObject(tareaObject);
        }
    }

    private void clearColumns() {
        ObservableList<EmpleadoTarea> empleadoTareas = getTableView().getItems();

        for (EmpleadoTarea empleadoTarea:empleadoTareas) {
            empleadoTarea.setTareaObject(null);
        }
        getTableView().refresh();
    }

    private void changeColorofCells() {

        ArrayList<String> e = new ArrayList<>();
        ObservableList<EmpleadoTarea> empleadoTareas = getTableView().getItems();
        int completados = empleadoTareas.size();
        for (EmpleadoTarea empleadoTarea:empleadoTareas) {
            if (e.contains(empleadoTarea.getTareaObject().title)) {
                completados += -1;
            }
        }
//        tareas_asignadas.set(completados);
//        current_project.setValue(current_project.get());


    }
}
