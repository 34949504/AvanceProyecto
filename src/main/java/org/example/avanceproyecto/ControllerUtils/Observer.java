package org.example.avanceproyecto.ControllerUtils;
import javafx.scene.layout.BorderPane;

public interface Observer {
    default boolean show_layout(Class<?> clazz) {
        return false;
    }
}
