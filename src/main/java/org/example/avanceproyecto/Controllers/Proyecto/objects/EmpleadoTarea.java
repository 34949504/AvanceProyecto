package org.example.avanceproyecto.Controllers.Proyecto.objects;

import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.Empleado;
import org.json.JSONObject;

@Getter
@Setter
public class EmpleadoTarea {
    Empleado empleado;
    TareaObject tareaObject;


    public static JSONObject toJson(EmpleadoTarea empleadoTarea) {

        Empleado empleado = empleadoTarea.getEmpleado();
        TareaObject tareaObject = empleadoTarea.getTareaObject();

        JSONObject jsonObject = new JSONObject();

        JSONObject empleado_json = Empleado.toJson(empleado);
        JSONObject tarea_json = TareaObject.toJson(tareaObject);

        jsonObject.put("empleado", empleado_json);
        jsonObject.put("tarea", tarea_json);

        return jsonObject;
    }
}
