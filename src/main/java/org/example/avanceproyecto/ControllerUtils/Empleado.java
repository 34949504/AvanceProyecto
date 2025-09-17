package org.example.avanceproyecto.ControllerUtils;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Empleado {
    String empleadoName;
    String empleadoLastName;
    Integer departamentoId;
    ActividadStatus actividadStatus = ActividadStatus.No_activo;

    public Empleado(String empleadoName, String empleadoLastName, Integer departamento_id) {
        this.empleadoName = empleadoName;
        this.empleadoLastName = empleadoLastName;
        this.departamentoId = departamento_id;
    }

    public String getFullName() {

        return String.format("%s %s",empleadoName,empleadoLastName);
    }

    public enum ActividadStatus {
        Activo,
        No_activo;
    }
}
