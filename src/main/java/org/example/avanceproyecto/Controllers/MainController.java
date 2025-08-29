package org.example.avanceproyecto.Controllers;

import javafx.fxml.FXML;
import javafx.scene.Parent;
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

    }

    @FXML
    private void displayAgregarTarea() throws IOException {
        Utils.callObserver_show_layout(getObservers(),getBorderpane_main(),AgregarTarea.class);
    }
    @FXML
    private void displayVerTarea() {
        Utils.callObserver_show_layout(getObservers(),getBorderpane_main(),VerTareas.class);
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

}
