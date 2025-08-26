package org.example.avanceproyecto;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter @Setter
public class MainController implements Observer {

    private Parent agregar_tarea_layout;
    private Parent main_layout;
    private Stage stage;

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

    @Override
    public void go_mainLayout() {

        Scene scene = stage.getScene();
        System.out.println("GOingt");
    }
}
