package org.example.avanceproyecto.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;
import jdk.jshell.execution.Util;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.BaseController;
import org.example.avanceproyecto.ControllerUtils.Empleado;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Utils;
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/*
Estan las keys de los departamentos y ahi se ponen adentro las keys de las tareas
Se va a guardar en las keys de las tareas  un json con la key de la fecha de ho yyyy-mm-dd y adentro va a haber keys
con el
 */
@Getter @Setter
public class EstadisticaTracker extends BaseController implements Observer {

    JSONObject estadistica_json;
    JSONObject empleados_tracking_data_json;

    @FXML
    private TableView<Empleado> table = new TableView<>();
    ObservableList<Empleado> data = FXCollections.observableArrayList();

    @FXML
    private ChoiceBox<String> departamentos_choicebox = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> tarea_choicebox = new ChoiceBox<>();

    @FXML
    private Button empleado_ejemplar_button = new Button();

    @FXML TableColumn<Empleado,Empleado> empleado_column;
    @FXML TableColumn<Empleado,Empleado> departamento_column;
    @FXML TableColumn<Empleado,Integer> tareas_column;





    @FXML
    private Button total_tareas_button = new Button();



    public EstadisticaTracker(String fxml) {
        initilize_fxml(fxml);
        initialize_columns();
    }
    private void initialize_columns() {

        // Empleado column: display full name
        empleado_column.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue())
        );
        empleado_column.setCellFactory(col -> new TableCell<Empleado, Empleado>() {
            @Override
            protected void updateItem(Empleado empleado, boolean empty) {
                super.updateItem(empleado, empty);
                setText(empty || empleado == null ? "" : empleado.getFullName());
            }
        });

        // Departamento column: display departamento name from Empleado
        departamento_column.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue())
        );
        departamento_column.setCellFactory(col -> new TableCell<Empleado, Empleado>() {
            @Override
            protected void updateItem(Empleado empleado, boolean empty) {
                super.updateItem(empleado, empty);
                setText(empty || empleado == null ? "" : empleado.getDepartamentoNombre());
            }
        });

        // Tareas column: display tareas_realizadas from Empleado's estadistica
        tareas_column.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleObjectProperty<>(cellData.getValue().getEstadistica().getTareas_realizadas())
        );
        tareas_column.setCellFactory(col -> new TableCell<Empleado, Integer>() {
            @Override
            protected void updateItem(Integer tareas, boolean empty) {
                super.updateItem(tareas, empty);
                setText(empty || tareas == null ? "" : tareas.toString());
            }
        });
    }



    @Override
    public void init() {
        table.setItems(data);

        table.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        table.setStyle("-fx-control-inner-background: white; "
                + "-fx-table-cell-border-color: transparent; "
                + "-fx-text-background-color: black;");

        fill_data();
        actions_empleado_ejemplar();
    }

    private void actions_empleado_ejemplar() {
        empleado_ejemplar_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

            }
        });
    }


    @Override
    public void tareaTerminada(TareaNodo tareaNodo) {
        System.out.println("tareafue terinada aqui en tarea terminada");
        Empleado empleado = tareaNodo.getEmpleadoAsignado();
        int count =empleado.getEstadistica().increment_tareas_realizadas();
        empleados_tracking_data_json.put(empleado.getEmpleado_id(),count);
        Utils.writeJson(empleados_tracking_data_json.toString(4),"data","empleados_data_tracking.json");
    }

    /**
     * Actualiza el json de estadisticas cuando una nueva tarea es agregada
     * La estructura del json es la siguiente al agregar record
     * Departatemto -> tarea -> fecha_hoy (array) -> jsonblock(empleados_segundos)
     * @param tareaNodo
     */

    @Override
    public void tarea_creada(TareaNodo tareaNodo) {
        System.out.println("tarea creada?");
//        Empleado empleado = tareaNodo.getEmpleadoAsignado();
//        int count =empleado.getEstadistica().increment_tareas_realizadas();
//        empleados_tracking_data_json.put(empleado.getEmpleado_id(),count);

//        System.out.println("THIW WAS CALLED");
//        String departamento = tareaNodo.getDepartamento().toLowerCase();
//        String tarea = tareaNodo.getNombreTarea();
//
//        System.out.println(String.format("departamento %s",departamento));
//        JSONObject departamento_json = estadistica_json.getJSONObject(departamento);
//        JSONObject tarea_json = departamento_json.getJSONObject(tarea);
//        String current_date = Utils.getTodaysDate();
//
//        //Instead, a date json is going to store the data activity done , with a json that has empleados,time.
//        JSONArray data_array = getDateArray(tarea_json,current_date); //Gets current date of json of tareas or creates
//
//        JSONObject object_empleado_time = new JSONObject();
//        String empleado_id = tareaNodo.getEmpleadoAsignado().getEmpleado_id();
//        object_empleado_time.put(empleado_id,Empleado.toJson(tareaNodo.getEmpleadoAsignado()));
//        object_empleado_time.put("segundos",tareaNodo.getSegundos());
//
//        data_array.put(object_empleado_time);
//        System.out.println("tarea creada");
//
////        System.out.println(estadistica_json.toString(4));
//        Utils.writeJson(estadistica_json.toString(4), "data","estadisticas.json");
    }

    /*
    Busca el json date, si no existe se crea y se regresa
     */
    private JSONArray getDateArray(JSONObject tareaJson, String date) {
        try {
            JSONArray date_array = tareaJson.getJSONArray(date);
            return date_array;

        } catch (JSONException e) {
            JSONArray date_json = new JSONArray();
            tareaJson.put(date,date_json);
            return date_json;
        }
    }

    @Override
    public void pre_close_request() {

        System.out.println("writing file");
    }


    private void fill_data() {
        SharedStates s = getSharedStates();

        for (String departamento:s.getDepartamentos_names() ) {

            for (Empleado e: s.getEmpleadosArray(departamento)) {
                if (e == null) {
                    System.out.println("empleado null?");
                }

                System.out.println(e.getFullName());

                JSONObject empleado_data = empleados_tracking_data_json.getJSONObject(e.getEmpleado_id());
                Integer tareas_realizadas = empleado_data.getInt("tareas_realizadas");
                e.getEstadistica().setTareas_realizadas(tareas_realizadas);
                data.add(e);
            }

        }

    }





    /**
     * Ocupare diferentes layouts?
     * Recibir historial (tabla) de una tarea (fecha,empleado, segundos)
     * Recibir que empleado (popup) ha realizado mas tareas por departamento
     * Recibir el promedio de tiempo de una tarea
     * Recibir (tabla) de tareas realiazas (empleado,total)
     *
     * Si en una tarea no hay registros, mostrar  null o 0  idk
     *
     *choicebox departamento = Historial del departamento de todas las tareas (se agrega una nueva columna)
     * choicebox departamento + tarea = se quita una columna y se muestra historial de la tarea
     * choicebox departamento + total de tareas por empleado =  mostrar una tabla que (empleado, total)
     * popup con choicebox que te permite seleccionar departamento y tarea y para caluclar el promedio de segundos
     *
     */
    private class EstadisticaCalculator {


    }



    @Getter @Setter
    public class EmpleadoEstadistica {
        Empleado empleado;
        Integer tareas_completadas;
    }

}

