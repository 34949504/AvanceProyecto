package org.example.avanceproyecto;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.json.JSONObject;

import java.io.IOException;
public class Main extends Application {
    JSONObject tareas_json = Utils.readJson("/Tareas.json");
    @Override
    public void start(Stage stage) throws IOException {

        MainController mainController = new MainController("Main.fxml");
        AgregarTarea agregarTarea = new AgregarTarea("AgregarTarea.fxml");

        agregarTarea.setTareas_json(tareas_json);
        agregarTarea.addObserver(mainController);
        mainController.setStage(stage);



        mainController.setAgregar_tarea_layout(agregarTarea.getLayout()); // ✅ works now






        Rectangle2D rectangle2D = Utils.getScreenDimsHalfed();
        Scene scene = new Scene(mainController.getLayout(), rectangle2D.getWidth(), rectangle2D.getHeight());
        scene.getStylesheets().add(getClass().getResource("/css/buttons.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/css/general.css").toExternalForm());

        stage.setX(rectangle2D.getMinX());
        stage.setY(rectangle2D.getMinY());
        stage.setTitle("Hello!");
        stage.setScene(scene);


        stage.show();
    }

    @SuppressWarnings("unchecked")
    private <T> T getController(FXMLLoader fxmlLoader) {
        return (T) fxmlLoader.getController();
    }
}
