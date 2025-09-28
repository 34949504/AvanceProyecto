package org.example.avanceproyecto.Controllers.Proyecto.objects;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

@Getter @Setter
public class ProyectoObjectCreados extends ProyectoObject {
    private ArrayList<TareaObject> tareas_proyecto = new ArrayList<>();


    public static ProyectoObjectCreados fromJson(JSONObject jsonObject) {
        ProyectoObjectCreados proyecto = new ProyectoObjectCreados();
        proyecto.setBasicFields(jsonObject); // Use inherited method

        // Parse tareas_proyecto array
        JSONArray tareasArray = jsonObject.getJSONArray(KEYS.TAREAS_PROYECTO);
        for (int i = 0; i < tareasArray.length(); i++) {
            JSONObject tareaJson = tareasArray.getJSONObject(i);
            TareaObject tarea = new TareaObject();
            tarea.setTitle(tareaJson.getString(KEYS.TITLE));
            tarea.setDesc(tareaJson.getString(KEYS.DESC));
            proyecto.getTareas_proyecto().add(tarea);
        }
        return proyecto;
    }

    public static JSONObject toJsonCreated(ProyectoObjectCreados proyecto) {
        JSONObject jsonObject = new JSONObject();
        proyecto.putBasicFields(jsonObject); // Use inherited method

        // Create tareas array
        JSONArray tareasArray = new JSONArray();
        for (TareaObject tarea : proyecto.getTareas_proyecto()) {
            JSONObject tareaJson = new JSONObject();
            tareaJson.put(KEYS.TITLE, tarea.getTitle());
            tareaJson.put(KEYS.DESC, tarea.getDesc());
            tareasArray.put(tareaJson);
        }
        jsonObject.put(KEYS.TAREAS_PROYECTO, tareasArray);
        return jsonObject;
    }
    public int getTareasSize() {
        return tareas_proyecto.size();
    }

    @Override
    public String toString() {
        return  this.proycto_name;
    }
}
