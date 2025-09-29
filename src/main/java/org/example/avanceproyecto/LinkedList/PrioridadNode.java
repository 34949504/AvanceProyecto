package org.example.avanceproyecto.LinkedList;

import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Prioridad;
import org.example.avanceproyecto.ControllerUtils.Utils;
import org.example.avanceproyecto.Controllers.SharedStates;
import org.example.avanceproyecto.Controllers.TaskDoer;
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.example.avanceproyecto.Tarea.TipoTarea;

import java.util.ArrayList;

/**
 * No es node xd, ya habia otra clase con Prioridad
 * Al pedir por una tarea, primero checa por el orden de prioridad
 * Tiene taskDoer
 */
public class PrioridadNode implements LinkedlistFuncs {
    ArrayList<TareaNodo> prioridad_alta_lista = new ArrayList<>();
    ArrayList<TareaNodo> prioridad_media_lista = new ArrayList<>();
    ArrayList<TareaNodo> prioridad_baja_lista = new ArrayList<>();
    Prioridad current_task_priority = Prioridad.none;

    TaskDoer taskDoer;

    public PrioridadNode(ArrayList<Observer> observers, SharedStates sharedStates) {
        taskDoer =  Utils.createTaskDoer(this,observers,sharedStates);
    }

    public void push(TareaNodo tareaNodo){
        if (tareaNodo.getPrioridad() == Prioridad.alta) {
            prioridad_alta_lista.add(tareaNodo);
            return;
        }
        if (tareaNodo.getPrioridad() == Prioridad.media) {
            prioridad_media_lista.add(tareaNodo);
            return;
        }
        if (tareaNodo.getPrioridad() == Prioridad.baja) {
            prioridad_baja_lista.add(tareaNodo);
            return;
        }

    }


    private TareaNodo getTaskByPriority() {
        if (!prioridad_alta_lista.isEmpty()) {
            current_task_priority = Prioridad.alta;
            return  prioridad_alta_lista.getLast();
        }
        else if (!prioridad_media_lista.isEmpty()) {

            current_task_priority = Prioridad.media;
            return  prioridad_media_lista.getLast();
        }
        else if (!prioridad_baja_lista.isEmpty()) {
            current_task_priority = Prioridad.baja;
            return  prioridad_baja_lista.getLast();
        }
        return null;
    }
    @Override
    public TareaNodo getTaskToBeDone() {
        return  getTaskByPriority();
    }
    @Override
    public void removeLastlyDoneTask() {
        if (current_task_priority == Prioridad.alta) {
            prioridad_alta_lista.removeLast();
            return;
        }
        if (current_task_priority == Prioridad.media) {
            prioridad_media_lista.removeLast();
            return;
        }
        if (current_task_priority == Prioridad.baja) {
            prioridad_baja_lista.removeLast();
            return;
        }
    }
    public ArrayList<TareaNodo> getAllNodes() {
        ArrayList<TareaNodo> nodes = new ArrayList<>();
        nodes.addAll(prioridad_alta_lista);
        nodes.addAll(prioridad_media_lista);
        nodes.addAll(prioridad_baja_lista);
        return nodes;
    }



    @Override
    public void notifyTaskDone(TipoTarea tipoTarea) {

    }
}
