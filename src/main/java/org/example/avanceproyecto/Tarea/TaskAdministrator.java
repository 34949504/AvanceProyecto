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
import org.example.avanceproyecto.LinkedList.PrioridadNode;

import java.util.ArrayList;

public class TaskAdministrator {

    private ArrayList<Observer> observers;
    private SharedStates sharedStates;

    private Pila pila ;
    private Cola cola ;
    private Lista lista;
    private PrioridadNode prioridadNode;

    public TaskAdministrator(ArrayList<Observer> observers, SharedStates sharedStates) {

        this.observers = observers;
        this.sharedStates = sharedStates;
        pila = new Pila(observers,sharedStates);
         cola = new Cola(observers,sharedStates);
         lista = new Lista(observers);
         prioridadNode = new PrioridadNode(observers,sharedStates);



    }

    public void add_task(TareaNodo nodo) {
        TipoTarea tipoTarea = nodo.getTipoTarea();
        Prioridad prioridad = nodo.getPrioridad();

        System.out.println("prioridad es "+prioridad);
        if (prioridad != Prioridad.none){
            System.out.println("aqui vamos xd");
            prioridadNode.push(nodo);
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

    public ArrayList<TareaNodo> get_arraylist_tarea_nodo(TipoTarea tipoTarea,Prioridad prioridad) {

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
        else if (prioridad != Prioridad.none) {
            return prioridadNode.getAllNodes();
        }

        return null;
    }

}
