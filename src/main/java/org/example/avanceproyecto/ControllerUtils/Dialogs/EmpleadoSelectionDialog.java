package org.example.avanceproyecto.ControllerUtils.Dialogs;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.example.avanceproyecto.ControllerUtils.Empleado;
import org.example.avanceproyecto.Controllers.SharedStates;

import java.util.ArrayList;
import java.util.List;

/**
 * Dialogo que te muestra choicebox departamento y otro choicebox con los empleados de ese departamento
 */
public class EmpleadoSelectionDialog extends BaseDialog<EmpleadoSelectionDialog.EmployeeSelectionResult> {
    private ChoiceBox<String> departamentoChoiceBox;
    private ChoiceBox<Empleado> empleadoChoiceBox;
    ObservableList<Empleado> empleado_data  = FXCollections.observableArrayList(); //Fata de todos los tareas nodos
    private SharedStates sharedStates;
    static Rectangle2D dims = new Rectangle2D(0,0,400,200);

    boolean skipListeningOneTime = false;

    
    public EmpleadoSelectionDialog(Stage parentStage, SharedStates sharedStates) {
        super(parentStage, "Select Employee",dims);
        this.sharedStates = sharedStates;
        fill_choicebox();
        departamentos_listener();
    }
    private void fill_choicebox() {
       ObservableList<String> data = FXCollections.observableArrayList(); //Fata de todos los tareas nodos
        for (String s: sharedStates.getDepartamentos_names()) {
            data.add(s);
        }
        departamentoChoiceBox.setItems(data);
        empleadoChoiceBox.setItems(empleado_data);



        empleadoChoiceBox.setConverter(new StringConverter<Empleado>() {
            @Override
            public String toString(Empleado empleado) {
                if (empleado != null) {
                    return empleado.getFullName();
                }
                return "Seleccionar";
            }

            @Override
            public Empleado fromString(String s) {
                return null;
            }
        });
        departamentoChoiceBox.setConverter(new StringConverter<String>() {
            @Override
            public String toString(String s) {
               if (s != null) {
                   return s;
               }
               return "Seleccionar";
            }

            @Override
            public String fromString(String s) {
                return "";
            }
        });
        empleadoChoiceBox.setValue(null);
        departamentoChoiceBox.setValue(null);

    }

    private void fill_empleado_choicebox(ArrayList<Empleado> empleados) {

        empleado_data.clear();
        for (Empleado e: empleados) {
            empleado_data.add(e);
        }

    }

    private void departamentos_listener() {
        departamentoChoiceBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String neww) {
                if (skipListeningOneTime) {
                    skipListeningOneTime = false;
                    return;
                }
                empleadoChoiceBox.setDisable(false);
                fill_empleado_choicebox(sharedStates.getEmpleadosArray(neww));
            }
        });
    }


    
    @Override
    protected GridPane createContent() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        Label deptLabel = new Label("Departamento:");
        departamentoChoiceBox = new ChoiceBox<>();
        grid.add(deptLabel, 0, 0);
        grid.add(departamentoChoiceBox, 1, 0);
        
        // Employee row
        Label empLabel = new Label("Empleado:");
        empleadoChoiceBox = new ChoiceBox<>();
        empleadoChoiceBox.setDisable(true);
        grid.add(empLabel, 0, 1);
        grid.add(empleadoChoiceBox, 1, 1);

        departamentoChoiceBox.setPrefWidth(200);
        empleadoChoiceBox.setPrefWidth(200);


        return grid;
    }
    
    @Override
    protected boolean validateInput() {

        if (departamentoChoiceBox.getValue() == null) {
            showError("Please select a department!\n");
            return  false;
        }
        
        if (empleadoChoiceBox.getValue() == null) {
            showError("Please select an employee!\n");
            return false;
        }
        

        return true;
    }
    
    @Override
    protected EmployeeSelectionResult getResult() {
        return new EmployeeSelectionResult(
            departamentoChoiceBox.getValue(),
            empleadoChoiceBox.getValue()
        );
    }

    @Override
    protected void clearValues() {
        skipListeningOneTime = true;
        departamentoChoiceBox.setValue(null);
        empleadoChoiceBox.setValue(null);
        empleadoChoiceBox.setDisable(true);
        empleadoChoiceBox.getItems().clear();
    }

    public void set_choice_boxes_to_current_value(Empleado empleado) {

        empleadoChoiceBox.setDisable(false);
        fill_empleado_choicebox(sharedStates.getEmpleadosArray(empleado.getDepartamentoNombre().toLowerCase()));
        System.out.println("Setiing values to boxes");
        skipListeningOneTime = true;
        int id = empleado.getDepartamentoId();
        String departamento = sharedStates.getDepartamentoName(id);
        empleadoChoiceBox.setValue(empleado);
        System.out.println("departamento is " + departamento);
        departamentoChoiceBox.setValue(departamento);

    }


    
    // Methods to populate the choice boxes
    public void setDepartments(List<String> departments) {
        departamentoChoiceBox.getItems().setAll(departments);
    }
    
    public void setEmployeesForDepartment(String department, List<Empleado> employees) {
        if (department.equals(departamentoChoiceBox.getValue())) {
            empleadoChoiceBox.getItems().setAll(employees);
        }
    }

    public static class EmployeeSelectionResult {
        private final String departamento;
        private final Empleado empleado;

        public EmployeeSelectionResult(String departamento, Empleado empleado) {
            this.departamento = departamento;
            this.empleado = empleado;
        }

        public String getDepartamento() { return departamento; }
        public Empleado getEmpleado() { return empleado; }
    }
}

