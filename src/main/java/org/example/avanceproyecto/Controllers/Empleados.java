package org.example.avanceproyecto.Controllers;

import javafx.fxml.FXML;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.BaseController;
import org.example.avanceproyecto.ControllerUtils.Observer;

@Setter @Getter
public class Empleados extends BaseController implements Observer {


    public Empleados(String fxml) {
        initilize_fxml(fxml);
    }

    @Override
    public void init() {

    }
}
