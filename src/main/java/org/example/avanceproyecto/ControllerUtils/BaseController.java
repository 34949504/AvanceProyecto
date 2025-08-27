package org.example.avanceproyecto.ControllerUtils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.BorderPane;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public abstract class BaseController implements Observer {
    protected Parent layout;
    private BorderPane borderpane_main;
    private ArrayList<Observer> observers = new ArrayList<>();

    public BaseController(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setController(this); // ✅ Set this instance as controller
            this.layout = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load FXML: " + fxmlFile, e);
        }
    }
    public void addObserver(Observer observer) {
        observers.add(observer);
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



//@Getter
//public abstract class BaseController {
//    protected Parent layout;
//
//    protected BaseController() {} // Empty constructor for FXML instantiation
//
//    public static <T extends BaseController> T load(String fxmlFile) {
//        try {
//            // Try multiple resource loading strategies
//            URL resource = BaseController.class.getResource("/" + fxmlFile); // Root of classpath
//            if (resource == null) {
//                resource = BaseController.class.getResource(fxmlFile); // Relative to BaseController
//            }
//            if (resource == null) {
//                throw new RuntimeException("FXML file not found: " + fxmlFile);
//            }
//
//            FXMLLoader loader = new FXMLLoader(resource);
//            Parent root = loader.load();
//            T controller = loader.getController();
//            controller.layout = root;
//            return controller;
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to load FXML: " + fxmlFile, e);
//        }
//    }
//
//    public Parent getLayout() {
//        return layout;
//    }
//}