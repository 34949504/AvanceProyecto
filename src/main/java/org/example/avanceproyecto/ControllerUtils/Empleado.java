package org.example.avanceproyecto.ControllerUtils;

import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;

import java.util.ArrayList;

import static java.lang.Math.abs;

@Getter @Setter
public class Empleado {
    String empleadoName;
    String empleadoLastName;
    Integer departamentoId;
    String departamentoNombre;
    String fecha_nacimiento;
    ActividadStatus actividadStatus = ActividadStatus.No_activo;

    public Integer id;
    public Empleado right = null;
    public Empleado left = null;

    public Empleado(String empleadoName, String empleadoLastName, Integer departamento_id,String fecha_nacimiento) {
        this.empleadoName = empleadoName;
        this.empleadoLastName = empleadoLastName;
        this.departamentoId = departamento_id;
        this.departamentoNombre = Utils.getDepartamentoById(new JSONObject(),this.departamentoId);
        this.fecha_nacimiento = fecha_nacimiento;
        this.id = generateEmpleadoID();
    }

    public String getFullName() {

        return String.format("%s %s",empleadoName,empleadoLastName);
    }

    public enum ActividadStatus {
        Activo,
        No_activo;
    }

    public ArrayList<String> get_attributes_arrayString() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(empleadoName);
        arrayList.add(empleadoLastName);
        arrayList.add(departamentoNombre);
        arrayList.add(get_state_string());
        return  arrayList;

    }
    private String get_state_string() {
        return this.actividadStatus == ActividadStatus.Activo ? "Activo":"No activo";
    }

    private int generateEmpleadoID() {
        Integer tint = abs(getFullName().hashCode() %1000);
        return (tint);

    }

    public static JSONObject toJson(Empleado empleado) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nombre",empleado.getEmpleadoName());
        jsonObject.put("apellidos",empleado.getEmpleadoLastName());
        jsonObject.put("fecha_nacimiento",empleado.getFecha_nacimiento());
        return jsonObject;
    }
}
