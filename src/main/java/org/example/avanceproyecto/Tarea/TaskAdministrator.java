package org.example.avanceproyecto.Tarea;

import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.LinkedList.Cola;
import org.example.avanceproyecto.LinkedList.Lista;
import org.example.avanceproyecto.LinkedList.Pila;

import java.util.ArrayList;

public class TaskAdministrator {

    private ArrayList<Observer> observers;


    private Pila pila ;
    private Cola cola ;
    private Lista lista;

    public TaskAdministrator(ArrayList<Observer> observers) {
        this.observers = observers;
        pila = new Pila(observers);
         cola = new Cola(observers);
         lista = new Lista(observers);

    }



    public void add_task(String departamento,String tarea,TipoTarea tipoTarea,int milisegundos) {
        TareaNodo tareaNodo = new TareaNodo(departamento,tarea,milisegundos,tipoTarea);

        if (tipoTarea == TipoTarea.Urgente) {
            pila.push(tareaNodo);
        }
        else if (tipoTarea == TipoTarea.No_Urgente) {
            cola.enqueue(tareaNodo);
        }
        else if (tipoTarea == TipoTarea.Lista) {
            lista.insert(tareaNodo);
        }
    }

    public ArrayList<TareaNodo> get_arraylist_tarea_nodo(TipoTarea tipoTarea) {

        System.out.println(tipoTarea);
        if (tipoTarea == TipoTarea.Urgente) {
            return pila.get_all_nodes();
        }
        else if (tipoTarea == TipoTarea.No_Urgente) {
            return cola.getallNodes();
        }
        else if (tipoTarea == TipoTarea.Lista) {
            return lista.getAllNodes();
        }

        return null;
    }

}
