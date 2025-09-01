/*
No se utiliza
 */
package org.example.avanceproyecto.ControllerUtils;

import javafx.animation.FadeTransition;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FadingPopup {

    public static void show(Stage owner, String message) {
        Popup popup = new Popup();

        Label msg = new Label(message);
        msg.setStyle("-fx-background-color: black; -fx-text-fill: white; "
                   + "-fx-padding: 10; -fx-background-radius: 8;");
        msg.setOpacity(1);

        StackPane container = new StackPane(msg);
        popup.getContent().add(container);
        popup.show(owner);

        // Fade transition
        FadeTransition fade = new FadeTransition(Duration.seconds(2), container);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setDelay(Duration.seconds(1)); // stay visible for 1 second before fading
        fade.setOnFinished(e -> popup.hide());
        fade.play();
    }
}
