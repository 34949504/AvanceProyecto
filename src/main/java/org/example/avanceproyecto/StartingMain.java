package org.example.avanceproyecto;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.BaseController;
import org.example.avanceproyecto.ControllerUtils.Observer;
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


        ControllerInitializer controllerManager = new ControllerInitializer();
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
    private class ControllerInitializer {
        private MainController mainController = new MainController("/FXML/Main.fxml");
        private AgregarTarea agregarTarea = new AgregarTarea("/FXML/AgregarTarea.fxml");
        private VerTareas verTareas = new VerTareas("/FXML/VerTareas.fxml");

        public ControllerInitializer() {
            initiliaze_observers();
            setStuff();
            setUpstuff();
        }

        private void initiliaze_observers() {

//            addObserver(mainController,agregarTarea,verTareas);

            agregarTarea.addObserver(mainController);
            agregarTarea.addObserver(verTareas);
            mainController.addObserver(agregarTarea);
            mainController.addObserver(verTareas);
            verTareas.addObserver(mainController);
            verTareas.addObserver(agregarTarea);
        }

        private void addObserver(BaseController notifier, Observer... observers) {
            for (Observer observer : observers) {
                notifier.addObserver(observer);
            }
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