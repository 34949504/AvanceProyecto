/*
.Todos los controllers extienden a esta clase abstracta para reducir codigo redundante
Inicializa fxml
 */
package org.example.avanceproyecto.ControllerUtils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;
import org.example.avanceproyecto.Controllers.SharedStates;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Padre de los controladores, que guarda variables en comun
 */
@Getter @Setter
public abstract class BaseController implements Observer {
    protected Parent layout;
    private BorderPane borderpane_main;
    private ArrayList<Observer> observers = new ArrayList<>();
    private Stage stage;
    SharedStates sharedStates;


    public BaseController() {

    }

    //I initialize like this because  with the super(), it does not let me use the attributes of the subclass on the initialization, even tho they look initialized,
    //they are not for the abstrac class which calls the javafx method initialize()
    public void initilize_fxml(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setController(this);
            this.layout = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load FXML: " + fxmlFile, e);
        }
    }
    public void addObservers(Observer ... observers_param) {
        observers.addAll(Arrays.asList(observers_param));
    }


    @Override
    public boolean show_layout(Class<?> clazz) {

        if (clazz.isAssignableFrom(this.getClass())) {
            this.borderpane_main.setCenter(getLayout());
            return true;
        }
        return false;
    }
}