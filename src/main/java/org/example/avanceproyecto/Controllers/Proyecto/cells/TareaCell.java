package org.example.avanceproyecto.Controllers.Proyecto.cells;

import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.util.ArrayList;

import javafx.collections.ObservableList;
import org.example.avanceproyecto.ControllerUtils.Dialogs.TareaAsignacionProyectoDialog;
import org.example.avanceproyecto.Controllers.Proyecto.objects.EmpleadoTarea;
import org.example.avanceproyecto.Controllers.Proyecto.objects.ProyectoObjectCreados;
import org.example.avanceproyecto.Controllers.Proyecto.objects.TareaObject;
import org.example.avanceproyecto.Controllers.SharedStates;

import java.util.concurrent.atomic.AtomicInteger;

public class TareaCell extends TableCell<EmpleadoTarea, String> {
    private TextField textField;
    private static TareaAsignacionProyectoDialog tareaAsignacionProyectoDialog;
    ObjectProperty<ProyectoObjectCreados> current_project;
    AtomicInteger tareas_asignadas;


    public TareaCell(ObjectProperty<ProyectoObjectCreados> current_project, SharedStates sharedStates, AtomicInteger tareas_num) {
        this.tareas_asignadas = tareas_num;
        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                if (!isEditing()) {
                    startEdit();
                }
            }
        });

        current_project.addListener(new ChangeListener<ProyectoObjectCreados>() {
            @Override
            public void changed(ObservableValue<? extends ProyectoObjectCreados> observableValue, ProyectoObjectCreados proyectoObject, ProyectoObjectCreados t1) {
                if (proyectoObject !=null) {
                    clearColumns();
                    if (t1 != null)
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
