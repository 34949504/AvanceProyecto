package org.example.avanceproyecto.ControllerUtils.Dialogs;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import lombok.Setter;

public abstract class BaseDialog<T> {
    protected Stage dialogStage;
    protected Stage parentStage;
    protected Button okButton;
    protected Button cancelButton;
    protected boolean okClicked = false;
    protected T result;
    
    public BaseDialog(Stage parentStage, String title) {
        this.parentStage = parentStage;
        createDialog(title);
        setupCommonBehavior();
    }

    public BaseDialog(Stage parentStage, String title, Rectangle2D dialog_dims) {
        this.parentStage = parentStage;
        createDialog(title);
        setupCommonBehavior();

        dialogStage.setWidth(dialog_dims.getWidth());
        dialogStage.setHeight(dialog_dims.getHeight());

    }

    private void createDialog(String title) {
        dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(parentStage);
        dialogStage.setResizable(false);
    }
    
    protected abstract GridPane createContent();
    protected abstract boolean validateInput();
    protected abstract T getResult();
    protected abstract void clearValues();

    private void setupCommonBehavior() {
        GridPane content = createContent();
        
        // Add buttons if not already added by subclass
        if (okButton == null || cancelButton == null) {
            createButtons();
            addButtonsToGrid(content);
        }
        
        setupButtonActions();

        Scene scene = new Scene(content);
        setupKeyboardShortcuts(scene);
        dialogStage.setScene(scene);
        
        // Center dialog when shown
        dialogStage.setOnShown(e -> centerDialog());
    }
    
    protected void createButtons() {
        okButton = new Button("OK");
        cancelButton = new Button("Cancel");
    }
    
    protected void addButtonsToGrid(GridPane grid) {
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.getChildren().addAll(cancelButton, okButton);
        
        // Find next available row
        int nextRow = grid.getRowCount();
        grid.add(buttonBox, 1, nextRow);
    }
    
    private void setupButtonActions() {
        okButton.setOnAction(e -> {
            if (validateInput()) {
                okClicked = true;
                result = getResult();
                dialogStage.close();
            }
        });
        
        cancelButton.setOnAction(e -> {
            okClicked = false;
            dialogStage.close();
        });
    }
    
    private void setupKeyboardShortcuts(Scene scene) {
        scene.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ESCAPE) {
                cancelButton.fire();
            } else if (e.getCode() == KeyCode.ENTER && e.isControlDown()) {
                okButton.fire();
            }
        });
    }
    
    private void centerDialog() {
        double centerX = parentStage.getX() + (parentStage.getWidth() / 2) - (dialogStage.getWidth() / 2);
        double centerY = parentStage.getY() + (parentStage.getHeight() / 2) - (dialogStage.getHeight() / 2);
        dialogStage.setX(centerX);
        dialogStage.setY(centerY);
    }
    
    protected void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid Input");
        alert.setHeaderText("Please correct the following:");
        alert.setContentText(message);
        
        alert.setOnShown(e -> {
            alert.getDialogPane().applyCss();
            alert.getDialogPane().autosize();
            double centerX = parentStage.getX() + (parentStage.getWidth() / 2) - (alert.getWidth() / 2);
            double centerY = parentStage.getY() + (parentStage.getHeight() / 2) - (alert.getHeight() / 2);
            alert.setX(centerX);
            alert.setY(centerY);
        });
        
        alert.showAndWait();
    }
    
    public boolean show() {
        okClicked = false;
        dialogStage.showAndWait();
        clearValues();
        return okClicked;
    }
    
    public T getDialogResult() {
        return result;
    }
}

// Task creation result class

// Task Dialog implementation

// Employee selection result class

// Employee Selection Dialog implementation

// Usage example: