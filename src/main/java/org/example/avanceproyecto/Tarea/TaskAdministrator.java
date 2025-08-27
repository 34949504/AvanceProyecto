package org.example.avanceproyecto.Tarea;

import org.example.avanceproyecto.LinkedList.Pila;

public class TaskAdministrator {

    private Pila pila = new Pila();



    public void add_task(String departamento,String tarea,String tipo_tarea) {
        TipoTarea tipoTarea = TipoTarea.get_enum_by_string_comparison(tipo_tarea);
        TareaNodo tareaNodo = new TareaNodo(departamento,tarea);

        if (tipoTarea == TipoTarea.Urgente) {
            pila.push(tareaNodo);
        }
        else if (tipoTarea == TipoTarea.No_Urgente) {

        }
        else if (tipoTarea == TipoTarea.Lista) {

        }
    }

}
