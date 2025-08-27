package org.example.avanceproyecto.Tarea;

import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.LinkedList.Pila;

import java.util.ArrayList;

public class TaskAdministrator {

    private Pila pila = new Pila();



    public void add_task(String departamento,String tarea,TipoTarea tipoTarea,int milisegundos) {
        TareaNodo tareaNodo = new TareaNodo(departamento,tarea,milisegundos,tipoTarea);

        if (tipoTarea == TipoTarea.Urgente) {
            pila.push(tareaNodo);
        }
        else if (tipoTarea == TipoTarea.No_Urgente) {

        }
        else if (tipoTarea == TipoTarea.Lista) {

        }
    }

    public ArrayList<TareaNodo> get_arraylist_tarea_nodo(TipoTarea tipoTarea) {

        System.out.println(tipoTarea);
        if (tipoTarea == TipoTarea.Urgente) {
            return pila.get_all_nodes();
        }
        else if (tipoTarea == TipoTarea.No_Urgente) {
            return null;

        }
        else if (tipoTarea == TipoTarea.Lista) {
            return null;
        }

        return null;
    }

}
