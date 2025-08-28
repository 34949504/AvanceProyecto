package org.example.avanceproyecto.Controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.LinkedList.LinkedlistFuncs;
import org.example.avanceproyecto.Tarea.TareaNodo;

import java.util.ArrayList;

@Getter @Setter
public class TaskDoer extends Task<Void> {
    LinkedlistFuncs linkedlistFuncs;
    ArrayList<Observer> observers;
    SharedStates sharedStates;

    public TaskDoer(LinkedlistFuncs linkedlistFuncs,ArrayList<Observer>observers) {
        this.linkedlistFuncs = linkedlistFuncs;
        this.observers = observers;
    }

    @Override
    protected Void call() throws Exception {
        System.out.println("here in while loop");
        // do something in background
        while (true) {
        TareaNodo tareaNodo = linkedlistFuncs.getTaskToBeDone();
        if (sharedStates.getThread_active().get()) {
            Thread.sleep(1000);
            continue;
        }

        if (tareaNodo == null) {
//            System.out.println("Es null" + "desde clase " + linkedlistFuncs.getClass());
            Thread.sleep(1000);
            continue;
        }

        int miliseconds = tareaNodo.getSegundos() * 1000;
        String tarea = tareaNodo.getNombreTarea();

        System.out.println("Doing tarea "+tarea);
        Thread.sleep(miliseconds);
        System.out.println("Done with tarea "+tarea);
        linkedlistFuncs.removeLastlyDoneTask();

        for (Observer observer:observers) {
            if (observer instanceof VerTareas verTareas) {
                Platform.runLater(() -> {
                    verTareas.updateTable(tareaNodo.getTipoTarea());
                });
                break;
            }
        }

        }
    }
}
