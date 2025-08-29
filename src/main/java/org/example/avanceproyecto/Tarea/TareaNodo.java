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

    TareaNodo previous_node = null;
    TareaNodo next_node = null;

    public TareaNodo(String departamento,String nombreTarea,int milisegundos,TipoTarea tipoTarea) {
        this.departamento = departamento;
        this.nombreTarea = nombreTarea;
        this.segundos = milisegundos;
        this.tipoTarea = tipoTarea;
        this.remainingSeconds = segundos;
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

    public void decrementRemainingSeconds() {
        if (remainingSeconds > 0) {
            remainingSeconds--;
        }
    }
}
