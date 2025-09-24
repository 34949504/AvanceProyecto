package org.example.avanceproyecto.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    @FXML private TextField buscar_filtro;
    @FXML private ChoiceBox<String> filtro_departamentos;


    JSONObject departamentos_id;
    private ObservableList<Empleado> data = FXCollections.observableArrayList();
    private ObservableList<Empleado> data_filtered = FXCollections.observableArrayList();


    public VerEmpleados(String fxml) {
        initilize_fxml(fxml);
    }

    @Override
    public void init() {

        createTable();
        listener_buscarTextfield();
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

        TableColumn<Empleado, String> nombre_columna = Utils.createColumn("Empleado","empleadoName",150);
        TableColumn<Empleado, String> apellido_column = Utils.createColumn("Apellido","empleadoLastName",200);;


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
        status_actividad.setCellFactory(column -> {
            return new TableCell<Empleado, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty || item == null) {
                        setText(null);
                        setStyle("");
                    } else {
                        setText(item);

                        // Change color based on value
                        if ("Activo".equals(item)) {
                            setStyle("-fx-background-color: #90EE90; -fx-text-fill: black;"); // Light green
                        } else {
                            setStyle("-fx-background-color: #f5e642; -fx-text-fill: black;"); // Light pink
                        }
                    }
                }
            };
        });


        apellido_column.setPrefWidth(200);
        departamento_column.setPrefWidth(300);

        table.setPrefWidth(800);
        table.setMaxWidth(800);
        table.setMaxHeight(300);


        table.getColumns().addAll(nombre_columna, apellido_column,departamento_column,status_actividad);
        table.setItems(this.data);
        populate_data();
    }

    /**
    Agrega los datos de los empleados al observablelist data
     */
    private void populate_data() {

        SharedStates sharedStates = getSharedStates();
        for (String departamento: sharedStates.getDepartamentos_names()) {
            for (Empleado empleado:sharedStates.getEmpleadosArray(departamento)) {
                data.add(empleado);
            }

        }
    }
    private void listener_buscarTextfield() {
        buscar_filtro.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String old, String new_str) {
                if (new_str.length() == 0) {
                    table.setItems(data);
                } else {
                    update_filtered_data(new_str);
                    table.setItems(data_filtered);
                }
            }
        });
        buscar_filtro.setOnAction(actionEvent -> {
            System.out.println("table focus");
            table.requestFocus();
        });
    }

    /**
     * Es llamada  por taskDoer, solamente se tiene que refreshear, porque alla se actualiza el status del empleado
     */
    @Override
    public void tareaTerminada(TareaNodo tareaNodo) {
        table.refresh();
    }

    private void update_filtered_data(String buscar_text) {

        data_filtered.clear();
        for (Empleado empleado:data) {
            ArrayList<String> arrayList = empleado.get_attributes_arrayString();
            boolean subset_in_array = string_subset_in_array(arrayList,buscar_text);

            if (subset_in_array) {
                data_filtered.add(empleado);
            }
        }

    }
    private boolean string_subset_in_array(ArrayList<String> arrayList,String subset) {
        int len_subset = subset.length();
        subset = subset.toLowerCase();
//        System.out.println(subset);
        for (String string:arrayList) {
            string = string.toLowerCase();
//            System.out.printf("%s %d %s\n",string,string.length(),subset);
           if (string.length() < len_subset) {
               continue;
           }
           String sub = string.substring(0,len_subset);
//            System.out.printf("%s sub:%s %b\n",string,sub);
           if (sub.compareTo(subset) == 0) {
               System.out.println("Same subset");
               return true;
           }

        }
        return false;
    }
}
