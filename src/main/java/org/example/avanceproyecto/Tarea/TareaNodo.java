package org.example.avanceproyecto.Tarea;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TareaNodo {
    String departamento;
    String nombre_tarea;
    int milisegundos;

    TareaNodo previous_node = null;
    TareaNodo next_node = null;

    public TareaNodo(String departamento,String nombre_tarea,int milisegundos) {
        this.departamento = departamento;
        this.nombre_tarea = nombre_tarea;
        this.milisegundos = milisegundos;

    }
}
