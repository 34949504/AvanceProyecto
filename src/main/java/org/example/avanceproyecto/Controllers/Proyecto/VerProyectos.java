package org.example.avanceproyecto.Controllers.Proyecto;

import javafx.scene.Parent;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Utils;
import org.example.avanceproyecto.Controllers.Proyecto.objects.EmpleadoTarea;
import org.example.avanceproyecto.Controllers.SharedStates;

import java.util.ArrayList;

public class VerProyectos {

    Parent layout;
    private SharedStates sharedStates;
    private ProyectoShared prsh;

    ArrayList<Observer> observers = new ArrayList<>();
    private void addObservers(Observer ... observers) {
        for(Observer observer:observers){
            this.observers.add(observer);
        }
    }

    public VerProyectos(SharedStates sharedStates, ProyectoShared proyectoShared) {
        this.prsh = proyectoShared;
        this.sharedStates = sharedStates;
        this.layout = Utils.load_fxml("/FXML/Proyecto/AsignarProyecto.fxml", this);
    }
}
