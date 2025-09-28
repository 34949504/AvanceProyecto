package org.example.avanceproyecto.Controllers.Proyecto.objects;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@Getter
@Setter
public class ProyectoObject {

    String proycto_name;
    String descripcion;
    String fecha_de_entrega;
    ArrayList<TareaObject> tareas_proyecto = new ArrayList<>();

    public static ProyectoObject fromJson(JSONObject jsonObject) {
        ProyectoObject proyecto = new ProyectoObject();

        proyecto.setProycto_name(jsonObject.getString("proyecto_nombre"));
        proyecto.setDescripcion(jsonObject.getString("descripcion"));
        proyecto.setFecha_de_entrega(jsonObject.getString("fecha_de_entrega"));

        JSONArray tareasArray = jsonObject.getJSONArray("tareas_proyecto");
        for (int i = 0; i < tareasArray.length(); i++) {
            JSONObject tareaJson = tareasArray.getJSONObject(i);

            TareaObject tarea = new TareaObject();
            tarea.setTitle(tareaJson.getString("title"));
            tarea.setDesc(tareaJson.getString("desc"));

            proyecto.getTareas_proyecto().add(tarea);
        }
        return proyecto;
    }
    public static JSONObject createJsonFromProyectoObject(ProyectoObject proyecto) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("proyecto_nombre", proyecto.getProycto_name());
        jsonObject.put("descripcion", proyecto.getDescripcion());
        jsonObject.put("fecha_de_entrega", proyecto.getFecha_de_entrega());

        // Create tareas array
        JSONArray tareasArray = new JSONArray();
        for (TareaObject tarea : proyecto.getTareas_proyecto()) {
            JSONObject tareaJson = new JSONObject();
            tareaJson.put("title", tarea.getTitle());
            tareaJson.put("desc", tarea.getDesc());
            tareasArray.put(tareaJson);
        }

        // Add tareas array to main object
        jsonObject.put("tareas_proyecto", tareasArray);

        return jsonObject;
    }

    public static JSONObject toJson(ProyectoObject proyecto, ArrayList<EmpleadoTarea> empleadoTareas) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("proyecto_nombre", proyecto.getProycto_name());
        jsonObject.put("descripcion", proyecto.getDescripcion());
        jsonObject.put("fecha_de_entrega", proyecto.getFecha_de_entrega());


        JSONArray empleadoTareaArray = new JSONArray();
        jsonObject.put("empleado_tareas",empleadoTareaArray);

        for (EmpleadoTarea empleadoTarea:empleadoTareas) {
            JSONObject empleadoTareaJson = EmpleadoTarea.toJson(empleadoTarea);
            empleadoTareaArray.put(empleadoTareaJson);
        }
        return jsonObject;
    }

    public int getTareasSize() {
        return tareas_proyecto.size();
    }

    @Override
    public String toString() {
        return proycto_name; // This is what will be displayed in the ListView
    }
}
