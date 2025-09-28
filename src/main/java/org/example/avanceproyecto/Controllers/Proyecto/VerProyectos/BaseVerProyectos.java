package org.example.avanceproyecto.Controllers.Proyecto.VerProyectos;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.ButtonColorManager;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Utils;
import org.example.avanceproyecto.Controllers.Proyecto.ProyectoShared;
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


    private VerProyectosCreados verProyectos;
    private VerProyectosAsignados verProyectosAsignados;
    ArrayList<Observer> observers = new ArrayList<>();


    public BaseVerProyectos(SharedStates sharedStates, ProyectoShared proyectoShared) {
        this.prsh = proyectoShared;
        this.sharedStates = sharedStates;
        this.layout = Utils.load_fxml("/FXML/Proyecto/BaseVerProyectos.fxml", this);
        this.verProyectos = new VerProyectosCreados(sharedStates,proyectoShared);
        this.verProyectosAsignados = new VerProyectosAsignados(sharedStates,proyectoShared);


        buttonColorManager = new ButtonColorManager(proyectos_creados_button,proyectos_asignados_button);
        buttonColorManager.change_color_state(proyectos_creados_button);
        changeLayout(verProyectos.getLayout());


        setupProyectosAsignadosButton();
        setupProyectosCreadosButton();
    }
   private void setupProyectosCreadosButton() {
        proyectos_creados_button.setOnAction(event -> {
            buttonColorManager.change_color_state(proyectos_creados_button);
            changeLayout(verProyectos.getLayout());
        });
    }

    private void setupProyectosAsignadosButton() {
        proyectos_asignados_button.setOnAction(event -> {
            buttonColorManager.change_color_state(proyectos_asignados_button);
            changeLayout(verProyectosAsignados.getLayout());
        });
    }

    private void changeLayout(Parent layout) {
        ObservableList<Node> nodes = center.getChildren();
        if (nodes.size() >0)
            nodes.removeLast();
        nodes.add(layout);
    }
}
