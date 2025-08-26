package org.example.avanceproyecto;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.stage.Stage;
import javafx.util.Pair;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;

@Getter @Setter
public abstract class BaseController {
    protected Parent layout;

    public BaseController(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            loader.setController(this); // ✅ Set this instance as controller
            this.layout = loader.load();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load FXML: " + fxmlFile, e);
        }
    }
}
