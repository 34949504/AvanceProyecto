package org.example.avanceproyecto.Controllers.Proyecto.objects;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

/**
 * Representa las tareas de los proyectos
 */
@Getter
@Setter
public class TareaObject {
    public String title;
    public String desc;


    public static JSONObject toJson(TareaObject tarea) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", tarea.title);
        jsonObject.put("desc", tarea.desc);
        return jsonObject;
    }

    // Convert JSONObject to TareaObject
    public static TareaObject fromJson(JSONObject jsonObject) {
        TareaObject tarea = new TareaObject();
        tarea.title = jsonObject.getString("title");
        tarea.desc = jsonObject.getString("desc");
        return tarea;
    }

}
