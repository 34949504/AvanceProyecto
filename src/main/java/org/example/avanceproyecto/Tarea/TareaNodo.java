package org.example.avanceproyecto.Tarea;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class TareaNodo {
    String departamento;
    String nombreTarea;
    int milisegundos;
    TipoTarea tipoTarea;

    TareaNodo previous_node = null;
    TareaNodo next_node = null;

    public TareaNodo(String departamento,String nombreTarea,int milisegundos,TipoTarea tipoTarea) {
        this.departamento = departamento;
        this.nombreTarea = nombreTarea;
        this.milisegundos = milisegundos;
        this.tipoTarea = tipoTarea;
    }
    public  TareaNodo(){

    }

    public String getValues(){

        return String.format(
                "Departamento %s\n" +
                        "Tarea %s\n" +
                        "Milisegundos %d\n" +
                        "Tipo tarea %s\n",
                departamento, nombreTarea, milisegundos, tipoTarea
        );
    }
}
