package org.example.avanceproyecto;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.effect.*;
import javafx.scene.paint.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;
import javafx.geometry.Insets;

public class CLabel extends Label {

    private Timeline pulseAnimation;
    private Timeline glowAnimation;

    // No-argument constructor for FXML
    public CLabel() {
        super();
        makeBadass();
    }

    // Your existing constructor
    public CLabel(String value) {
        super(value);
        makeBadass();
    }

    public static CLabel valueOf(String value) {
        return new CLabel(value);
    }

    // Add setter for FXML property binding
    public void setValue(String value) {
        this.setText(value);
    }

    public String getValue() {
        return this.getText();
    }

    /**
     * 🔥 MAKE THIS LABEL ABSOLUTELY BADASS 🔥
     */
    private void makeBadass() {

    }

    /**
     * 🎭 HOVER EFFECTS - Mouse interactions
     */
}
