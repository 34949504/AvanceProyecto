/*
Interfaz que permite a la clase TareaDoer llamar los metodos de PIlas,COlas,
 */
package org.example.avanceproyecto.LinkedList;

import org.example.avanceproyecto.Tarea.TareaNodo;
import org.example.avanceproyecto.Tarea.TipoTarea;

public interface LinkedlistFuncs {
    public TareaNodo getTaskToBeDone();
    public void removeLastlyDoneTask();
    default public void notifyTaskDone(TipoTarea tipoTarea){

    };
}
