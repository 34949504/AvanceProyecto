/*
Es un Thread que se inicializa en las clases COlas y pilas, que checa cada segundo si una nueva tarea
ha sido agregada.
Simula la realizacion de tareas con Thread.Sleep(),
Esta ajustado para la lista de pilas ya si se esta realizando una tarea, y agregar otra tarea, se detiene la tarea vieja y resume la
nueva tarea
 */

package org.example.avanceproyecto.Controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.Empleado;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.LinkedList.LinkedlistFuncs;
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.example.avanceproyecto.Tarea.TipoTarea;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter
@Setter
public class TaskDoer extends Task<Void> {
    LinkedlistFuncs linkedlistFuncs;
    ArrayList<Observer> observers;
    SharedStates sharedStates;
    AtomicBoolean doingTask = new AtomicBoolean(false);

    AtomicBoolean isLastIteration = new AtomicBoolean(false);
    boolean DEBUGG = true;

    public TaskDoer(LinkedlistFuncs linkedlistFuncs, ArrayList<Observer> observers, SharedStates sharedStates) {
        this.linkedlistFuncs = linkedlistFuncs;
        this.observers = observers;
        this.sharedStates = sharedStates;
    }

    @Override
    protected Void call() throws Exception {
        while (true) {
            TareaNodo tareaNodo = linkedlistFuncs.getTaskToBeDone();

            if (!sharedStates.getThread_active().get()) {
                Thread.sleep(1000);
                continue;
            }
            if (tareaNodo == null) {
                Thread.sleep(1000);
                continue;
            }

            if (doingTask.get()) {
                Thread.sleep(1000);
                continue;
            }


            doingTask.set(true);

            boolean nottipoTareisNULL = !(tareaNodo.getTipoTarea() == null);
            if (nottipoTareisNULL && tareaNodo.getTipoTarea() == TipoTarea.No_Urgente) {
                tarea_no_urgente();
            } else if (nottipoTareisNULL && tareaNodo.getTipoTarea() == TipoTarea.Urgente) {
                tarea_urgente();

            } else {
                System.out.println("haciendi prioridad");
                tarea_no_urgente();
                tarea_prioridad();
            }
        }
    }

    private boolean activeThread() throws InterruptedException {

        if (!sharedStates.getThread_active().get()) {
            Thread.sleep(1000);
            return false;
        }
        return true;
    }


    private void tarea_no_urgente() throws InterruptedException {

        TareaNodo tareaNodo = linkedlistFuncs.getTaskToBeDone();
        int segundos = tareaNodo.getSegundos();

        for (int i = 0; i < segundos; i++) {

            Thread.sleep(sharedStates.getSpeed().get());
            if (!sharedStates.getThread_active().get()) {
                while (!sharedStates.getThread_active().get()) {
                    System.out.println("Sleeping in tarea");
                    Thread.sleep(1000);
                }
            }


            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    for (Observer observer : observers) {
                        if (observer instanceof VerTareas verTareas) {

                            int remainingSeconds = tareaNodo.decrementRemainingSeconds();
                            verTareas.updateSecondsInTable(remainingSeconds);

                            // Remove task when countdown reaches 0
                            if (remainingSeconds == 0) {
                                linkedlistFuncs.removeLastlyDoneTask();
                                verTareas.tareaTerminada(tareaNodo);
                                isLastIteration.set(true);
                                tareaNodo.getEmpleadoAsignado().setActividadStatus(Empleado.ActividadStatus.No_activo);
                            }
                            break;
                        }
                    }
                }
            });

            if (isLastIteration.get()) {
                // Give Platform.runLater time to execute before allowing next task
                Thread.sleep(500); // Increased delay to ensure UI cleanup completes
                doingTask.set(false);
            }
        }

        while (true) {
            if (isLastIteration.get()) {
                Thread.sleep(500); // Increased delay to ensure UI cleanup completes
                doingTask.set(false);
                isLastIteration.set(false);
                break;
            }
        }
    }

    private void tarea_prioridad() {

    }

    private void tarea_urgente() {
        // Check every second if there's a new urgent task on top of stack
        TareaNodo tareaNodo = linkedlistFuncs.getTaskToBeDone();
        int segundos = tareaNodo.getSegundos();

        while (tareaNodo != null) {

            TareaNodo currentTopTask = linkedlistFuncs.getTaskToBeDone();

            if (currentTopTask == null) {
                // No more tasks
                break;
            }

            // If a different task is now on top, switch to it
            if (!currentTopTask.equals(tareaNodo)) {
                tareaNodo = currentTopTask;
                segundos = tareaNodo.getSegundos();

                // IMMEDIATELY update UI with new task's remaining time
                final int newTaskRemaining = tareaNodo.getRemainingSeconds();
                final TareaNodo newTask = tareaNodo; // Capture for lambda
                Platform.runLater(() -> {
                    for (Observer observer : observers) {
                        observer.updateSecondsInTable(newTaskRemaining);
                        observer.tareaTerminada(newTask);
//                            if (observer instanceof VerTareas verTareas) {
//                                    verTareas.updateSecondsInTable(newTaskRemaining);
//                                    verTareas.tareaTerminada(newTask);
//                                    break;
//                                }
                    }
                });

                continue; // Skip rest of iteration, start fresh with new task
            }

            // Check if paused
            if (!sharedStates.getThread_active().get()) {
                while (!sharedStates.getThread_active().get()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                continue; // Recheck for new tasks after unpausing
            }

            try {
                Thread.sleep(sharedStates.getSpeed().get());
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            // Decrement remaining time
            tareaNodo.decrementRemainingSeconds();
            final int remainingSeconds = tareaNodo.getRemainingSeconds();
            final boolean isCompleted = (remainingSeconds == 0);
            final TareaNodo currentTask = tareaNodo; // Capture for lambda
            final Empleado empleado = tareaNodo.getEmpleadoAsignado();

            Platform.runLater(() -> {
                for (Observer observer : observers) {

//                    observer.updateSecondsInTable(remainingSeconds);

                    if (observer instanceof VerTareas verTareas) {
                        verTareas.updateSecondsInTable(remainingSeconds);
                    observer.updateSecondsInTable(remainingSeconds);
                    if (isCompleted) {
                        linkedlistFuncs.removeLastlyDoneTask();
                        observer.tareaTerminada(currentTask);
                        empleado.setActividadStatus(Empleado.ActividadStatus.No_activo);
                    }
                    }
                }
            });

            if (isCompleted) {
                try {
                    Thread.sleep(2000); // Let UI update
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                tareaNodo = linkedlistFuncs.getTaskToBeDone(); // Get next task
            }

        }
        doingTask.set(false);
    }

    private void debugging(String message) {
        if (DEBUGG) {
            System.out.println(message);
        }
    }
}
