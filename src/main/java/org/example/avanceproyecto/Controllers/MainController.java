/*
Es el controlador que tiene el layout dondde van los demas layouts fxml
 */

package org.example.avanceproyecto.Controllers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.BaseController;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Utils;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Es la pantalla principal, siempre se ve, y tiene un borderpane en donde el centro es
 * replazado cuando se presiona un boton para mostrar el layout correspondiete,
 * Se inica con el layout AgregarTarea como default
 */
@Getter @Setter
public class MainController extends BaseController implements Observer {

    private Stage stage;
    private Parent main_layout;
    private Parent origin;


    @FXML
    private Button agregar_tarea;
    @FXML
    private Button ver_tarea;
    @FXML
    private Button empleados;
    @FXML
    private Button estadisticas;
    @FXML
    private Button proyecto;

    ArrayList<Button> buttons_array = new ArrayList<>();


    public MainController(String fxmlFile) {
        initilize_fxml(fxmlFile);
        setBorderpane_main((((BorderPane)getLayout())));
        Parent center = ((Parent) ((BorderPane)getLayout()).getCenter());
        origin = getLayout();
        super.setLayout(center);
        //Change the layout to the center and not the whole borderpane
    }

    @Override
    public void init() {
        init_buttons_array();
        color_active_button(agregar_tarea);
        try {
            displayAgregarTarea();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                if (windowEvent.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
                    for (Observer observer:getObservers()) {
                        observer.pre_close_request();
                    }
                }
            }
        });
    }
    private void init_buttons_array(){
        buttons_array.add(agregar_tarea);
        buttons_array.add(ver_tarea);
        buttons_array.add(empleados);
        buttons_array.add(estadisticas);
        buttons_array.add(proyecto);
    }
    private void color_active_button(Button active) {
        String light_yellow = "#ffde85";
        String white = "#ffffff";
        active.setStyle(String.format("-fx-background-color: %s;",light_yellow));
        for (Button button:buttons_array) {
            if (button != active) {
                button.setStyle(String.format("-fx-background-color: %s;",white));
            }
        }
    }

    @FXML
    private void displayAgregarTarea() throws IOException {
        Utils.callObserver_show_layout(getObservers(),getBorderpane_main(),AgregarTarea.class);
        color_active_button(agregar_tarea);
    }
    @FXML
    private void displayVerTarea() {
        Utils.callObserver_show_layout(getObservers(),getBorderpane_main(),VerTareas.class);
        color_active_button(ver_tarea);
    }
    @FXML
    private void verEmpleados() {
        Utils.callObserver_show_layout(getObservers(),getBorderpane_main(), VerEmpleados.class);
        color_active_button(empleados);
    }
    @FXML
    private void verEstadisticas() {
        Utils.callObserver_show_layout(getObservers(),getBorderpane_main(),EstadisticaTracker.class);
        color_active_button(estadisticas);
    }
    @FXML
    private void verProyectos() {
        Utils.callObserver_show_layout(getObservers(),getBorderpane_main(),Proyectos.class);
        color_active_button(proyecto);
    }

    /*
    No estoy muy seguro de porque tengo que pasar el borderpane, si esta misma clase ya tiene el borderpane y lo puede accesar xd
    SE tiene que checar
     */
    public void share_with_controllers_borderpane(){
        for(Observer observer:getObservers()) {
            if (observer instanceof BaseController baseController) {
                baseController.setBorderpane_main(getBorderpane_main());
            } else {
            }
        }
    }


}
