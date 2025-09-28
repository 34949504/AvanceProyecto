package org.example.avanceproyecto.Controllers.Proyecto.objects;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

@Getter @Setter
public class ProyectoObjectAsignado extends ProyectoObject {
    private ArrayList<EmpleadoTarea> empleadoTareas = new ArrayList<>();

    public static ProyectoObjectAsignado fromJson(JSONObject jsonObject) {
        ProyectoObjectAsignado proyecto = new ProyectoObjectAsignado();
        proyecto.setBasicFields(jsonObject); // Use inherited method

        // Parse empleado_tareas array
        JSONArray empleadoTareasArray = jsonObject.getJSONArray(KEYS.EMPLEADO_TAREAS);
        for (int i = 0; i < empleadoTareasArray.length(); i++) {
            JSONObject empleadoTareaJson = empleadoTareasArray.getJSONObject(i);
            EmpleadoTarea empleadoTarea = EmpleadoTarea.fromJson(empleadoTareaJson);
            proyecto.getEmpleadoTareas().add(empleadoTarea);
        }
        return proyecto;
    }

    public static JSONObject createJson(ProyectoObjectAsignado proyecto) {
        JSONObject jsonObject = new JSONObject();
        proyecto.putBasicFields(jsonObject); // Use inherited method

        // Create empleado_tareas array
        JSONArray empleadoTareasArray = new JSONArray();
        for (EmpleadoTarea empleadoTarea : proyecto.getEmpleadoTareas()) {
            JSONObject empleadoTareaJson = EmpleadoTarea.toJson(empleadoTarea);
            empleadoTareasArray.put(empleadoTareaJson);
        }
        jsonObject.put(KEYS.EMPLEADO_TAREAS, empleadoTareasArray);
        return jsonObject;
    }

    public static JSONObject createJson(ProyectoObjectCreados proyecto, ArrayList<EmpleadoTarea> empleadoTareas) {
        JSONObject jsonObject = new JSONObject();
        proyecto.putBasicFields(jsonObject); // Use inherited method

        JSONArray empleadoTareasArray = new JSONArray();
        for (EmpleadoTarea empleadoTarea : empleadoTareas) {
            JSONObject empleadoTareaJson = EmpleadoTarea.toJson(empleadoTarea);
            empleadoTareasArray.put(empleadoTareaJson);
        }
        jsonObject.put(KEYS.EMPLEADO_TAREAS, empleadoTareasArray);
        return jsonObject;
    }

    public int getTareasSize() {
        return empleadoTareas.size();
    }

    public ArrayList<TareaObject> getTareasFromEmpleadoTareas() {
        ArrayList<TareaObject> tareas = new ArrayList<>();
        for (EmpleadoTarea empleadoTarea : empleadoTareas) {
            tareas.add(empleadoTarea.getTareaObject());
        }
        return tareas;
    }
}
