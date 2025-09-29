package org.example.avanceproyecto.ControllerUtils;

import javafx.scene.control.Button;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase simple que permite cambiar el color de un boton para mostrar estatus activo
 */
public class ButtonColorManager  {

    ArrayList<Button> buttons = new ArrayList<>();

    public ButtonColorManager(Button ... buttons) {
        this.buttons.addAll(List.of(buttons));
    }

    public void change_color_state(Button button){
        button.setStyle("-fx-background-color:orange");
        for (Button btn:buttons) {
            if (btn == button){
                continue;
            }
            btn.setStyle("-fx-background-color:white");
        }
    }
}