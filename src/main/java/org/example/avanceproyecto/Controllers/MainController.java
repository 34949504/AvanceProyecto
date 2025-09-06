/*
Es el controlador que tiene el layout dondde van los demas layouts fxml
 */

package org.example.avanceproyecto.Controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.BaseController;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Utils;

import java.io.IOException;

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
        color_active_button(agregar_tarea,ver_tarea);
        try {
            displayAgregarTarea();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void displayAgregarTarea() throws IOException {
        Utils.callObserver_show_layout(getObservers(),getBorderpane_main(),AgregarTarea.class);
        color_active_button(agregar_tarea,ver_tarea,empleados);
    }
    @FXML
    private void displayVerTarea() {
        Utils.callObserver_show_layout(getObservers(),getBorderpane_main(),VerTareas.class);
        color_active_button(ver_tarea,agregar_tarea,empleados);
    }
    @FXML
    private void verEmpleados() {
        Utils.callObserver_show_layout(getObservers(),getBorderpane_main(),Empleados.class);
        color_active_button(empleados,agregar_tarea,ver_tarea);
    }


    public void share_with_controllers_borderpane(){
        for(Observer observer:getObservers()) {
            if (observer instanceof BaseController baseController) {
                baseController.setBorderpane_main(getBorderpane_main());
                System.out.println("horray");
            } else {
                System.out.println("Not horray");
            }
        }
    }

    private void color_active_button(Button active_button,Button ... not_active_buttons) {
        String light_yellow = "#ffde85";
        String white = "#ffffff";
        active_button.setStyle(String.format("-fx-background-color: %s;",light_yellow));

        for (Button not_active: not_active_buttons) {
            not_active.setStyle(String.format("-fx-background-color: %s;",white));
        }


    }

}
