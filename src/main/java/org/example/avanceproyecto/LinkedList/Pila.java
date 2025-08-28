package org.example.avanceproyecto.LinkedList;


import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Utils;
import org.example.avanceproyecto.Controllers.SharedStates;
import org.example.avanceproyecto.Controllers.TaskDoer;
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.example.avanceproyecto.Tarea.TipoTarea;

import java.util.ArrayList;

//PILA, COMO SE APILAN LOS PLATOS UNO SOBRE OTRO, Y CUANDO SE AGREGA UN PLATO
//SE AGREGA AL TOPE Y ES EL PRIMERO QUE SALE
@Getter @Setter
public class Pila implements LinkedlistFuncs {
    TareaNodo tareaTop;
    TaskDoer taskDoer;

    public Pila(ArrayList<Observer>observers) {
        taskDoer = Utils.createTaskDoer(this,observers);
    }


    public void push(TareaNodo tareaNodo) {
        if (tareaTop == null) {
           this.tareaTop = tareaNodo;
           return;
        }
        TareaNodo previous = tareaTop;
        tareaTop = tareaNodo;
        tareaTop.setPrevious_node(previous);
    }

    public void pop() {
        if (tareaTop != null) {
            tareaTop = tareaTop.getPrevious_node();
        }
    }
    public TareaNodo peek() {
        return tareaTop;
    }
    public ArrayList<TareaNodo> get_all_nodes() {
        ArrayList<TareaNodo> tareaNodoArrayList = new ArrayList<>();
        TareaNodo pointer = tareaTop;

        while (pointer != null) { // loop until pointer is null
            System.out.println("here in loop");
            tareaNodoArrayList.add(pointer);
            pointer = pointer.getPrevious_node();
        }

        return tareaNodoArrayList;
    }

    @Override
    public TareaNodo getTaskToBeDone() {
        return peek();
    }

    @Override
    public void removeLastlyDoneTask() {
        pop();
    }
}
