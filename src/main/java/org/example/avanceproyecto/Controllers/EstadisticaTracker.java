package org.example.avanceproyecto.Controllers;

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


    public EstadisticaTracker(String fxml) {
        initilize_fxml(fxml);
    }

    @Override
    public void init() {

    }

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
        Utils.writeJson(estadistica_json.toString(4), "/home/gerardo/programming/school/Estructura_de_datos/AvanceProyecto/data/estadisticas.json");
    }

    private JSONObject getJSON(JSONObject jsonObject,String key) {

        try {
            JSONObject other_json = jsonObject.getJSONObject(key);
            return other_json;
        } catch (Exception e) {
            return null;
        }
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

    private Integer getActiviadDateCounter(JSONObject date_json){
        try {
            Integer counter = date_json.getInt("autoincrement_counter");
            return counter;
        } catch (JSONException e) {
            date_json.put("autoincrement_counter",0);
            return  0;
        }

    }

    @Override
    public void pre_close_request() {
        System.out.println("writing file");
    }
}
