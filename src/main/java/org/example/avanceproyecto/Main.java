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

//        MainController mainController = BaseController.<MainController>loadController("Main.fxml", MainController.class);
//        AgregarTarea agregarController = BaseController.<AgregarTarea>loadController("AgregarTarea.fxml", AgregarTarea.class);

        Pair<Parent, MainController> main_pair = loadFxml("Main.fxml");
        Pair<Parent, AgregarTarea> agregarTarea_pair = loadFxml("AgregarTarea.fxml");

        MainController mainController = main_pair.getValue();
        AgregarTarea agregarTarea = agregarTarea_pair.getValue();

        agregarTarea.setTareas_json(tareas_json);
        agregarTarea.addObserver(mainController);

        mainController.setStage(stage);
        mainController.setMain_layout(main_pair.getKey());



        mainController.setAgregar_tarea_layout(agregarTarea_pair.getKey()); // ✅ works now






        Rectangle2D rectangle2D = Utils.getScreenDimsHalfed();
        Scene scene = new Scene(main_pair.getKey(), rectangle2D.getWidth(), rectangle2D.getHeight());
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

    public static <T> Pair<Parent, T> loadFxml(String resourcePath) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(resourcePath));
        Parent root = loader.load();
        T controller = loader.getController();
        return new Pair<>(root, controller);
    }
}
