package org.example.avanceproyecto.Controllers;

import javafx.application.Platform;
import javafx.concurrent.Task;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.LinkedList.LinkedlistFuncs;
import org.example.avanceproyecto.Tarea.TareaNodo;
import org.example.avanceproyecto.Tarea.TipoTarea;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

@Getter @Setter
public class TaskDoer extends Task<Void> {
    LinkedlistFuncs linkedlistFuncs;
    ArrayList<Observer> observers;
    SharedStates sharedStates;
    AtomicBoolean doingTask = new AtomicBoolean(false);

    public TaskDoer(LinkedlistFuncs linkedlistFuncs, ArrayList<Observer> observers, SharedStates sharedStates) {
        this.linkedlistFuncs = linkedlistFuncs;
        this.observers = observers;
        this.sharedStates = sharedStates;
    }

    @Override
    protected Void call() throws Exception {
        while (true) {
            if (doingTask.get()) {
                Thread.sleep(1000);
                continue;
            }


            TareaNodo tareaNodo = linkedlistFuncs.getTaskToBeDone();
            if (sharedStates == null) {
                System.out.println("Fuck new york");
            }

            if (!sharedStates.getThread_active().get()) {
                Thread.sleep(1000);
                continue;
            } else {
            }

            if (tareaNodo == null) {
                Thread.sleep(1000);
                continue;
            } else {
            }

            int miliseconds = tareaNodo.getSegundos() * 1000;
            doingTask.set(true);

            String tarea = tareaNodo.getNombreTarea();
            int segundos = tareaNodo.getSegundos();

            if (tareaNodo.getTipoTarea() == TipoTarea.No_Urgente) {


                for (int i = 0; i < segundos; i++) {

                    if (!sharedStates.getThread_active().get()) {
                        while (!sharedStates.getThread_active().get()) {
                            System.out.println("Sleeping in tarea");
                            Thread.sleep(1000);
                        }
                    }

                    Thread.sleep(1000);

                    final int currentIteration = i + 1;
                    final int remainingSeconds = segundos - currentIteration;
                    final boolean isLastIteration = (remainingSeconds == 0);

                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            for (Observer observer : observers) {
                                System.out.println("observer");
                                if (observer instanceof VerTareas verTareas) {
                                    System.out.println("found vertareas");
                                    verTareas.updateSecondsInTable(remainingSeconds);

                                    // Remove task when countdown reaches 0
                                    if (remainingSeconds == 0) {

                                        TareaNodo tareaNodo = linkedlistFuncs.getTaskToBeDone();
                                        System.out.println("Done with tarea " + tareaNodo.getNombreTarea());
                                        linkedlistFuncs.removeLastlyDoneTask();
                                        // Update table after removal
                                        verTareas.updateTable(tareaNodo.getTipoTarea());
                                    }
                                    break;
                                }
                            }
                        }
                    });

                    if (isLastIteration) {
                        // Give Platform.runLater time to execute before allowing next task
                        Thread.sleep(500); // Increased delay to ensure UI cleanup completes
                        doingTask.set(false);
                    }
                }
            }
            //pila
//pila
            else if (tareaNodo.getTipoTarea() == TipoTarea.Urgente) {
                while (tareaNodo != null) {
                    // Check every second if there's a new urgent task on top of stack
                    TareaNodo currentTopTask = linkedlistFuncs.getTaskToBeDone();

                    if (currentTopTask == null) {
                        // No more tasks
                        break;
                    }

                    // If a different task is now on top, switch to it
                    if (!currentTopTask.equals(tareaNodo)) {
                        System.out.println("🔄 Switching from " + tareaNodo.getNombreTarea() +
                                " to new urgent task: " + currentTopTask.getNombreTarea());
                        tareaNodo = currentTopTask;
                        segundos = tareaNodo.getSegundos();

                        // IMMEDIATELY update UI with new task's remaining time
                        final int newTaskRemaining = tareaNodo.getRemainingSeconds();
                        final TareaNodo newTask = tareaNodo; // Capture for lambda
                        Platform.runLater(() -> {
                            for (Observer observer : observers) {
                                if (observer instanceof VerTareas verTareas) {
                                    System.out.println("🎯 Updating UI for new task: " + newTaskRemaining + " seconds");
                                    verTareas.updateSecondsInTable(newTaskRemaining);
                                    verTareas.updateTable(newTask.getTipoTarea());
                                    break;
                                }
                            }
                        });

                        continue; // Skip rest of iteration, start fresh with new task
                    }

                    // Check if paused
                    if (!sharedStates.getThread_active().get()) {
                        while (!sharedStates.getThread_active().get()) {
                            System.out.println("💤 Sleeping in urgent tarea");
                            Thread.sleep(1000);
                        }
                        continue; // Recheck for new tasks after unpausing
                    }

                    System.out.println("⚡ Doing urgent tarea: " + tareaNodo.getNombreTarea() +
                            " (" + tareaNodo.getRemainingSeconds() + "s left)");
                    Thread.sleep(1000);

                    // Decrement remaining time
                    tareaNodo.decrementRemainingSeconds();
                    final int remainingSeconds = tareaNodo.getRemainingSeconds();
                    final boolean isCompleted = (remainingSeconds == 0);
                    final TareaNodo currentTask = tareaNodo; // Capture for lambda

                    Platform.runLater(() -> {
                        for (Observer observer : observers) {
                            if (observer instanceof VerTareas verTareas) {
                                System.out.println("🔢 Updating countdown: " + remainingSeconds);
                                verTareas.updateSecondsInTable(remainingSeconds);

                                if (isCompleted) {
                                    System.out.println("✅ Done with urgent tarea: " + currentTask.getNombreTarea());
                                    linkedlistFuncs.removeLastlyDoneTask();
                                    verTareas.updateTable(currentTask.getTipoTarea());
                                }
                                break;
                            }
                        }
                    });

                    if (isCompleted) {
                        System.out.println("🏁 Task completed, checking for next...");
                        Thread.sleep(500); // Let UI update
                        tareaNodo = linkedlistFuncs.getTaskToBeDone(); // Get next task
                        if (tareaNodo != null) {
                            segundos = tareaNodo.getSegundos();
                            System.out.println("🆕 Next urgent task: " + tareaNodo.getNombreTarea());
                        }
                    }
                }
                doingTask.set(false);
            }
        }
    }
}
