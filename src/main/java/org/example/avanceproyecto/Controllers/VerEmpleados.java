package org.example.avanceproyecto.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.BaseController;
import org.example.avanceproyecto.ControllerUtils.Empleado;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Utils;
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.json.JSONObject;

import java.util.ArrayList;


@Setter @Getter
public class VerEmpleados extends BaseController implements Observer {

    @FXML
    private TableView<Empleado> table;

    JSONObject departamentos_id;
    private ObservableList<Empleado> data = FXCollections.observableArrayList();


    public VerEmpleados(String fxml) {
        initilize_fxml(fxml);
    }

    @Override
    public void init() {

        createTable();
    }



    private void createTable() {
        table.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        // Optional: Style the table cells specifically
        table.setRowFactory(tv -> {
            TableRow<Empleado> row = new TableRow<>();
            row.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            return row;
        });

        // Create columns
        TableColumn<Empleado, String> nombre_columna = new TableColumn<>("Empleado");
        nombre_columna.setCellValueFactory(new PropertyValueFactory<>("empleadoName"));
        nombre_columna.setPrefWidth(150);

        TableColumn<Empleado, String> apellido_column = new TableColumn<>("Apellido");
        apellido_column.setCellValueFactory(new PropertyValueFactory<>("empleadoLastName")); // Use camelCase
        apellido_column.setPrefWidth(200);


        TableColumn<Empleado, String> departamento_column = new TableColumn<>("Departamento");
        departamento_column.setCellValueFactory(cellData -> {
            Integer id = cellData.getValue().getDepartamentoId();
            String departamento = Utils.getDepartamentoById(departamentos_id,id);
            return new SimpleStringProperty(departamento); // or whatever field you want
        });
        TableColumn<Empleado, String> status_actividad = new TableColumn<>("Status actividad");
        status_actividad.setCellValueFactory( cellData -> {

            Empleado.ActividadStatus actividadStatus = cellData.getValue().getActividadStatus();
            String status  = actividadStatus == Empleado.ActividadStatus.Activo ? "Activo":"No activo";
            return new SimpleStringProperty(status); // or whatever field you want

        }); // Use camelCase
        apellido_column.setPrefWidth(200);

        departamento_column.setPrefWidth(300);
        table.setPrefWidth(800);
        table.setMaxWidth(800);
        table.setMaxHeight(300);


        table.getColumns().addAll(nombre_columna, apellido_column,departamento_column,status_actividad);
        table.setItems(this.data);
        populate_data();
    }

    private void populate_data() {

        SharedStates sharedStates = getSharedStates();
        for (String departamento: sharedStates.getDepartamentos_names()) {
            for (Empleado empleado:sharedStates.getEmpleadosArray(departamento)) {
                data.add(empleado);
            }

        }
    }
}
