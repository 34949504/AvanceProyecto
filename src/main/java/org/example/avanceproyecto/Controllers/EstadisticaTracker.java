package org.example.avanceproyecto.Controllers;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.BaseController;
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

    @FXML
    private TableView<String> table = new TableView<>();

    @FXML
    private ChoiceBox<String> departamentos_choicebox = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> tarea_choicebox = new ChoiceBox<>();

    @FXML
    private Button empleado_ejemplar_button = new Button();

    @FXML
    private Button total_tareas_button = new Button();



    public EstadisticaTracker(String fxml) {
        initilize_fxml(fxml);
    }


    @Override
    public void init() {

        table.setStyle("-fx-background-color: white; -fx-text-fill: black;");
        // Optional: Style the table cells specifically
        table.setRowFactory(tv -> {
            TableRow<String> row = new TableRow<>();
            row.setStyle("-fx-background-color: white; -fx-text-fill: black;");
            return row;
        });
        actions_empleado_ejemplar();

        Timeline timeline = new Timeline(new KeyFrame(
                Duration.seconds(1), // Delay duration
                event -> Platform.runLater(() -> {
                tinkerTable();
                })
        ));
        timeline.play();
    }

    private void actions_empleado_ejemplar() {
        empleado_ejemplar_button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.out.println(table.getWidth());
                tinkerTable();
            }
        });
    }


    /**
     * Actualiza el json de estadisticas cuando una nueva tarea es agregada
     * La estructura del json es la siguiente al agregar record
     * Departatemto -> tarea -> fecha_hoy (array) -> jsonblock(empleados_segundos)
     * @param tareaNodo
     */
    @Override
    public void tarea_creada(TareaNodo tareaNodo) {
        System.out.println("THIW WAS CALLED");
        String departamento = tareaNodo.getDepartamento().toLowerCase();
        String tarea = tareaNodo.getNombreTarea();

        System.out.println(String.format("departamento %s",departamento));
        JSONObject departamento_json = estadistica_json.getJSONObject(departamento);
        JSONObject tarea_json = departamento_json.getJSONObject(tarea);
        String current_date = Utils.getTodaysDate();

        //Instead, a date json is going to store the data activity done , with a json that has empleados,time.
        JSONArray data_array = getDateArray(tarea_json,current_date); //Gets current date of json of tareas or creates

        JSONObject object_empleado_time = new JSONObject();
        object_empleado_time.put("empleado",tareaNodo.getEmpleadoAsignado().getFullName());
        object_empleado_time.put("segundos",tareaNodo.getSegundos());

        data_array.put(object_empleado_time);
        System.out.println("tarea creada");

//        System.out.println(estadistica_json.toString(4));
        Utils.writeJson(estadistica_json.toString(4), "data","estadisticas.json");
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

    private void tinkerTable() {
        ObservableList<TableColumn<String,?>> tableColumnObservableList = table.getColumns();
         TableColumn tableColumn = ((TableColumn)tableColumnObservableList.getFirst());
         double width = tableColumn.getWidth();
         double table_width = table.getWidth();
        System.out.println("table width is "+table_width);
        double new_width = table.getWidth() - width;

        tableColumnObservableList.removeFirst();
        System.out.println("new width is " +new_width);
        table.setPrefWidth(new_width);
        table.setMaxWidth(new_width);
        table.refresh();
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
}
