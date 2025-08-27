package org.example.avanceproyecto;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.Utils;
import org.example.avanceproyecto.Controllers.MainController;
import org.example.avanceproyecto.Controllers.AgregarTarea;
import org.example.avanceproyecto.Controllers.VerTareas;
import org.json.JSONObject;

import java.io.IOException;
public class StartingMain extends Application {
    JSONObject tareas_json = Utils.readJson("/Tareas.json");
    @Override
    public void start(Stage stage) throws IOException {


        ControllerManager controllerManager = new ControllerManager();
        Rectangle2D rectangle2D = Utils.getScreenDimsHalfed();
        Scene scene = new Scene(controllerManager.getMainController().getOrigin(), rectangle2D.getWidth(), rectangle2D.getHeight());
        scene.getStylesheets().add(getClass().getResource("/css/buttons.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/css/general.css").toExternalForm());

        stage.setX(rectangle2D.getMinX());
        stage.setY(rectangle2D.getMinY());
        stage.setTitle("Yeah");
        stage.setScene(scene);
        stage.show();
    }

    @Getter @Setter
    private class  ControllerManager {
        private MainController mainController = new MainController("/FXML/Main.fxml");
        private AgregarTarea agregarTarea = new AgregarTarea("/FXML/AgregarTarea.fxml");
        private VerTareas verTareas = new VerTareas("/FXML/VerTareas.fxml");

        public ControllerManager() {
            initiliaze_observers();
            setStuff();
            setUpstuff();
        }

        private void initiliaze_observers() {
            agregarTarea.addObserver(mainController);
            mainController.addObserver(agregarTarea);
            mainController.addObserver(verTareas);
            verTareas.addObserver(mainController);
        }
        private void setStuff() {
            agregarTarea.setTareas_json(tareas_json);
        }
        private void setUpstuff() {
            //To get acess of borderpane_main, you need to be observer of maincontroller
            mainController.share_with_controllers_borderpane();
        }

    }

}