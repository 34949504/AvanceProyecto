/*
Objecto para mostrar por breves segundos que la operacion fue exitosa creando la tarea
 */
package org.example.avanceproyecto.ControllerUtils;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Toast {

    public static void show(Stage owner, String message) {
        Popup popup = new Popup();

        Label msg = new Label(message);
        msg.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; "
                + "-fx-padding: 10 20; -fx-background-radius: 8; "
                + "-fx-font-size: 14px; -fx-font-weight: bold;");
        msg.setOpacity(0);

        // transparent container (no white background)
        StackPane container = new StackPane(msg);
        container.setPadding(new Insets(10));
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: black;");

        popup.getContent().add(container);

        // Position bottom-right relative to owner
        double x = owner.getX() + owner.getWidth() - 250;
        double y = owner.getY() + owner.getHeight() - 100;
        popup.show(owner, x, y);

        // Fade in & fade out
        FadeTransition fadeIn = new FadeTransition(Duration.millis(300), msg);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(2), msg);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setDelay(Duration.seconds(1.5));
        fadeOut.setOnFinished(e -> popup.hide());

        fadeIn.setOnFinished(e -> fadeOut.play());
        fadeIn.play();
    }
}
