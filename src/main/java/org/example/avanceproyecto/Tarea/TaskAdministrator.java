/*
Clase que resguarda las clases Pila,Cola,Lista
Se encarga de realizar los metodos de la clase Pila,Cola, y LIsta
 */

package org.example.avanceproyecto.Tarea;

import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Prioridad;
import org.example.avanceproyecto.Controllers.SharedStates;
import org.example.avanceproyecto.LinkedList.Cola;
import org.example.avanceproyecto.LinkedList.Lista;
import org.example.avanceproyecto.LinkedList.Pila;

import java.util.ArrayList;

public class TaskAdministrator {

    private ArrayList<Observer> observers;
    private SharedStates sharedStates;

    private Pila pila ;
    private Cola cola ;
    private Lista lista;

    public TaskAdministrator(ArrayList<Observer> observers, SharedStates sharedStates) {

        this.observers = observers;
        this.sharedStates = sharedStates;
        pila = new Pila(observers,sharedStates);
         cola = new Cola(observers,sharedStates);
         lista = new Lista(observers);



    }

    public void add_task(TareaNodo nodo) {
        TipoTarea tipoTarea = nodo.getTipoTarea();
        Prioridad prioridad = nodo.getPrioridad();

        if (prioridad != Prioridad.none){
            System.out.println("priorities");
            return;
        }

        if (tipoTarea == TipoTarea.Urgente) {
            pila.push(nodo);
        }
        else if (tipoTarea == TipoTarea.No_Urgente) {
            cola.enqueue(nodo);
        }
        else if (tipoTarea == TipoTarea.Lista) {
            lista.insert(nodo);
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
