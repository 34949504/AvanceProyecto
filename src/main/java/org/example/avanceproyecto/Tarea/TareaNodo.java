/*
Es la estructura de datos que se utiliza para agregar tareas
RemainingSeconds se utiliza para calcular los segundos restantes de la tarea
previous and next node se utilizan en pilas o colas

 */


package org.example.avanceproyecto.Tarea;

import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.Empleado;
import org.example.avanceproyecto.ControllerUtils.Prioridad;

/**
 * Objeto que represta una tarea
 */
@Getter @Setter
public class TareaNodo {
    String departamento;
    String nombreTarea;
    int segundos;
    int remainingSeconds;
    TipoTarea tipoTarea;
    Empleado empleadoAsignado;
    Prioridad prioridad = Prioridad.none;

    TareaNodo previous_node = null;
    TareaNodo next_node = null;

    public TareaNodo(String departamento,String nombreTarea,int milisegundos,TipoTarea tipoTarea,Empleado empleado_asignado,Prioridad prioridad) {
        this.departamento = departamento;
        this.nombreTarea = nombreTarea;
        this.segundos = milisegundos;
        this.tipoTarea = tipoTarea;
        this.remainingSeconds = segundos;
        this.empleadoAsignado = empleado_asignado;
        this.prioridad = prioridad;
    }
    public  TareaNodo(){

    }

    public String getValues(){

        return String.format(
                "Departamento %s\n" +
                        "Tarea %s\n" +
                        "Segundos %d\n" +
                        "Tipo tarea %s\n",
                departamento, nombreTarea, segundos, tipoTarea
        );
    }

    public int decrementRemainingSeconds() {
        if (remainingSeconds > 0) {
            remainingSeconds--;
        }
        return  remainingSeconds;
    }

    public static TareaNodo getNodeFromOtherNodeValues(TareaNodo nodo_values) {
        String departamento = nodo_values.getDepartamento();
        String nombreTarea = nodo_values.getNombreTarea();
        int segundos = nodo_values.getSegundos();
        int remainingSeconds = nodo_values.getRemainingSeconds();
        TipoTarea tipoTarea = nodo_values.getTipoTarea();
        Empleado empleado_asignado = nodo_values.getEmpleadoAsignado();
        Prioridad prioridad = nodo_values.getPrioridad();
        TareaNodo new_node = new TareaNodo(departamento,nombreTarea,segundos,tipoTarea,empleado_asignado,prioridad);
        return new_node;
    }
}
