/*
Es la estructura de datos que se utiliza para agregar tareas
RemainingSeconds se utiliza para calcular los segundos restantes de la tarea
previous and next node se utilizan en pilas o colas

 */


package org.example.avanceproyecto.Tarea;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TareaNodo {
    String departamento;
    String nombreTarea;
    int segundos;
    int remainingSeconds;
    TipoTarea tipoTarea;
    String empleado_asignado;

    TareaNodo previous_node = null;
    TareaNodo next_node = null;

    public TareaNodo(String departamento,String nombreTarea,int milisegundos,TipoTarea tipoTarea,String empleado_asignado) {
        this.departamento = departamento;
        this.nombreTarea = nombreTarea;
        this.segundos = milisegundos;
        this.tipoTarea = tipoTarea;
        this.remainingSeconds = segundos;
        this.empleado_asignado = empleado_asignado;
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
        String empleado_asignado = nodo_values.getEmpleado_asignado();
        TareaNodo new_node = new TareaNodo(departamento,nombreTarea,segundos,tipoTarea,empleado_asignado);
        return new_node;
    }
}
