/*
Aqui inicia el programa
Inicializo los controladores y sus observadores
 */


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
import org.example.avanceproyecto.Controllers.*;
import org.json.JSONObject;

import java.io.IOException;
/*
Es el inicio del programa
Resumen:
Se lee los archivos jsons
Se inicializa ControllerInitializer
Se agrega a la escenal los archivos css

 */
public class StartingMain extends Application {
    JSONObject tareas_json ;
    JSONObject estadisticas_json ;
    JSONObject empleados_json ;
    JSONObject departamentos_id ;

    private void readJsons() {
         tareas_json = Utils.readJson_READONLY("/Tareas.json");
         estadisticas_json = new JSONObject(Utils.readFile("data","estadisticas.json"));
         empleados_json = Utils.readJson_READONLY("/empleados.json");
         departamentos_id = Utils.readJson_READONLY("/departamentos_id.json");
    }

    @Override
    public void start(Stage stage) throws IOException {

        readJsons();
        Utils.setDepartamentos_id(departamentos_id); //XD


        ControllerInitializer controllerManager = new ControllerInitializer(stage);
        Scene scene = new Scene(controllerManager.getMainController().getOrigin(),960, 540);
        scene.getStylesheets().add(getClass().getResource("/css/buttons.css").toExternalForm());
        scene.getStylesheets().add(getClass().getResource("/css/general.css").toExternalForm());


        stage.setResizable(false);
        stage.setTitle("App");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Inicializa las clases del programa
     * Se inicializan sus observadores
     * Se les pasa los datos de los jsons
     *
     */
    @Getter @Setter
    private class ControllerInitializer {

        private MainController mainController = new MainController("/FXML/Main.fxml");
        private AgregarTarea agregarTarea = new AgregarTarea("/FXML/AgregarTarea.fxml");
        private VerTareas verTareas = new VerTareas("/FXML/VerTareas.fxml");
        private VerEmpleados empleados = new VerEmpleados("/FXML/Empleados.fxml");
//        private EstadisticaTracker estadisticaTracker = new EstadisticaTracker("/FXML/Estadisticas.fxml");
        private EstadisticaTracker estadisticaTracker = new EstadisticaTracker("/FXML/estadisticas/guacala.fxml");
        private SharedStates sharedStates = new SharedStates(departamentos_id,empleados_json);

        private Stage stage;
        //IMPOOORTANT SE TIENE QUE AGREGAR LOS CONTROLLERS QUE TIENEN GUI FXML
        Observer[] controllers_FXML = {mainController,agregarTarea,verTareas,empleados,estadisticaTracker};
        //IMPORTANT
        public ControllerInitializer(Stage stage) {
            this.stage = stage;
            initiliaze_observers();
            setJsons();
            setUpstuff();
            loop_over_observers_and_pass_attributes(controllers_FXML);
        }

        /*
        Aqui se agrega observadores a los controladores
         */
        private void initiliaze_observers() {

            mainController.addObservers(agregarTarea,verTareas,empleados,estadisticaTracker);
            agregarTarea.addObservers(mainController,verTareas,estadisticaTracker,agregarTarea,empleados);
            verTareas.addObservers(mainController,agregarTarea);
        }

        /*
        Se pasa el json a controladores
         */
        private void setJsons() {

            Utils.setDepartamentos_id(departamentos_id); //XD

            agregarTarea.setTareas_json(tareas_json);
            agregarTarea.setEmpleados_json(empleados_json);
            estadisticaTracker.setEstadistica_json(estadisticas_json);
            empleados.setDepartamentos_id(departamentos_id);
            agregarTarea.setDepartamentos_id(departamentos_id);
        }

        //IMPORTANT ALL CONTROLLERS FXML NEED THE BORDERPANE
        private void setUpstuff() {
            mainController.share_with_controllers_borderpane();
        }
        /*
        Se pasa a los observadores el stage y sharedstates, y despues se inicializa los observadores despues de tener lo necesario
        Es importante agregar a los controladores FXML a la lista de controladores para agregarlos a eso
         */
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