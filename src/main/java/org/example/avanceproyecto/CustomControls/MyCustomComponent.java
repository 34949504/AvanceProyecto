package org.example.avanceproyecto.CustomControls;

import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;

public class MyCustomComponent extends VBox {

    private Label statusLabel;
    private Button actionButton;

    public MyCustomComponent() {
        // Initialize UI elements
        statusLabel = new Label("Initial Status");
        actionButton = new Button("Perform Action");

        // Add elements to the VBox
        this.getChildren().addAll(statusLabel, actionButton);

        // Add some styling (optional)
        this.setSpacing(10);

        // Add event handler
        actionButton.setOnAction(event -> {
            statusLabel.setText("Action Performed!");
        });
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
    }
}