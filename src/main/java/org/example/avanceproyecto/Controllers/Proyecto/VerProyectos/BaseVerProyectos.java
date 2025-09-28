package org.example.avanceproyecto.Controllers.Proyecto;

import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.ButtonColorManager;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Utils;
import org.example.avanceproyecto.Controllers.SharedStates;

import javafx.fxml.FXML;
import java.util.ArrayList;

@Getter @Setter
public class BaseVerProyectos {

    @FXML
    private Button proyectos_creados_button;

    @FXML
    private Button proyectos_asignados_button;

    @FXML
    private VBox center;

    Parent layout;
    private SharedStates sharedStates;
    private ProyectoShared prsh;
    private ButtonColorManager buttonColorManager;

    ArrayList<Observer> observers = new ArrayList<>();
    private void addObservers(Observer ... observers) {
        for(Observer observer:observers){
            this.observers.add(observer);
        }
    }

    public BaseVerProyectos(SharedStates sharedStates, ProyectoShared proyectoShared) {
        this.prsh = proyectoShared;
        this.sharedStates = sharedStates;
        this.layout = Utils.load_fxml("/FXML/Proyecto/BaseVerProyectos.fxml", this);


        buttonColorManager = new ButtonColorManager(proyectos_creados_button,proyectos_asignados_button);
        buttonColorManager.change_color_state(proyectos_creados_button);

        setupProyectosAsignadosButton();
        setupProyectosCreadosButton();
    }
    private void setupProyectosCreadosButton() {
        proyectos_creados_button.setOnAction(event -> {
            buttonColorManager.change_color_state(proyectos_creados_button);
        });
    }

    private void setupProyectosAsignadosButton() {
        proyectos_asignados_button.setOnAction(event -> {
            buttonColorManager.change_color_state(proyectos_asignados_button);
        });
    }
}
