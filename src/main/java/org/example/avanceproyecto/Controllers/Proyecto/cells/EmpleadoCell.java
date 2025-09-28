package org.example.avanceproyecto.Controllers.Proyecto.cells;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableRow;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.example.avanceproyecto.ControllerUtils.Dialogs.EmpleadoSelectionDialog;
import org.example.avanceproyecto.ControllerUtils.Empleado;
import org.example.avanceproyecto.Controllers.Proyecto.objects.EmpleadoTarea;
import org.example.avanceproyecto.Controllers.SharedStates;

//Cells
public class EmpleadoCell extends TableCell<EmpleadoTarea, String> {
    private TextField textField;
    private static EmpleadoSelectionDialog employeeSelectionDialog;


    public EmpleadoCell(SharedStates sharedStates) {

        this.addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 1) {
                if (!isEditing()) {
                    startEdit();
                }
            }
        });


        employeeSelectionDialog = new EmpleadoSelectionDialog(sharedStates.getStage(), sharedStates);
    }

    @Override
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        Empleado empleado = getValue();
        if (empty || empleado == null) {
            setText(null);
            setGraphic(null);
        } else {
            setText(empleado.getFullName());

        }
    }

    @Override
    public void startEdit() {
        super.startEdit();

        Empleado currentValue = getValue();
        if (currentValue != null) {
            employeeSelectionDialog.set_choice_boxes_to_current_value(currentValue); //Does
            setText(currentValue.getFullName());
        }

        if (employeeSelectionDialog.show()) {
            EmpleadoSelectionDialog.EmployeeSelectionResult employeeSelectionResult = employeeSelectionDialog.getDialogResult();
            Empleado empleado = employeeSelectionResult.getEmpleado();
            setValue(empleado);
            commitEdit(empleado.getFullName());
        }
        cancelEdit();
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        if (getValue() != null)
            setText(getValue().getFullName());
    }

    @Override
    public void commitEdit(String newValue) {
        super.commitEdit(newValue);
        setText(newValue);
    }


    private Empleado getValue() {
        TableRow<EmpleadoTarea> tableRow = getTableRow();
        if (tableRow != null && tableRow.getItem() != null) {
            EmpleadoTarea empleadoTarea = tableRow.getItem();
            return empleadoTarea.getEmpleado();
        }
        return null;
    }

    private void setValue(Empleado empleado) {
        TableRow<EmpleadoTarea> tableRow = getTableRow();
        if (tableRow != null && tableRow.getItem() != null) {
            EmpleadoTarea empleadoTarea = tableRow.getItem();
            empleadoTarea.setEmpleado(empleado);
        }
    }
}
