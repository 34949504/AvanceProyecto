package org.example.avanceproyecto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class MainController {

    private Parent agregar_tarea_layout;

    @FXML
    private BorderPane MainPane;


    @FXML
    private void showPila() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Pila.fxml"));
        Parent pilaUI = fxmlLoader.load(); // load the FXML
        MainPane.setCenter(pilaUI);
    }

    @FXML
    private void displayAgregarTarea() throws IOException {
        MainPane.setCenter(agregar_tarea_layout);

    }

    public void setAgregar_tarea_layout(Parent agregar_tarea_layout) {
        this.agregar_tarea_layout = agregar_tarea_layout;
    }
}
