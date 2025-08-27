package org.example.avanceproyecto.Controllers;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.fxml.FXML;
import org.example.avanceproyecto.ControllerUtils.BaseController;
import org.example.avanceproyecto.ControllerUtils.Observer;

public class VerTareas extends BaseController implements Observer {


    @FXML
    private Button urgentes_button;

    @FXML
    private Button no_urgentes_button;

    @FXML
    private Button lista_button;

    @FXML
    private Label titulo_nombre_tarea_label;

    public VerTareas(String fxmlFile) {
        super(fxmlFile);
    }
}


