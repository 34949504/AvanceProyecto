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
    JSONObject estadisticas_json = Utils.readJson("/estadisticas.json");
    JSONObject empleados_json = Utils.readJson("/empleados.json");
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
        private Empleados empleados = new Empleados("/FXML/Empleados.fxml");
        private SharedStates sharedStates = new SharedStates();
        private EstadisticaTracker estadisticaTracker = new EstadisticaTracker();

        Observer[] controllers = {mainController,agregarTarea,verTareas,empleados};

        Stage stage;
        public ControllerInitializer(Stage stage) {
            this.stage = stage;
            initiliaze_observers();
            setStuff();
            setUpstuff();
            loop_over_observers_and_pass_attributes(controllers);
        }

        private void initiliaze_observers() {

            mainController.addObservers(agregarTarea,verTareas,empleados);
            agregarTarea.addObservers(mainController,verTareas,estadisticaTracker);
            verTareas.addObservers(mainController,agregarTarea);
        }

        private void setStuff() {
            agregarTarea.setTareas_json(tareas_json);
            agregarTarea.setEmpleados_json(empleados_json);
        }

        //IMPORTANT ALL CONTROLLERS FXML NEED THE BORDERPANE
        private void setUpstuff() {
            mainController.share_with_controllers_borderpane();
        }
        private void loop_over_observers_and_pass_attributes(Observer[] observers){
            int length = observers.length;
            for (int i = 0; i <length; i++) {
                Observer observer = observers[i];
                if (observer instanceof BaseController baseController) {
                    System.out.println(observer.getClass());
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