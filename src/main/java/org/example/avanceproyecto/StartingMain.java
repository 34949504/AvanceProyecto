/*
Aqui inicia el programa
Inicializo los controladores y sus observadores
 */


package org.example.avanceproyecto;

import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.ControllerUtils.BaseController;
import org.example.avanceproyecto.ControllerUtils.Observer;
import org.example.avanceproyecto.ControllerUtils.Utils;
import org.example.avanceproyecto.Controllers.*;
import org.json.JSONObject;

import java.io.IOException;
public class StartingMain extends Application {
    JSONObject tareas_json = Utils.readJson("/Tareas.json");
    @Override
    public void start(Stage stage) throws IOException {


        Window window = stage.getOwner();
        if (window == null) {
        }

        ControllerInitializer controllerManager = new ControllerInitializer(stage);
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
        private SharedStates sharedStates = new SharedStates();

        Observer[] controllers = {mainController,agregarTarea,verTareas};

        Stage stage;
        public ControllerInitializer(Stage stage) {
            this.stage = stage;
            initiliaze_observers();
            setStuff();
            setUpstuff();
            loop_over_observers_and_pass_attributes(controllers);
        }

        private void initiliaze_observers() {

            mainController.addObservers(agregarTarea,verTareas);
            agregarTarea.addObservers(mainController,verTareas);
            verTareas.addObservers(mainController,agregarTarea);
        }

        private void setStuff() {
            agregarTarea.setTareas_json(tareas_json);
        }
        private void setUpstuff() {
            //To get acess of borderpane_main, you need to be observer of maincontroller
            mainController.share_with_controllers_borderpane();
        }
        private void loop_over_observers_and_pass_attributes(Observer[] observers){
            int length = observers.length;
            for (int i = 0; i <length; i++) {
                Observer observer = observers[i];
                if (observer instanceof BaseController baseController) {
                    baseController.setStage(stage);
                    baseController.setSharedStates(sharedStates);
                }
            }
            for (int i = 0; i <length; i++) {
                Observer observer = observers[i];
                if (observer instanceof BaseController baseController) {
                    observer.init();
                }
            }
        }

    }

}