package org.example.avanceproyecto.LinkedList;

import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Utils;
import org.example.avanceproyecto.Controllers.SharedStates;
import org.example.avanceproyecto.Controllers.TaskDoer;
import org.example.avanceproyecto.Tarea.TareaNodo;

import java.util.ArrayList;

//Cola: ES COMO UNA FILA AL BANCO, EL PRIMERO EN LLEGAR ES EL PRIMERO QUE ES ATENDIDO
// Y el utlimo de la cola es el ultimo en ser atendido
@Getter @Setter
public class Cola implements LinkedlistFuncs {
    TareaNodo first;
    TareaNodo last;
    TaskDoer taskDoer;

    public Cola(ArrayList<Observer> observers,SharedStates sharedStates) {
        taskDoer =  Utils.createTaskDoer(this,observers,sharedStates);
    }


    //quiter
    public void dequeue() {
        if (first == null) return;
        first = first.getNext_node();
        if (first == null) {
            last = null;  // cola vacía
        }
    }

    //agregar
    public void enqueue(TareaNodo tareaNodo) {
        if (first == null) {
            first = tareaNodo;
            last = tareaNodo;  // primer nodo también es el último
        } else {
            last.setNext_node(tareaNodo);
            last = tareaNodo;
        }
    }
    public TareaNodo peek() {
       return first;
    }


    public ArrayList<TareaNodo> getallNodes() {
        ArrayList<TareaNodo> tareaNodoArrayList = new ArrayList<>();

        TareaNodo pointer = first;

        while (pointer != null) {
            tareaNodoArrayList.add(pointer);
            pointer = pointer.getNext_node();
        }
        return tareaNodoArrayList;
    }


    @Override
    public TareaNodo getTaskToBeDone() {
        return peek();
    }

    @Override
    public void removeLastlyDoneTask() {
        dequeue();
    }
}
